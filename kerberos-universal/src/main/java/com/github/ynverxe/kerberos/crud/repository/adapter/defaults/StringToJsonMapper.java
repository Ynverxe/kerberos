package com.github.ynverxe.kerberos.crud.repository.adapter.defaults;

import com.github.ynverxe.kerberos.crud.repository.adapter.DataMapper;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

public class StringToJsonMapper implements DataMapper<String, JSONObject> {
  @Override
  public @NotNull JSONObject adaptToNewData(@NotNull String input) {
    return new JSONObject(input);
  }

  @Override
  public @NotNull String adaptToBackingData(@NotNull JSONObject output) {
    return output.toString();
  }
}