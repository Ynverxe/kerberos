package com.github.ynverxe.kerberos.util;

import org.jetbrains.annotations.NotNull;

public interface Builder<T> {

  @NotNull T build() throws Throwable;

}