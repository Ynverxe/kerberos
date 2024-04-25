package com.github.ynverxe.kerberos.crud.repository;

import com.github.ynverxe.kerberos.crud.repository.adapter.DataMapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

import static java.util.Objects.requireNonNull;

public class AdaptedCrudRepository<I, O> extends AbstractCrudRepository<O>
  implements BackedRepository<O, I> {

  private final @NotNull CrudRepository<I> backing;
  private final @NotNull DataMapper<I, O> adapter;

  public AdaptedCrudRepository(@NotNull Encoder<O> encoder, @NotNull CrudRepository<I> backing, @NotNull DataMapper<I, O> adapter) {
    super(encoder);
    this.backing = requireNonNull(backing, "backing");
    this.adapter = requireNonNull(adapter, "adapter");
  }

  @Override
  public void performSave(@NotNull String key, @NotNull O data) throws Throwable {
    backing.save(key, adapter.adaptToBackingData(data));
  }

  @Override
  public @Nullable O performRead(@NotNull String key) throws Throwable {
    I found = backing.read(key);

    if (found == null) return null;

    return adapter.adaptToNewData(found);
  }

  @Override
  public @NotNull Map<String, O> performReadAll() throws Throwable {
    Map<String, I> data = backing.readAll();
    return adaptMap(data);
  }

  @Override
  public @NotNull Map<String, O> performReadAllWithKey(@NotNull String key) throws Throwable {
    Map<String, I> data = backing.readAllWithKey(key);
    return adaptMap(data);
  }

  @Override
  public boolean delete(@NotNull String key) throws Throwable {
    return backing.delete(key);
  }

  @Override
  public CrudRepository<I> backingRepository() {
    return backing;
  }

  public DataMapper<I, O> adapter() {
    return adapter;
  }

  private Map<String, O> adaptMap(Map<String, I> data) throws Throwable {
    Map<String, O> adapted = new LinkedHashMap<>();

    for (Map.Entry<String, I> entry : data.entrySet()) {
      String key = entry.getKey();
      I value = entry.getValue();
      adapted.put(key, adapter.adaptToNewData(value));
    }

    return adapted;
  }

  @Override
  public boolean isClosed() {
    return backing.isClosed();
  }

  @Override
  public void close() throws Exception {
    backing.close();
  }

  public static class Builder<I, O> extends BackedCrudRepositoryBuilder<O, I, Builder<I, O>> {

    private DataMapper<I, O> adapter;

    public Builder<I, O> mapper(@NotNull DataMapper<I, O> adapter) {
      this.adapter = requireNonNull(adapter, "adapter");
      return this;
    }

    @Override
    public @NotNull AdaptedCrudRepository<I, O> build() {
      return new AdaptedCrudRepository<>(encoder, backing, adapter);
    }
  }
}