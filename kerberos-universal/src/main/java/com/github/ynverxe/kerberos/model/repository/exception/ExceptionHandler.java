package com.github.ynverxe.kerberos.model.repository.exception;

import com.github.ynverxe.kerberos.model.repository.ModelRepository;
import com.github.ynverxe.kerberos.model.repository.SavableModel;
import com.github.ynverxe.kerberos.model.repository.operation.Operation;
import com.github.ynverxe.kerberos.model.repository.operation.ReadOperation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ExceptionHandler {

  default void handleFailedException(@NotNull Operation operation, @NotNull Throwable throwable) throws Throwable {
  }

  default void handleReadOperation(@NotNull ReadOperation<?> operation, @NotNull Throwable throwable) throws Throwable {
    handleFailedException(operation, throwable);
  }

  default void handleReadAllOperation(@NotNull ReadOperation.ReadAll<?> operation, @NotNull Throwable throwable) throws Throwable {
    handleFailedException(operation, throwable);
  }

  /**
   * Called when trying to get the id of a model during
   * {@link ModelRepository#saveModel(SavableModel)}.
   *
   * @return The fallback id of the model or null.
   */
  default @Nullable String listenModelException(@NotNull SavableModel model, @NotNull Throwable throwable) throws Throwable {
    throw throwable;
  }

  static ExceptionHandler justThrowIt() {
    return new ExceptionHandler() {
      @Override
      public void handleFailedException(@NotNull Operation operation, @NotNull Throwable throwable) throws Throwable {
        throw throwable;
      }
    };
  }

  static @NotNull ExceptionDelegator newDelegator() {
    return new ExceptionDelegator();
  }
}