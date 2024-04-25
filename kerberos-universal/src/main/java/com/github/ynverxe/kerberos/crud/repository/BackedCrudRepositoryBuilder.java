package com.github.ynverxe.kerberos.crud.repository;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public abstract class BackedCrudRepositoryBuilder<T, S, B extends BackedCrudRepositoryBuilder<T, S, B>>
  extends AbstractCrudRepositoryBuilder<T, B> {

  protected CrudRepository<S> backing;

  public B backing(@NotNull CrudRepository<S> backing) {
    this.backing = Objects.requireNonNull(backing, "backing");
    return self();
  }
}