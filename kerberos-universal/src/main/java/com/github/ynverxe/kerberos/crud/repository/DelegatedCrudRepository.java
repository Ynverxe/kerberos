package com.github.ynverxe.kerberos.crud.repository;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;

/**
 * An extendable class to make custom CrudRepository
 * that needs a delegate.
 *
 * @param <T> The data type
 */
public class DelegatedCrudRepository<T> extends AbstractCrudRepository<T> implements BackedRepository<T, T> {

  private final CrudRepository<T> backing;

  protected DelegatedCrudRepository(@NotNull Encoder<T> encoder, @NotNull CrudRepository<T> backing) {
    super(encoder);
    this.backing = Objects.requireNonNull(backing, "backing");
  }

  @Override
  public void performSave(@NotNull String key, @NotNull T data) throws Throwable {
    backing.save(key, data);
  }

  @Override
  @Nullable
  public T performRead(@NotNull String key) throws Throwable {
    return backing.read(key);
  }

  @Override
  public @NotNull Map<String, T> performReadAll() throws Throwable {
    return backing.readAll();
  }

  @Override
  public @NotNull Map<String, T> performReadAllWithKey(@NotNull String key) throws Throwable {
    return backing.readAllWithKey(key);
  }

  @Override
  public boolean delete(@NotNull String key) throws Throwable {
    return backing.delete(key);
  }

  @Override
  public boolean isClosed() {
    return backing.isClosed();
  }

  @Override
  public void close() throws Exception {
    backing.close();
  }

  @Override
  public CrudRepository<T> backingRepository() {
    return backing;
  }

  public static class Builder<T> extends BackedCrudRepositoryBuilder<T, T, Builder<T>> {

    @Override
    public @NotNull DelegatedCrudRepository<T> build() {
      return new DelegatedCrudRepository<>(encoder, backing);
    }
  }
}