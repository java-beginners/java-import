package org.example.io.imports;

import org.example.io.imports.functional_Interface_define.Read;
import org.example.io.imports.functional_Interface_define.Transform;
import org.example.io.imports.functional_Interface_define.Write;

import java.util.ArrayList;
import java.util.List;

public class Importer {
    private final Read read;
    private final Transform transform;
    private final Write write;

    public Importer(Transform transform, Write write, Read read) {
        this.transform = transform;
        this.read = read;
        this.write = write;
    }

    public void importExecute() throws Exception {
//        List<Object> itemStructs = new ArrayList<>();

        read.read((lines, isEndRead) -> {
//            if (isEndRead != null) {
//                write.write(itemStructs, true);
//                return;
//            }
            Object itemStruct = transform.transform(lines);
//            itemStructs.add(itemStruct);
            write.write(itemStruct, false);
        });
    }
}
