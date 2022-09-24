package org.example.io.imports;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@interface MyLength {
    int val();
}

@Retention(RetentionPolicy.RUNTIME)
@interface MyScale {
    int val();
}

@Retention(RetentionPolicy.RUNTIME)
@interface MyFormat{
    String dateFormat() default "";
    int scale() default 0;
}
