package com.github.ynverxe.kerberos;

import com.github.ynverxe.kerberos.crud.repository.CrudRepository;
import com.github.ynverxe.kerberos.model.repository.SavableModel;
import com.github.ynverxe.kerberos.model.serialization.ModelSerializer;
import com.github.ynverxe.kerberos.util.Builder;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

public class ModelStorageBuilder<D, T extends SavableModel> implements Builder<ModelStorage<D, T>> {

  private CrudRepository<D> crudRepository;
  private ModelSerializer<D, T> modelSerializer;
  private ModelStorageOptions<D> options = ModelStorageOptions.defaults();

  ModelStorageBuilder(ModelStorage<D, T> modelStorage) {
    this.options = modelStorage.options();
    this.crudRepository = modelStorage.crudRepository();
    this.modelSerializer = modelStorage.modelSerializer();
  }

  public ModelStorageBuilder() {
  }

  @Contract("_ -> this")
  public ModelStorageBuilder<D, T> crudRepository(@NotNull CrudRepository<D> crudRepository) {
    this.crudRepository = requireNonNull(crudRepository, "crudRepository");
    return this;
  }

  @Contract("_ -> this")
  public ModelStorageBuilder<D, T> modelSerializer(@NotNull ModelSerializer<D, T> modelSerializer) {
    this.modelSerializer = requireNonNull(modelSerializer, "modelSerializer");;
    return this;
  }

  @Contract("_ -> this")
  public ModelStorageBuilder<D, T> options(@NotNull ModelStorageOptions<D> options) {
    this.options = requireNonNull(options, "options");
    return this;
  }

  @Contract("_ -> this")
  public ModelStorageBuilder<D, T> optionsBuilder(@NotNull Builder<ModelStorageOptions<D>> optionsBuilder) {
    try {
      return this.options(optionsBuilder.build());
    } catch (Throwable throwable) {
      throw new RuntimeException(throwable);
    }
  }

  @Contract("_ -> this")
  public ModelStorageBuilder<D, T> optionsBuilder(Consumer<ModelStorageOptionsBuilder<D>> optionsBuilderConsumer) {
    ModelStorageOptionsBuilder<D> builder = ModelStorageOptionsBuilder.create();
    optionsBuilderConsumer.accept(builder);
    return this.optionsBuilder(builder);
  }

  @Contract("_ -> this")
  public ModelStorageBuilder<D, T> options(Function<ModelStorageOptions<D>, ModelStorageOptions<D>> optionsTransformer) {
    return this.options(optionsTransformer.apply(options));
  }

  @Override
  public @NotNull ModelStorage<D, T> build() {
    return new ModelStorage<>(crudRepository, modelSerializer, options);
  }
}