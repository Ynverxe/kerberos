package com.github.ynverxe.kerberos.crud.repository;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;

public abstract class AbstractCrudRepository<T> implements CrudRepository<T> {

  private final Encoder<T> encoder;

  protected AbstractCrudRepository(@NotNull Encoder<T> encoder) {
    this.encoder = Objects.requireNonNull(encoder, "encoder");
  }

  @Override
  public final void save(@NotNull String key, @NotNull T data) throws Throwable {
    performSave(key, encoder.encode(data));
  }

  @Override
  public final @Nullable T read(@NotNull String key) throws Throwable {
    T read = performRead(key);

    if (read == null) return null;

    return encoder.decode(read);
  }

  @Override
  public final @NotNull Map<String, T> readAll() throws Throwable {
    Map<String, T> dataMap = performReadAll();
    decodeAll(dataMap);
    return dataMap;
  }

  @Override
  public final @NotNull Map<String, T> readAllWithKey(@NotNull String key) throws Throwable {
    Map<String, T> dataMap = performReadAllWithKey(key);
    decodeAll(dataMap);
    return dataMap;
  }

  protected abstract void performSave(@NotNull String key, @NotNull T data) throws Throwable;

  protected abstract T performRead(@NotNull String key) throws Throwable;

  protected abstract Map<String, T> performReadAll() throws Throwable;

  protected abstract @NotNull Map<String, T> performReadAllWithKey(@NotNull String key) throws Throwable;

  protected void decodeAll(Map<String, T> map) throws Throwable {
    for (Map.Entry<String, T> entry : map.entrySet()) {
      entry.setValue(encoder.decode(entry.getValue()));
    }
  }
}