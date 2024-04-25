package com.github.ynverxe.kerberos.model.repository;

import com.github.ynverxe.kerberos.model.repository.exception.ExceptionHandler;
import com.github.ynverxe.kerberos.model.repository.operation.Operation;
import com.github.ynverxe.kerberos.model.repository.operation.ReadOperation;
import com.github.ynverxe.kerberos.model.repository.operation.RepositoryOperationType;
import com.github.ynverxe.kerberos.model.serialization.ModelSerializer;
import com.github.ynverxe.kerberos.ModelStorage;
import com.github.ynverxe.kerberos.ModelStorageOptions;
import com.github.ynverxe.kerberos.crud.repository.CrudRepository;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

import static java.util.concurrent.CompletableFuture.*;

/**
 * This class is responsible for:
 *
 * <ul>
 *   <li>The mentioned on {@link ModelRepository} documentation</li>
 *   <li>Handle the errors and delegate them to an {@link ExceptionHandler}</li>
 * </ul>
 * <p>
 * All of this are possible using a {@link CrudRepository}, a
 * {@link ModelSerializer} and a {@link Executor} provided by
 * a {@link ModelStorage}.
 *
 * @param <D> The database data type
 * @param <T> The model type
 */
public class ModelRepositoryImpl<D, T extends SavableModel> implements ModelRepository<T> {

  private final CrudRepository<D> crudRepository;
  private final ModelSerializer<D, T> serializer;
  private final Executor executor;
  private final ExceptionHandler exceptionHandler;

  public ModelRepositoryImpl(@NotNull ModelStorage<D, T> modelStorage) {
    ModelStorageOptions<D> options = modelStorage.options();
    this.crudRepository = modelStorage.crudRepository();
    this.executor = options.executor();
    this.exceptionHandler = options.exceptionHandler();
    this.serializer = modelStorage.modelSerializer();
  }

  @Override
  public void saveModel(@NotNull T model) throws Throwable {
    String modelId = resolveId(model);

    try {
      D serialized = serializer.serializeModel(modelId, model);

      crudRepository.save(modelId, serialized);
    } catch (Throwable e) {
      exceptionHandler.handleFailedException(Operation.generic(modelId, RepositoryOperationType.UPDATE), e);
    }
  }

  @Override
  public @Nullable T findModel(@NotNull String id) throws Throwable {
    try {
      D foundData = crudRepository.read(id);

      if (foundData == null) {
        return null;
      }

      return serializer.deserializeModel(id, foundData);
    } catch (Throwable e) {
      ReadOperation<T> readOperation = Operation.read(id);
      T fallbackModel;

      exceptionHandler.handleReadOperation(readOperation, e);
      fallbackModel = readOperation.fallbackResult();

      if (fallbackModel != null) {
        return fallbackModel;
      }

      throw e;
    }
  }

  @Override
  public @NotNull Map<String, T> findAllWithId(@NotNull String id) throws Throwable {
    return readMultipleModels(id, repository -> repository.readAllWithKey(id));
  }

  @Override
  public @NotNull Map<String, T> findAll() throws Throwable {
    return readMultipleModels(null, CrudRepository::readAll);
  }

  private Map<String, T> readMultipleModels(@Nullable String id, AdaptedFunction<CrudRepository<D>, Map<String, D>> function) throws Throwable {
    try {
      Map<String, D> foundData = function.apply(crudRepository);

      if (foundData.isEmpty()) {
        return Collections.emptyMap();
      }

      Map<String, T> models = new LinkedHashMap<>();
      for (Map.Entry<String, D> value : foundData.entrySet()) {
        String modelId = value.getKey();
        models.put(modelId, serializer.deserializeModel(modelId, value.getValue()));
      }

      return models;
    } catch (Throwable e) {
      ReadOperation.ReadAll<T> readAllOperation = Operation.readAll(id);

      exceptionHandler.handleReadAllOperation(readAllOperation, e);
      Map<String, T> fallbackResult = readAllOperation.fallbackResult();

      if (fallbackResult != null) {
        return fallbackResult;
      }

      throw e;
    }
  }

  @Override
  public boolean deleteModel(@NotNull String id) throws Throwable {
    try {
      return crudRepository.delete(id);
    } catch (Throwable e) {
      exceptionHandler.handleFailedException(Operation.generic(id, RepositoryOperationType.DELETE), e);

      throw e;
    }
  }

  @Override
  public @NotNull CompletableFuture<Boolean> deleteModelAsync(@NotNull String id) {
    return supply(() -> deleteModel(id));
  }

  @Override
  public @NotNull CompletableFuture<Void> saveModelAsync(@NotNull T model) {
    return run(() -> saveModel(model));
  }

  @Override
  public @NotNull CompletableFuture<T> findModelAsync(@NotNull String id) {
    return supply(() -> findModel(id));
  }

  @Override
  public @NotNull CompletableFuture<Map<String, T>> findAllWithIdAsync(@NotNull String id) {
    return supply(() -> findAllWithId(id));
  }

  @Override
  public @NotNull CompletableFuture<Map<String, T>> findAllAsync() {
    return supply(this::findAll);
  }

  private <V> CompletableFuture<V> supply(AdaptedSupplier<V> supplier) {
    return supplyAsync(supplier, executor);
  }

  private CompletableFuture<Void> run(AdaptedRunnable runnable) {
    return runAsync(runnable, executor);
  }

  private String resolveId(T model) throws Throwable {
    try {
      return model.id();
    } catch (Throwable e) {
      String fallbackId = exceptionHandler.listenModelException(model, e);

      if (fallbackId == null) throw e;

      return fallbackId;
    }
  }

  interface AdaptedFunction<I, O> {
    O apply(I input) throws Throwable;
  }

  interface AdaptedSupplier<T> extends Supplier<T> {
    @Override
    default T get() {
      try {
        return getValue();
      } catch (RuntimeException runtimeException) {
        throw runtimeException;
      } catch (Throwable throwable) {
        throw new RuntimeException(throwable);
      }
    }

    T getValue() throws Throwable;
  }

  interface AdaptedRunnable extends Runnable {
    @Override
    default void run() {
      try {
        execute();
      } catch (RuntimeException runtimeException) {
        throw runtimeException;
      } catch (Throwable throwable) {
        throw new RuntimeException(throwable);
      }
    }

    void execute() throws Throwable;
  }
}