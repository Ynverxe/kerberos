package com.github.ynverxe.kerberos.crud.repository;

public interface BackedRepository<T, D> extends CrudRepository<T> {

  CrudRepository<D> backingRepository();

}