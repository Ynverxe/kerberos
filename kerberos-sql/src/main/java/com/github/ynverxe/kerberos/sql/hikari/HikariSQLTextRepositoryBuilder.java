package com.github.ynverxe.kerberos.sql.hikari;

import com.github.ynverxe.kerberos.sql.access.SQLAccessData;
import com.github.ynverxe.kerberos.sql.source.ConnectionSource;
import com.github.ynverxe.kerberos.sql.repository.SQLTextRepositoryBuilder;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

public class HikariSQLTextRepositoryBuilder extends SQLTextRepositoryBuilder<HikariSQLTextRepositoryBuilder> {
  private Supplier<HikariConfig> hikariConfigSupplier = HikariConfig::new;

  @Contract("_ -> this")
  public HikariSQLTextRepositoryBuilder hikariConfigSupplier(@NotNull Supplier<HikariConfig> hikariConfigSupplier) {
    this.hikariConfigSupplier = requireNonNull(hikariConfigSupplier, "hikariConfigSupplier");
    return this;
  }

  @Contract("_ -> this")
  public HikariSQLTextRepositoryBuilder hikariConfig(@NotNull HikariConfig hikariConfig) {
    requireNonNull(hikariConfig, "hikariConfig");
    return hikariConfigSupplier(() -> hikariConfig);
  }

  @Contract("_ -> this")
  public HikariSQLTextRepositoryBuilder configureConfig(@NotNull Consumer<HikariConfig> hikariConfigConsumer) {
    requireNonNull(hikariConfigConsumer, "hikariConfigConsumer");
    return hikariConfigSupplier(() -> {
      HikariConfig config = hikariConfigSupplier.get();
      hikariConfigConsumer.accept(config);
      return config;
    });
  }

  @Contract("_, _ -> this")
  public HikariSQLTextRepositoryBuilder loadHikariConfig(@NotNull Path pathToProperties, boolean suppressFail) {
    requireNonNull(pathToProperties, "pathToProperties");
    try (InputStream inputStream = Files.newInputStream(pathToProperties)) {
      Properties properties = new Properties();
      properties.load(inputStream);
      return hikariConfig(new HikariConfig(properties));
    } catch (IOException e) {
      if (suppressFail) return this;

      throw new RuntimeException(e);
    }
  }

  @Override
  protected ConnectionSource connect(@NotNull String jdbcUrl, @Nullable SQLAccessData accessData) throws SQLException {
    HikariConfig config = hikariConfigSupplier.get();
    if (accessData != null) {
      config.setUsername(accessData.username());
      config.setPassword(accessData.password());
    }

    config.setJdbcUrl(jdbcUrl);

    HikariDataSource hikariDataSource = new HikariDataSource(config);
    return new ConnectionSource(hikariDataSource.getConnection(), hikariDataSource);
  }
}