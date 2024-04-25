package com.github.ynverxe.kerberos.model.repository.operation;

import org.jetbrains.annotations.NotNull;

import static java.util.Objects.requireNonNull;

public class GenericOperation implements Operation {

  private final @NotNull String id;
  private final @NotNull RepositoryOperationType type;

  public GenericOperation(@NotNull String id, @NotNull RepositoryOperationType type) {
    this.id = requireNonNull(id, "id");
    this.type = requireNonNull(type, "type");
  }

  @Override
  public @NotNull String id() {
    return id;
  }

  @Override
  public @NotNull RepositoryOperationType type() {
    return type;
  }
}