package com.github.ynverxe.kerberos.model.repository.operation;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class ReadOperation<T> implements Operation {

  private final String id;
  private T fallbackModel;

  ReadOperation(String id) {
    this.id = id;
  }

  @Override
  public String id() {
    return id;
  }

  @Override
  public @NotNull RepositoryOperationType type() {
    return RepositoryOperationType.READ;
  }

  public T fallbackResult() {
    return fallbackModel;
  }

  public ReadOperation<T> fallbackResult(T fallbackModel) {
    this.fallbackModel = fallbackModel;
    return this;
  }

  public static class ReadAll<T> extends ReadOperation<Map<String, T>> {

    ReadAll(String id) {
      super(id);
    }

    /**
     * When this method returns a non-null value, that means that
     * the operation aimed to collect all the entries that match
     * the provided id, if returns null, the operation aimed to
     * collect all existent entries in a database.
     */
    @Override
    public @Nullable String id() {
      return super.id();
    }
  }
}