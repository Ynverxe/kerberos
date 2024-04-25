package com.github.ynverxe.kerberos.crud.repository.adapter.defaults;

import com.github.ynverxe.kerberos.crud.repository.adapter.DataMapper;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;

public class ByteArrayToStringMapper implements DataMapper<byte[], String> {
  @Override
  public @NotNull String adaptToNewData(byte @NotNull [] input) {
    return new String(input, StandardCharsets.UTF_8);
  }

  @Override
  public byte @NotNull [] adaptToBackingData(@NotNull String output) {
    return output.getBytes(StandardCharsets.UTF_8);
  }
}