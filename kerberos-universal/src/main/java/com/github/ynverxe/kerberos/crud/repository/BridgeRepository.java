package com.github.ynverxe.kerberos.crud.repository;

/**
 * "Bridge" repositories are repositories that interacts directly with the database.
 *
 * @param <T> The repository data type
 * @see BackedRepository
 */
public interface BridgeRepository<T> extends CrudRepository<T> {
}