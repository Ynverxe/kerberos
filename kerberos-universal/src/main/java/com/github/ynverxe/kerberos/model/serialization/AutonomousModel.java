package com.github.ynverxe.kerberos.model.serialization;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a model that can serialize itself.
 *
 * @param <T> The type of data to which the model should be serialized.
 */
public interface AutonomousModel<T> {
  @NotNull T serialize() throws Throwable;
}