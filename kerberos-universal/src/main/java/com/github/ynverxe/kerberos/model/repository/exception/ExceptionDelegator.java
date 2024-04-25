package com.github.ynverxe.kerberos.model.repository.exception;

import com.github.ynverxe.kerberos.model.repository.SavableModel;
import com.github.ynverxe.kerberos.model.repository.operation.Operation;
import com.github.ynverxe.kerberos.model.repository.operation.ReadOperation;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Objects.requireNonNull;

public class ExceptionDelegator implements ExceptionHandler {

  private volatile ExceptionHandler fallbackHandler = ExceptionHandler.justThrowIt();
  private final Map<Class<?>, ExceptionHandler> exceptionHandlerMap = new ConcurrentHashMap<>();

  @Contract("_, _ -> this")
  public ExceptionDelegator addHandler(
    @NotNull Class<? extends Throwable> exceptionType, @NotNull ExceptionHandler exceptionHandler) {
    this.exceptionHandlerMap.put(requireNonNull(exceptionType, "exceptionType"), requireNonNull(exceptionHandler, "exceptionHandler"));
    return this;
  }

  public @NotNull ExceptionHandler fallbackHandler() {
    return fallbackHandler;
  }

  @Contract("_ -> this")
  public ExceptionDelegator fallbackHandler(@NotNull ExceptionHandler fallbackHandler) {
    this.fallbackHandler = requireNonNull(fallbackHandler);
    return this;
  }

  @Override
  public void handleFailedException(@NotNull Operation operation, @NotNull Throwable exception) throws Throwable {
    findHandler(exception).handleFailedException(operation, exception);
  }

  @Override
  public void handleReadOperation(@NotNull ReadOperation<?> operation, @NotNull Throwable exception) throws Throwable {
    findHandler(exception).handleReadOperation(operation, exception);
  }

  @Override
  public void handleReadAllOperation(ReadOperation.@NotNull ReadAll<?> operation, @NotNull Throwable exception) throws Throwable {
    findHandler(exception).handleReadAllOperation(operation, exception);
  }

  @Override
  public @Nullable String listenModelException(@NotNull SavableModel model, @NotNull Throwable exception) throws Throwable {
    return findHandler(exception).listenModelException(model, exception);
  }

  private ExceptionHandler findHandler(Throwable throwable) {
    return exceptionHandlerMap.getOrDefault(throwable.getClass(), fallbackHandler);
  }
}