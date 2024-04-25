package com.github.ynverxe.kerberos.test.serialization.model;

import com.github.ynverxe.kerberos.model.repository.SavableModel;
import com.github.ynverxe.kerberos.model.serialization.AutonomousModel;
import com.github.ynverxe.kerberos.model.serialization.annotation.DeserializationVia;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

@DeserializationVia.Constructor(argumentType = Map.class)
public class Credential implements SavableModel, AutonomousModel<Map<String, Object>> {

  private final String username;
  private final String password;

  private Credential(Map<String, Object> map) {
    this.username = (String) map.get("username");
    this.password = (String) map.get("password");
  }

  public Credential(String username, String password) {
    this.username = username;
    this.password = password;
  }

  @Override
  public @NotNull String id() {
    return username;
  }

  public String password() {
    return password;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Credential that = (Credential) o;
    return Objects.equals(username, that.username) && Objects.equals(password, that.password);
  }

  @Override
  public int hashCode() {
    return Objects.hash(username, password);
  }

  @Override
  public @NotNull Map<String, Object> serialize() throws Throwable {
    Map<String, Object> objectMap = new LinkedHashMap<>();
    objectMap.put("username", username);
    objectMap.put("password", password);
    return objectMap;
  }
}