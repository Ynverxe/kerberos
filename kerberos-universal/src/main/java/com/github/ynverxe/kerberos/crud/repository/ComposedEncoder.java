package com.github.ynverxe.kerberos.crud.repository;

import java.util.Collections;
import java.util.List;

public class ComposedEncoder<T> implements Encoder<T> {

  private final List<Encoder<T>> encoders;

  ComposedEncoder(List<Encoder<T>> encoders) {
    this.encoders = Collections.unmodifiableList(encoders);
  }

  @Override
  public T encode(T uncodedData) throws Throwable {
    T data = uncodedData;

    for (Encoder<T> encoder : encoders) {
      data = encoder.encode(data);
    }

    return data;
  }

  @Override
  public T decode(T codedData) throws Throwable {
    T data = codedData;

    for (Encoder<T> encoder : encoders) {
      data = encoder.decode(data);
    }

    return data;
  }
}