package com.github.ynverxe.kerberos.sql.query;

import org.jetbrains.annotations.NotNull;

public interface ParameterizedQueryFactory {

  @NotNull String createTableCreationQuery();

  @NotNull String createPayloadInsertionStatement();

  @NotNull String createDeletionStatement();

  @NotNull String createValueSelectQuery();

  @NotNull String createSelectAllRowsWithIdQuery();

  @NotNull String createSelectAllRowsQuery();

}