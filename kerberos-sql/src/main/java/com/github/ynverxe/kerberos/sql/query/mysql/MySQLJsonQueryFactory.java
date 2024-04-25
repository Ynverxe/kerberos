package com.github.ynverxe.kerberos.sql.query.mysql;

import com.github.ynverxe.kerberos.sql.query.ParameterizedQueryFactory;
import org.jetbrains.annotations.NotNull;

import static java.lang.String.format;

public class MySQLJsonQueryFactory implements ParameterizedQueryFactory {

  protected final @NotNull String tableId;
  protected final @NotNull String keyColumnId;
  protected final @NotNull String textPayloadColumnId;

  protected MySQLJsonQueryFactory(@NotNull String tableId, @NotNull String keyColumnId, @NotNull String textPayloadColumnId) {
    this.tableId = tableId;
    this.keyColumnId = keyColumnId;
    this.textPayloadColumnId = textPayloadColumnId;

    notEmpty(tableId, "tableId");
    notEmpty(keyColumnId, "keyColumnId");
    notEmpty(textPayloadColumnId, "textPayloadColumnId");
  }

  @Override
  public @NotNull String createTableCreationQuery() {
    return format("CREATE TABLE IF NOT EXISTS `%s`(`%s` VARCHAR(255) NOT NULL PRIMARY KEY, `%s` json NOT NULL)", tableId, keyColumnId, textPayloadColumnId);
  }

  @Override
  public @NotNull String createPayloadInsertionStatement() {
    return format("REPLACE INTO `%s` (%s, %s) VALUES(?, ?)", tableId, keyColumnId, textPayloadColumnId);
  }

  @Override
  public @NotNull String createDeletionStatement() {
    return format("DELETE FROM `%s` WHERE(`%s`=?)", tableId, keyColumnId);
  }

  @Override
  public @NotNull String createValueSelectQuery() {
    return format("SELECT `%s` FROM `%s` WHERE(`%s`=?)", textPayloadColumnId, tableId, keyColumnId);
  }

  @Override
  public @NotNull String createSelectAllRowsWithIdQuery() {
    return format("SELECT * FROM `%s` WHERE(`%s`=?)", tableId, keyColumnId);
  }

  @Override
  public @NotNull String createSelectAllRowsQuery() {
    return format("SELECT * FROM `%s`", tableId);
  }

  private static void notEmpty(String value, String fieldName) {
    if (value.isEmpty()) {
      throw new IllegalArgumentException(fieldName + " is empty.");
    }
  }

  public static MySQLJsonQueryFactory create(@NotNull String tableId, @NotNull String keyColumnId, @NotNull String textPayloadColumnId, boolean h2) {
    if (h2) {
      return new H2(tableId, keyColumnId, textPayloadColumnId);
    }

    return new MySQLJsonQueryFactory(tableId, keyColumnId, textPayloadColumnId);
  }

  static class H2 extends MySQLJsonQueryFactory {

    public H2(@NotNull String tableId, @NotNull String keyColumnId, @NotNull String textPayloadColumnId) {
      super(tableId, keyColumnId, textPayloadColumnId);
    }

    @Override
    public @NotNull String createPayloadInsertionStatement() {
      return format("REPLACE INTO `%s` (%s, %s) VALUES(?, ? FORMAT JSON)", tableId, keyColumnId, textPayloadColumnId);
    }
  }
}