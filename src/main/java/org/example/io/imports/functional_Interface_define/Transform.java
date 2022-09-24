package org.example.io.imports.functional_Interface_define;

import java.util.List;

@FunctionalInterface
public interface Transform {
    Object transform(List<String> lines) throws Exception;
}
