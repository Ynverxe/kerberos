package com.github.ynverxe.kerberos.crud.repository;

import com.github.ynverxe.kerberos.util.Builder;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

public abstract class AbstractCrudRepositoryBuilder<T, B extends AbstractCrudRepositoryBuilder<T, B>>
  implements Builder<CrudRepository<T>> {

  protected Encoder<T> encoder = Encoder.phantom();

  @Contract("_ -> this")
  public B encoder(@NotNull Encoder<T> encoder) {
    this.encoder = requireNonNull(encoder, "encoder");
    return self();
  }

  @Contract("_ -> new")
  public <S extends BackedCrudRepositoryBuilder<?, T, S>> S build(@NotNull Supplier<S> builderSupplier) throws Throwable {
    return builderSupplier.get()
      .backing(build());
  }

  @SuppressWarnings("unchecked")
  protected B self() {
    return (B) this;
  }
}