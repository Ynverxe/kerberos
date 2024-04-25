package com.github.ynverxe.kerberos.util;

import org.jetbrains.annotations.NotNull;

public interface Buildable<T extends Builder<?>> {

  @NotNull T toBuilder();

}
