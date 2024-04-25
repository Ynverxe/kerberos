package com.github.ynverxe.kerberos.test.encoder;

import com.github.ynverxe.kerberos.crud.repository.Encoder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GZIPEncoder implements Encoder<byte[]> {

  public static final GZIPEncoder INSTANCE = new GZIPEncoder();

  private GZIPEncoder() {
  }

  @Override
  public byte[] encode(byte[] uncodedData) throws Throwable {
    ByteArrayOutputStream stream = new ByteArrayOutputStream(uncodedData.length);
    try (GZIPOutputStream gzipStream = new GZIPOutputStream(stream)) {
      gzipStream.write(uncodedData);
    }

    return stream.toByteArray();
  }

  @Override
  public byte[] decode(byte[] codedData) throws Throwable {
    ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();

    ByteArrayInputStream stream = new ByteArrayInputStream(codedData);
    try (InputStream gzipStream = new GZIPInputStream(stream)) {
      int read;
      while ((read = gzipStream.read()) != -1) {
        arrayOutputStream.write(read);
      }
    }

    return arrayOutputStream.toByteArray();
  }
}