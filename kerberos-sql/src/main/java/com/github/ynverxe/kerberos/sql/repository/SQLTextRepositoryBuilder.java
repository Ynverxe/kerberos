package com.github.ynverxe.kerberos.sql.repository;

import com.github.ynverxe.kerberos.crud.repository.AbstractCrudRepositoryBuilder;
import com.github.ynverxe.kerberos.sql.access.SQLAccessData;
import com.github.ynverxe.kerberos.sql.query.ParameterizedQueryFactory;
import com.github.ynverxe.kerberos.sql.source.ConnectionSource;
import com.github.ynverxe.kerberos.sql.jdbc.JdbcUrlProvider;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

public class SQLTextRepositoryBuilder<B extends SQLTextRepositoryBuilder<B>>
  extends AbstractCrudRepositoryBuilder<String, B> {

  private ParameterizedQueryFactory queryFactory;
  private Callable<Connection> connectionSupplier;
  private SQLAccessData accessData;

  private String jdbcUrl;
  private JdbcUrlProvider jdbcUrlProvider;

  @Contract("_ -> this")
  public B jdbcUrl(@NotNull String jdbcUrl) {
    this.jdbcUrl = jdbcUrl;
    return self();
  }

  @Contract("_ -> this")
  public B jdbcUrlProvider(@NotNull JdbcUrlProvider jdbcUrlProvider) {
    this.jdbcUrlProvider = jdbcUrlProvider;
    return self();
  }

  @Contract("_ -> this")
  public B queryFactory(@NotNull ParameterizedQueryFactory queryFactory) {
    this.queryFactory = queryFactory;
    return self();
  }

  @Contract("_ -> this")
  public B connectionSupplier(@NotNull Callable<Connection> connectionSupplier) {
    this.connectionSupplier = connectionSupplier;
    return self();
  }

  @Contract("_ -> this")
  public B connection(@NotNull Connection connection) {
    return this.connectionSupplier(() -> connection);
  }

  @Contract("_ -> this")
  public B accessData(@NotNull SQLAccessData accessData) {
    this.accessData = accessData;
    return self();
  }

  @Contract("_ -> this")
  public B accessData(@NotNull Supplier<SQLAccessData> accessDataSupplier) {
    this.accessData = accessDataSupplier.get();
    return self();
  }

  @Contract("_, _ -> this")
  public B loadCredential(@NotNull Path pathToProperties, boolean suppressFail) {
    requireNonNull(pathToProperties, "pathToProperties");
    try (InputStream inputStream = Files.newInputStream(pathToProperties)) {
      Properties properties = new Properties();
      properties.load(inputStream);
      return accessData(SQLAccessData.fromProperties(properties));
    } catch (IOException e) {
      if (suppressFail) return self();

      throw new RuntimeException(e);
    }
  }

  @Override
  public @NotNull SQLTextRepository build() throws Throwable {
    requireNonNull(queryFactory, "queryFactory");

    ConnectionSource connectionSource;
    if (connectionSupplier != null) {
      Connection connection = connectionSupplier.call();
      if (connection.isClosed()) {
        throw new IllegalStateException("Provided Connection is closed");
      }

      connectionSource = new ConnectionSource(connection);
    } else {
      String jdbcUrl = this.jdbcUrl;
      if (this.jdbcUrl == null) {
        if (jdbcUrlProvider == null) {
          throw new IllegalArgumentException("No way provided to get a valid jdbcUrl");
        }

        if (accessData != null) {
          jdbcUrl = jdbcUrlProvider.jdbcUrl(accessData);
        } else {
          jdbcUrl = jdbcUrlProvider.fallbackJdbcUrl();

          if (jdbcUrl == null) {
            throw new IllegalArgumentException("Missing access data or no fallback url provided");
          }
        }
      }

      connectionSource = connect(jdbcUrl, accessData);
    }

    return new SQLTextRepository(encoder, queryFactory, connectionSource);
  }

  @SuppressWarnings("unchecked")
  protected B self() {
    return (B) this;
  }

  protected ConnectionSource connect(@NotNull String jdbcUrl, @Nullable SQLAccessData accessData) throws SQLException {
    Connection connection;
    if (accessData != null) {
      connection = DriverManager.getConnection(jdbcUrl, accessData.username(), accessData.password());
    } else {
      connection = DriverManager.getConnection(jdbcUrl);
    }

    return new ConnectionSource(connection);
  }
}