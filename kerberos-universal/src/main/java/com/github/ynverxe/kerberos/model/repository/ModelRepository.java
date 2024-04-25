package com.github.ynverxe.kerberos.model.repository;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * This class is a model that wants to:
 * <ul>
 *   <li>Serialize the models and store the data into the database</li>
 *   <li>Get the database model's data and convert it into a new model of type <b>T</b></li>
 *   <li>Provide async method to work</li>
 * </ul>
 *
 * @param <T> The model type
 * @see ModelRepositoryImpl
 */
public interface ModelRepository<T extends SavableModel> {

  void saveModel(@NotNull T model) throws Throwable;

  @Nullable T findModel(@NotNull String id) throws Throwable;

  @NotNull Map<String, T> findAllWithId(@NotNull String id) throws Throwable;

  @NotNull Map<String, T> findAll() throws Throwable;

  boolean deleteModel(@NotNull String id) throws Throwable;

  @NotNull CompletableFuture<Boolean> deleteModelAsync(@NotNull String id);

  @NotNull CompletableFuture<Void> saveModelAsync(@NotNull T model);

  @NotNull CompletableFuture<T> findModelAsync(@NotNull String id);

  @NotNull CompletableFuture<Map<String, T>> findAllWithIdAsync(@NotNull String id);

  @NotNull CompletableFuture<Map<String, T>> findAllAsync();

}