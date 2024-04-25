package com.github.ynverxe.kerberos;

import com.github.ynverxe.kerberos.model.repository.exception.ExceptionHandler;
import com.github.ynverxe.kerberos.util.Builder;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executor;

import static java.util.Objects.requireNonNull;

public class ModelStorageOptionsBuilder<D> implements Builder<ModelStorageOptions<D>> {

  private @NotNull Executor executor = Runnable::run;
  private @NotNull ExceptionHandler exceptionHandler = ExceptionHandler.justThrowIt();

  ModelStorageOptionsBuilder(ModelStorageOptions<D> options) {
    this.executor = options.executor();
    this.exceptionHandler = options.exceptionHandler();
  }

  public ModelStorageOptionsBuilder() {
  }

  @Contract("_ -> this")
  public ModelStorageOptionsBuilder<D> executor(@NotNull Executor executor) {
    this.executor = requireNonNull(executor, "executor");
    return this;
  }

  @Contract("_ -> this")
  public ModelStorageOptionsBuilder<D> exceptionHandler(@NotNull ExceptionHandler exceptionHandler) {
    this.exceptionHandler = requireNonNull(exceptionHandler, "exceptionHandler");
    return this;
  }

  public @NotNull ModelStorageOptions<D> build() {
    return new ModelStorageOptions<>(executor, exceptionHandler);
  }

  public static <D> @NotNull ModelStorageOptionsBuilder<D> create() {
    return new ModelStorageOptionsBuilder<>();
  }
}