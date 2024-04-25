package com.github.ynverxe.kerberos.sql.test;

import com.github.ynverxe.kerberos.sql.query.mysql.MySQLJsonQueryFactory;
import com.github.ynverxe.kerberos.sql.repository.SQLTextRepository;
import org.junit.jupiter.api.*;

import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SQLRepositoryTest {

  private static final SQLTextRepository REPOSITORY;

  static {
    try {
      REPOSITORY = SQLTextRepository.genericBuilder()
        .jdbcUrl("jdbc:h2:~/test;MODE=MySQL")
        .loadCredential(Paths.get("src/test/resources/mysql-test-credentials.properties"), false)
        .queryFactory(MySQLJsonQueryFactory.create("JSON_TABLE_TEST", "id", "jsonValue", true))
        .build();
    } catch (Throwable e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  @Order(0)
  public void testRead() throws Throwable {
    String json = "{\"name\":\"ynverxe\"}";
    REPOSITORY.save("jsonTest", json);
    String savedValue = REPOSITORY.read("jsonTest");
    assertNotNull(savedValue);
    assertEquals(json, savedValue);
  }

  @Test
  @Order(1)
  public void testDeletion() {
    assertTrue(REPOSITORY.delete("jsonTest"));
  }

  @Test
  @Order(2)
  public void testFindAll() throws Throwable {
    String firstJson = "{}";
    String secondJson = "{\"name\":\"ynverxe\"}";

    REPOSITORY.save("firstJson", firstJson);
    REPOSITORY.save("secondJson", secondJson);

    Map<String, String> model = new LinkedHashMap<>();
    model.put("firstJson", firstJson);
    model.put("secondJson", secondJson);

    assertEquals(model, REPOSITORY.readAll());
  }

  @AfterAll
  public static void clear() throws Throwable {
    dropTestTable();
  }

  private static void dropTestTable() throws Throwable {
    Connection connection = REPOSITORY.connection();
    Statement statement = connection.createStatement();

    statement.execute("DROP TABLE IF EXISTS JSON_TABLE_TEST");
    statement.close();
  }
}