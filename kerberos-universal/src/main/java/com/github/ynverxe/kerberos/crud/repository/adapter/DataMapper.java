package com.github.ynverxe.kerberos.crud.repository.adapter;

import org.jetbrains.annotations.NotNull;

public interface DataMapper<I, O> {

  @NotNull O adaptToNewData(@NotNull I input) throws Throwable;

  @NotNull I adaptToBackingData(@NotNull O output) throws Throwable;

}