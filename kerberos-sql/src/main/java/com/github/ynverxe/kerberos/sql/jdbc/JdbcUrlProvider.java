package com.github.ynverxe.kerberos.sql.jdbc;

import com.github.ynverxe.kerberos.sql.access.SQLAccessData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Used to provide a jdbcUrl ready to be used.
 */
public interface JdbcUrlProvider {

  @NotNull String jdbcUrl(@NotNull SQLAccessData accessData);

  default @Nullable String fallbackJdbcUrl() {
    return null;
  }

  static Formatter withFormat(@NotNull String jdbcUrlFormat) {
    return new Formatter() {
      @Override
      protected String format() {
        return jdbcUrlFormat;
      }
    };
  }

  abstract class Formatter implements JdbcUrlProvider {
    @Override
    public @NotNull String jdbcUrl(@NotNull SQLAccessData accessData) {
      return format()
        .replaceFirst("ip", accessData.ip())
        .replaceFirst("port", accessData.port())
        .replaceFirst("database", accessData.database())
        .replaceFirst("username", accessData.username())
        .replaceFirst("password", accessData.password());
    }

    protected abstract String format();

  }
}