package com.github.ynverxe.kerberos.crud.repository;

import java.util.Arrays;
import java.util.List;

/**
 * A class in charge of encode and decode {@link CrudRepository}
 * data. For example, maybe we want to use gzip compression
 * for a CrudRepository that works with byte arrays.
 *
 * @param <T> The data type to encode
 */
public interface Encoder<T> {

  T encode(T uncodedData) throws Throwable;

  T decode(T codedData) throws Throwable;

  static <T> Encoder<T> phantom() {
    return new PhantomEncoder<>();
  }

  @SafeVarargs
  static <T> Encoder<T> ofEncoders(Encoder<T>... encoders) {
    return new ComposedEncoder<>(Arrays.asList(encoders));
  }

  static <T> Encoder<T> ofEncoders(List<Encoder<T>> encoders) {
    return new ComposedEncoder<>(encoders);
  }
}