package com.github.ynverxe.kerberos.crud.repository;

public class PhantomEncoder<T> implements Encoder<T> {
  @Override
  public T encode(T uncodedData) throws Throwable {
    return uncodedData;
  }

  @Override
  public T decode(T codedData) throws Throwable {
    return codedData;
  }
}