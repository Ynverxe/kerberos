package com.github.ynverxe.kerberos.test.serialization;

import com.github.ynverxe.kerberos.ModelStorage;
import com.github.ynverxe.kerberos.model.repository.ModelRepository;
import com.github.ynverxe.kerberos.model.serialization.ReflectiveModelDeserializer;
import com.github.ynverxe.kerberos.test.common.InMemoryCrudRepository;
import com.github.ynverxe.kerberos.test.serialization.model.Credential;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ReflectiveSerializationTest {

  @Test
  public void testReflectiveSerialization() throws Throwable {
    ModelStorage<Map<String, Object>, Credential> storage = ModelStorage.<Map<String, Object>, Credential>builder()
      .crudRepository(new InMemoryCrudRepository<>())
      .modelSerializer(ReflectiveModelDeserializer.modelSerializer(Credential.class))
      .build();

    // Just to pretend that I put more effort into it
    PasswordGenerator generator = new PasswordGenerator.PasswordGeneratorBuilder()
      .useDigits(true)
      .useLower(true)
      .useUpper(true)
      .build();

    Credential original = new Credential("Ynverxe", generator.generate(16));
    System.out.println("Password: " + original.password());

    ModelRepository<Credential> credentialRepository = storage.modelRepository();
    credentialRepository.saveModel(original);

    assertEquals(original, credentialRepository.findModel("Ynverxe"));
  }
}