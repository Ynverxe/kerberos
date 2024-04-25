package com.github.ynverxe.kerberos.model.serialization;

import com.github.ynverxe.kerberos.model.serialization.annotation.DeserializationVia;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.Objects;
import java.util.function.Function;

/**
 * A Reflective deserializer for {@link AutonomousModel} implementations.
 * AutonomousModels can have a constructor or a static method that receives
 * the raw data to create a new instance of them.
 * <pre> {@code
 *  @DeserializationVia.Constructor(argumentType = Map.class)
 *  public class Dog implements AutonomousModel<Map<String, Object>> {
 *    private final int age;
 *
 *    public Dog(int age) {
 *      this.age = age;
 *    }
 *
 *    public Dog(Map<String, Object> rawData) {
 *      this((Integer) rawData.get("age"));
 *    }
 *
 *    public int age() {
 *      return age;
 *    }
 *
 *    @Override
 *    public Map<String, Object> serialize() {
 *      return Collections.singletonMap("age", age);
 *    }
 *  }
 * }
 * </pre>
 * Or:
 *
 * <pre> {@code
 *  @DeserializationVia.Method(argumentType = Map.class, name = "deserializeDog")
 *  public class Dog implements AutonomousModel<Map<String, Object>> {
 *    private final int age;
 *
 *    public Dog(int age) {
 *      this.age = age;
 *    }
 *
 *    public int age() {
 *      return age;
 *    }
 *
 *    @Override
 *    public Map<String, Object> serialize() {
 *      return Collections.singletonMap("age", age);
 *    }
 *
 *    public static Dog deserializeDog(Map<String, Object> rawData) {
 *      return new Dog((Integer) rawData.get("age"));
 *    }
 *  }
 * }
 * </pre>
 *
 * @param <D> The data type
 * @param <T> The model type
 */
public class ReflectiveModelDeserializer<D, T extends AutonomousModel<D>> implements Function<D, T> {

  private final AccessibleObject reflectiveObject;

  private final Class<T> modelClass;
  private final Annotation deserializationVia;

  public ReflectiveModelDeserializer(@NotNull Class<T> modelClass) {
    this.modelClass = Objects.requireNonNull(modelClass, "modelClass");
    this.deserializationVia = matchAnnotation(modelClass);

    try {
      this.reflectiveObject = inspect();
    } catch (NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public @NotNull T apply(@NotNull D data) {
    try {
      if (reflectiveObject instanceof Constructor) {
        return ((Constructor<T>) reflectiveObject).newInstance(data);
      }

      return (T) ((Method) reflectiveObject).invoke(null, data);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private AccessibleObject inspect() throws NoSuchMethodException {
    AccessibleObject accessibleObject;

    if (deserializationVia instanceof DeserializationVia.Constructor) {
      DeserializationVia.Constructor constructorData = (DeserializationVia.Constructor) deserializationVia;

      accessibleObject = modelClass.getDeclaredConstructor(constructorData.argumentType());
    } else {
      DeserializationVia.Method methodData = (DeserializationVia.Method) deserializationVia;
      Method method = modelClass.getDeclaredMethod(methodData.name(), methodData.argumentType());

      if (!Modifier.isStatic(method.getModifiers())) {
        throw new IllegalStateException("Method '" + method + "' is not static");
      }

      if (!modelClass.isAssignableFrom(method.getReturnType())) {
        throw new IllegalStateException("Method '" + method + "' doesn't returns an instance of model '" + modelClass + "'");
      }

      accessibleObject = method;
    }

    if (!accessibleObject.isAccessible()) {
      accessibleObject.setAccessible(true);
    }

    return accessibleObject;
  }

  private static Annotation matchAnnotation(Class<?> clazz) {
    for (Annotation declaredAnnotation : clazz.getDeclaredAnnotations()) {
      Class<?> type = declaredAnnotation.annotationType();

      if (type == DeserializationVia.Constructor.class || type == DeserializationVia.Method.class) {
        return declaredAnnotation;
      }
    }

    throw new IllegalStateException("Class '" + clazz + "' doesn't have an specified deserialization via");
  }

  /**
   * Creates a new {@link ModelDelegatedSerializer} with a {@link ReflectiveModelDeserializer}
   * given for model deserialization.
   *
   * @param modelClass The model class
   * @param <D>        The data type
   * @param <T>        The model type
   * @return A new {@link ModelDelegatedSerializer}.
   */
  public static <D, T extends AutonomousModel<D>> @NotNull ModelSerializer<D, T> modelSerializer(@NotNull Class<T> modelClass) {
    return new ModelDelegatedSerializer<>(new ReflectiveModelDeserializer<>(modelClass));
  }
}