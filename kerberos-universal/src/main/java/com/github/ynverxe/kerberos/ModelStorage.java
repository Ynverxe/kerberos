package com.github.ynverxe.kerberos;

import com.github.ynverxe.kerberos.crud.repository.CrudRepository;
import com.github.ynverxe.kerberos.model.repository.ModelRepository;
import com.github.ynverxe.kerberos.model.repository.ModelRepositoryImpl;
import com.github.ynverxe.kerberos.model.repository.SavableModel;
import com.github.ynverxe.kerberos.model.serialization.ModelSerializer;
import com.github.ynverxe.kerberos.util.Buildable;
import org.jetbrains.annotations.NotNull;

import static java.util.Objects.requireNonNull;

/**
 * A ModelStorage is one of the most relevant class of <b>kerberos-universal</b> module.
 * It contains all the necessary things to provide a functionally {@link ModelRepository}. This class
 * is immutable, so you can construct a new one using the features of an already created instance
 * using {@link #toBuilder()}.
 * To build a new ModelStorage from zero, use {@link #builder()}.
 *
 * @param <D> The data type that the <b>T</b> model is serialized and deserialized
 * @param <T> The model type
 * @see CrudRepository
 * @see ModelSerializer
 * @see ModelRepository
 * @see ModelStorageOptions
 * @see ModelStorageBuilder
 */
public class ModelStorage<D, T extends SavableModel> implements Buildable<ModelStorageBuilder<D, T>> {

  private final @NotNull CrudRepository<D> crudRepository;
  private final @NotNull ModelSerializer<D, T> modelSerializer;
  private final @NotNull ModelRepository<T> modelRepository;
  private final @NotNull ModelStorageOptions<D> options;

  public ModelStorage(
    @NotNull CrudRepository<D> crudRepository,
    @NotNull ModelSerializer<D, T> modelSerializer,
    @NotNull ModelStorageOptions<D> options
  ) {
    this.crudRepository = requireNonNull(crudRepository, "crudRepository");
    this.modelSerializer = requireNonNull(modelSerializer, "modelSerializer");
    this.options = requireNonNull(options, "options");
    this.modelRepository = new ModelRepositoryImpl<>(this);
  }

  /**
   * @return The CrudRepository that interacts with the database.
   */
  public @NotNull CrudRepository<D> crudRepository() {
    return crudRepository;
  }

  /**
   * @return The one in charge of serialize/deserialize the models
   * from/to the database data type.
   */
  public @NotNull ModelSerializer<D, T> modelSerializer() {
    return modelSerializer;
  }

  /**
   * @return The options.
   */
  public @NotNull ModelStorageOptions<D> options() {
    return options;
  }

  /**
   * @return The ModelRepository created by this class.
   */
  public @NotNull ModelRepository<T> modelRepository() {
    return modelRepository;
  }

  /**
   * @return A new {@link ModelStorageBuilder} configured with the parameters of this class.
   */
  @Override
  public @NotNull ModelStorageBuilder<D, T> toBuilder() {
    return new ModelStorageBuilder<>(this);
  }

  /**
   * @return A new {@link ModelStorageBuilder} with default configuration.
   */
  public static <D, T extends SavableModel> @NotNull ModelStorageBuilder<D, T> builder() {
    return new ModelStorageBuilder<>();
  }
}
