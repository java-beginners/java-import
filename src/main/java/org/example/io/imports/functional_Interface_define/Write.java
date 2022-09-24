package org.example.io.imports.functional_Interface_define;

import java.util.List;

@FunctionalInterface
public interface Write {
    void write(List<Object> data, boolean endLineFlag) throws IllegalAccessException;
}
