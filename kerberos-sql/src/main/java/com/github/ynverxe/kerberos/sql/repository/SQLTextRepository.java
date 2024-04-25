package com.github.ynverxe.kerberos.sql.repository;

import com.github.ynverxe.kerberos.crud.repository.AbstractCrudRepository;
import com.github.ynverxe.kerberos.crud.repository.BridgeRepository;
import com.github.ynverxe.kerberos.crud.repository.Encoder;
import com.github.ynverxe.kerberos.sql.hikari.HikariSQLTextRepositoryBuilder;
import com.github.ynverxe.kerberos.sql.query.ParameterizedQueryFactory;
import com.github.ynverxe.kerberos.sql.source.ConnectionSource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

import static java.util.Objects.requireNonNull;

public class SQLTextRepository extends AbstractCrudRepository<String> implements BridgeRepository<String> {

  private final @NotNull ParameterizedQueryFactory queryFactory;
  private final @NotNull ConnectionSource connectionSource;

  public SQLTextRepository(@NotNull Encoder<String> encoder, @NotNull ParameterizedQueryFactory queryFactory, @NotNull ConnectionSource connectionSource) {
    super(encoder);
    this.queryFactory = requireNonNull(queryFactory, "queryFactory");
    this.connectionSource = requireNonNull(connectionSource, "connectionSource");

    executeUpdate(queryFactory.createTableCreationQuery());
  }

  @Override
  public void performSave(@NotNull String key, @NotNull String data) {
    String insertQuery = queryFactory.createPayloadInsertionStatement();
    executeUpdate(insertQuery, key, data);
  }

  @Override
  public @Nullable String performRead(@NotNull String key) {
    String selectQuery = queryFactory.createValueSelectQuery();

    return executeQuery(selectQuery, resultSet -> {
      if (resultSet.next()) {
        return resultSet.getString(1);
      }

      throw new IllegalStateException("ResultSet is empty");
    }, key);
  }

  @Override
  public @NotNull Map<String, String> performReadAll() {
    String selectAllQuery = queryFactory.createSelectAllRowsQuery();

    return executeQuery(selectAllQuery, this::mapSetToMap);
  }

  @Override
  public @NotNull Map<String, String> performReadAllWithKey(@NotNull String key) {
    String selectAllQuery = queryFactory.createSelectAllRowsWithIdQuery();

    return executeQuery(selectAllQuery, this::mapSetToMap, key);
  }

  @Override
  public boolean delete(@NotNull String key) {
    String insertQuery = queryFactory.createDeletionStatement();
    return executeUpdate(insertQuery, key) != 0;
  }

  @Override
  public boolean isClosed() {
    try {
      return connection().isClosed();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void close() throws Exception {
    connection().close();
  }

  public @NotNull Connection connection() {
    return connectionSource.connection();
  }

  public @NotNull ConnectionSource connectionSource() {
    return connectionSource;
  }

  public @NotNull ParameterizedQueryFactory queryFactory() {
    return queryFactory;
  }

  private Map<String, String> mapSetToMap(ResultSet resultSet) throws SQLException {
    Map<String, String> map = new LinkedHashMap<>();
    while (resultSet.next()) {
      String key = resultSet.getString(1);
      String value = resultSet.getString(2);

      map.put(key, value);
    }
    return map;
  }

  private int executeUpdate(String query, Object... parameters) {
    try (PreparedStatement statement = connection().prepareStatement(query)) {
      for (int i = 1; i <= parameters.length; i++) {
        statement.setObject(i, parameters[i - 1]);
      }
      return statement.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  private <T> T executeQuery(String query, ResultSetMapper<T> mapper, Object... parameters) {
    try (PreparedStatement statement = connection().prepareStatement(query)) {
      for (int i = 1; i <= parameters.length; i++) {
        statement.setObject(i, parameters[i - 1]);
      }
      return mapper.mapResultSet(statement.executeQuery());
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  interface ResultSetMapper<T> {
    T mapResultSet(ResultSet resultSet) throws SQLException;
  }

  public static SQLTextRepositoryBuilder<?> genericBuilder() {
    return new SQLTextRepositoryBuilder<>();
  }

  public static HikariSQLTextRepositoryBuilder hikariBuilder() {
    return new HikariSQLTextRepositoryBuilder();
  }
}