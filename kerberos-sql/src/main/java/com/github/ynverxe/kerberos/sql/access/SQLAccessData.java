package com.github.ynverxe.kerberos.sql.access;

import com.github.ynverxe.kerberos.sql.exception.InvalidSQLAccessDataException;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import static java.util.Objects.requireNonNull;

public class SQLAccessData implements Serializable {

  private final @NotNull String ip;
  private final @NotNull String port;
  private final @NotNull String database;
  private final @NotNull String username;
  private final @NotNull String password;

  public SQLAccessData(
    @NotNull String ip,
    @NotNull String port,
    @NotNull String database,
    @NotNull String username,
    @NotNull String password
  ) {
    this.ip = requireNonNull(ip, "ip");
    this.port = requireNonNull(port, "port");
    this.database = requireNonNull(database, "database");
    this.username = requireNonNull(username, "username");
    this.password = requireNonNull(password, "password");

    notEmpty(ip, "ip");
    notEmpty(port, "port");
    notEmpty(database, "database");
    notEmpty(username, "username");
  }

  public String ip() {
    return ip;
  }

  public String port() {
    return port;
  }

  public String database() {
    return database;
  }

  public String username() {
    return username;
  }

  public String password() {
    return password;
  }

  public Map<String, Object> toMap() {
    Map<String, Object> objectMap = new LinkedHashMap<>();
    objectMap.put("ip", ip);
    objectMap.put("port", port);
    objectMap.put("database", database);
    objectMap.put("username", username);
    objectMap.put("password", password);
    return objectMap;
  }

  public static @NotNull SQLAccessData fromMap(@NotNull Map<String, Object> objectMap) throws InvalidSQLAccessDataException {
    try {
      return new SQLAccessData(
        (String) objectMap.get("ip"),
        (String) objectMap.get("port"),
        (String) objectMap.get("database"),
        (String) objectMap.get("username"),
        (String) objectMap.get("password")
      );
    } catch (Throwable throwable) {
      throw new InvalidSQLAccessDataException(throwable);
    }
  }

  public static @NotNull SQLAccessData fromProperties(@NotNull Properties properties) throws InvalidSQLAccessDataException {
    Map<String, Object> objectMap = new LinkedHashMap<>();
    properties.forEach((key, value) -> objectMap.put(String.valueOf(key), value));
    return fromMap(objectMap);
  }

  private static void notEmpty(String s, String fieldName) {
    if (s.isEmpty()) {
      throw new IllegalArgumentException(fieldName + " is empty");
    }
  }
}