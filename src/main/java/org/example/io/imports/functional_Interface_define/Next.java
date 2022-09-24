package org.example.io.imports.functional_Interface_define;

import java.util.List;

@FunctionalInterface
public interface Next{
    void next(List<String> lines, Boolean isReadEnd) throws Exception;
}


