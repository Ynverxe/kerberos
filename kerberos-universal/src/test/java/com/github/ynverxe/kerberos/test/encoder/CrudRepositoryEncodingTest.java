package com.github.ynverxe.kerberos.test.encoder;

import com.github.ynverxe.kerberos.crud.repository.CrudRepository;
import com.github.ynverxe.kerberos.crud.repository.adapter.defaults.ByteArrayToStringMapper;
import com.github.ynverxe.kerberos.test.common.InMemoryCrudRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CrudRepositoryEncodingTest {

  @Test
  public void testEncoding() throws Throwable {
    String key = "simpleKey";
    String original = "UncompressedString";

    CrudRepository<String> repository = new InMemoryCrudRepository<byte[]>()
      .passThisToBuilder(CrudRepository::delegatedRepositoryBuilder)
      .encoder(GZIPEncoder.INSTANCE)
      .build(CrudRepository::<byte[], String>adaptedRepositoryBuilder)
      .mapper(new ByteArrayToStringMapper())
      .build();

    repository.save(key, original);

    String read = repository.read(key);
    assertEquals(original, read);
  }
}