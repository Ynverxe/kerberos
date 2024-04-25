package com.github.ynverxe.kerberos.crud.repository;

import com.github.ynverxe.kerberos.crud.repository.adapter.DataMapper;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public interface AdapterFactoryRepository<T> {

  default <O> AdaptedCrudRepository<T, O> adapted(@NotNull DataMapper<T, O> dataAdapter) {
    return passThisToBuilder(CrudRepository::<T, O>adaptedRepositoryBuilder)
      .mapper(dataAdapter)
      .build();
  }

  default <B extends BackedCrudRepositoryBuilder<?, T, B>> B passThisToBuilder(Supplier<? extends B> builderSupplier) {
    return builderSupplier.get()
      .backing((CrudRepository<T>) this);
  }
}