package com.github.ynverxe.kerberos.crud.repository.adapter.defaults;

import com.github.ynverxe.kerberos.crud.repository.adapter.DataMapper;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.Map;

public class FieldMapToJsonMapper implements DataMapper<Map<String, Object>, JSONObject> {
  @Override
  public @NotNull JSONObject adaptToNewData(@NotNull Map<String, Object> input) {
    return new JSONObject(input);
  }

  @Override
  public @NotNull Map<String, Object> adaptToBackingData(@NotNull JSONObject output) {
    return output.toMap();
  }
}