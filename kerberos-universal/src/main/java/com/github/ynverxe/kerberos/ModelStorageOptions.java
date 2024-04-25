package com.github.ynverxe.kerberos;

import com.github.ynverxe.kerberos.model.repository.exception.ExceptionHandler;
import com.github.ynverxe.kerberos.util.Buildable;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executor;

import static java.util.Objects.requireNonNull;

public class ModelStorageOptions<D> implements Buildable<ModelStorageOptionsBuilder<D>> {

  private final Executor executor;
  private final ExceptionHandler exceptionHandler;

  public ModelStorageOptions(@NotNull Executor executor, @NotNull ExceptionHandler exceptionHandler) {
    requireNonNull(executor, "executor");
    requireNonNull(exceptionHandler, "exceptionHandler");

    this.executor = executor;
    this.exceptionHandler = exceptionHandler;
  }

  public @NotNull Executor executor() {
    return executor;
  }

  public @NotNull ExceptionHandler exceptionHandler() {
    return exceptionHandler;
  }

  public @NotNull ModelStorageOptionsBuilder<D> toBuilder() {
    return new ModelStorageOptionsBuilder<>(this);
  }

  public static <D> @NotNull ModelStorageOptionsBuilder<D> builder() {
    return new ModelStorageOptionsBuilder<>();
  }

  public static <D> @NotNull ModelStorageOptions<D> defaults() {
    try {
      return ModelStorageOptions.<D>builder().build();
    } catch (Throwable throwable) {
      throw new RuntimeException(throwable);
    }
  }
}