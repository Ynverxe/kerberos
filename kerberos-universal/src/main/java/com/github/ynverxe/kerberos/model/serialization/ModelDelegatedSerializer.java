package com.github.ynverxe.kerberos.model.serialization;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Function;

/**
 * A serializer that can be used to delegate the serialization process to the
 * model itself.
 * For deserialization process, you can provide a function to deserialize the model manually
 * or pass instead a {@link ReflectiveModelDeserializer}.
 */
public class ModelDelegatedSerializer<D, T extends AutonomousModel<D>> implements ModelSerializer<D, T> {

  private final Function<D, T> deserializerFunction;

  public ModelDelegatedSerializer(@NotNull Function<D, T> deserializerFunction) {
    this.deserializerFunction = Objects.requireNonNull(deserializerFunction);
  }

  @Override
  public @NotNull D serializeModel(@NotNull String id, @NotNull T model) throws Throwable {
    return model.serialize();
  }

  @Override
  public @NotNull T deserializeModel(@NotNull String id, @NotNull D modelData) throws Throwable {
    return deserializerFunction.apply(modelData);
  }

  public static <D, T extends AutonomousModel<D>> @NotNull ModelDelegatedSerializer<D, T> of(@NotNull Function<D, T> deserializerFunction) {
    return new ModelDelegatedSerializer<>(deserializerFunction);
  }

  public static <D, T extends AutonomousModel<D>> @NotNull ModelDelegatedSerializer<D, T> reflective(@NotNull Class<T> modelClass) {
    return new ModelDelegatedSerializer<>(new ReflectiveModelDeserializer<>(modelClass));
  }
}