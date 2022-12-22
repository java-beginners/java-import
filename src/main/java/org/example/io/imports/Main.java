package org.example.io.imports;

import org.example.io.imports.functional_Interface_define.BuildFileName;
import org.example.io.imports.functional_Interface_define.Read;
import org.example.io.imports.functional_Interface_define.Transform;

public class Main {

    public static void main(String[] args) throws Exception {
        final String DB_URL = "jdbc:mysql://b3xlz3mvmxmvbovqthbz-mysql.services.clever-cloud.com:3306/b3xlz3mvmxmvbovqthbz?allowMultiQueries=true";
        final String USER_NAME = "uoavvvqbdbvw8xhg";
        final String PASSWORD = "7oC90oma1uYdqBH4Fhug";

        DelimiterFormatter formatter = new DelimiterFormatter(new User());
//        FixedLengthFormatter formatter = new FixedLengthFormatter(new User());
        BuildFileName buildFileName = () -> {
            String fileName = "TEST.csv";
            return String.join("/", "src", "main", "export", fileName);
        };
        Transform transform = formatter::toStruct;

        FileReader reader = new FileReader(buildFileName, ",");
        Read read = reader::ReadDelimiterFile;
//        Read read = reader::Read;
        JDBCConnection conn = new JDBCConnection(DB_URL, USER_NAME, PASSWORD);
        Writer writer = new Writer(conn, "usersimport", new User(), false, 2);
        Importer importer = new Importer(transform,
                (data, endLineFlag) -> {
                    if (data != null && endLineFlag!=true) {
                        writer.insert(data);
                    }
                }
                , read);

        importer.importExecute();
    }
}