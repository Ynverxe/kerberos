package com.github.ynverxe.kerberos.crud.repository;

import com.github.ynverxe.kerberos.util.Closeable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * A model to execute generic database operations.
 *
 * @param <T> The data type that this repository uses.
 * @see BackedRepository
 * @see BridgeRepository
 */
public interface CrudRepository<T> extends Closeable, AdapterFactoryRepository<T> {

  void save(@NotNull String key, @NotNull T data) throws Throwable;

  @Nullable T read(@NotNull String key) throws Throwable;

  @NotNull Map<String, T> readAll() throws Throwable;

  @NotNull Map<String, T> readAllWithKey(@NotNull String key) throws Throwable;

  boolean delete(@NotNull String key) throws Throwable;

  static <I, O> AdaptedCrudRepository.Builder<I, O> adaptedRepositoryBuilder() {
    return new AdaptedCrudRepository.Builder<>();
  }

  static <T> DelegatedCrudRepository.Builder<T> delegatedRepositoryBuilder() {
    return new DelegatedCrudRepository.Builder<>();
  }
}