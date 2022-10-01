package org.example.io.imports;

import org.example.io.imports.functional_Interface_define.BuildFileName;
import org.example.io.imports.functional_Interface_define.Read;
import org.example.io.imports.functional_Interface_define.Transform;

public class Main {

    public static void main(String[] args) throws Exception {
        final String DB_URL = "jdbc:mysql://sql6.freesqldatabase.com:3306/sql6520711?allowMultiQueries=true";
        final String USER_NAME = "sql6520711";
        final String PASSWORD = "WvGUYNPsjy";

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
        Writer writer = new Writer(conn, "users", new User(), true, 2 );
        Importer importer = new Importer(transform,
                (data, endLineFlag) -> {
                    if (endLineFlag) {
                        int res = writer.InsertMany(data);
                        System.out.println(res + " rows affected");
                    }
                }
                , read);

        importer.importExecute();
    }
}