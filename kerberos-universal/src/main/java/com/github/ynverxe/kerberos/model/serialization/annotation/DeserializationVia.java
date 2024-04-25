package com.github.ynverxe.kerberos.model.serialization.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public interface DeserializationVia {

  @Target(ElementType.TYPE)
  @Retention(RetentionPolicy.RUNTIME)
  @interface Method {
    /**
     * The data type that the referenced method accepts.
     */
    Class<?> argumentType();

    /**
     * The method name.
     */
    String name();
  }

  @Target(ElementType.TYPE)
  @Retention(RetentionPolicy.RUNTIME)
  @interface Constructor {
    /**
     * The data type that the referenced constructor accepts.
     */
    Class<?> argumentType();
  }
}