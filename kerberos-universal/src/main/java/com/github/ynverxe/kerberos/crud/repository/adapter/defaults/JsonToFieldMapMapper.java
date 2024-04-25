package com.github.ynverxe.kerberos.crud.repository.adapter.defaults;

import com.github.ynverxe.kerberos.crud.repository.adapter.DataMapper;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.Map;

public class JsonToFieldMapMapper implements DataMapper<JSONObject, Map<String, Object>> {
  @Override
  public @NotNull Map<String, Object> adaptToNewData(@NotNull JSONObject input) {
    return input.toMap();
  }

  @Override
  public @NotNull JSONObject adaptToBackingData(@NotNull Map<String, Object> output) {
    return new JSONObject(output);
  }
}