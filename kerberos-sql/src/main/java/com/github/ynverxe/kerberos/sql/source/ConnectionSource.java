package com.github.ynverxe.kerberos.sql.source;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Objects;

public final class ConnectionSource {

  private final @NotNull Connection connection;
  private final @Nullable Object connectionProvider;

  public ConnectionSource(@NotNull Connection connection, @Nullable DataSource connectionProvider) {
    this.connection = Objects.requireNonNull(connection, "connection");
    this.connectionProvider = connectionProvider;
  }

  public ConnectionSource(@NotNull Connection connection) {
    this(connection, null);
  }

  public @NotNull Connection connection() {
    return connection;
  }

  @SuppressWarnings("unchecked")
  public <T> @Nullable T connectionProvider() {
    return (T) connectionProvider;
  }
}