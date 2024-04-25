package com.github.ynverxe.kerberos.model.repository.operation;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Operation {

  /**
   * Nullable on {@link ReadOperation.ReadAll#id()}.
   *
   * @return the id of the model that the operation points to.
   */
  @Nullable String id();

  /**
   * @return The operation type.
   */
  @NotNull RepositoryOperationType type();

  static @NotNull Operation generic(@NotNull String id, @NotNull RepositoryOperationType type) {
    return new GenericOperation(id, type);
  }

  static <T> @NotNull ReadOperation<T> read(@NotNull String id) {
    return new ReadOperation<>(id);
  }

  static <T> ReadOperation.@NotNull ReadAll<T> readAll(@Nullable String id) {
    return new ReadOperation.ReadAll<>(id);
  }
}