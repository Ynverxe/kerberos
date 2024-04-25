package com.github.ynverxe.kerberos.test.common;

import com.github.ynverxe.kerberos.crud.repository.CrudRepository;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryCrudRepository<T> implements CrudRepository<T> {

  private final Map<String, T> backing = new ConcurrentHashMap<>();

  @Override
  public void save(@NotNull String key, @NotNull T data) throws Throwable {
    backing.put(key, data);
  }

  @Override
  public @Nullable T read(@NotNull String key) throws Throwable {
    return backing.get(key);
  }

  @Override
  public @NotNull Map<String, T> readAll() throws Throwable {
    return new LinkedHashMap<>(backing);
  }

  @Override
  public @NotNull Map<String, T> readAllWithKey(@NotNull String key) throws Throwable {
    T found = backing.get(key);

    if (found == null) return Collections.emptyMap();

    return Collections.singletonMap(key, found);
  }

  @Override
  public boolean delete(@NotNull String key) throws Throwable {
    return backing.remove(key) != null;
  }

  @Override
  public boolean isClosed() {
    return false;
  }

  @Override
  public void close() throws Exception {
    throw new UnsupportedOperationException("Cannot close");
  }
}