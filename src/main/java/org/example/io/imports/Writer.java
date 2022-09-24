package org.example.io.imports;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Writer {
    private String tableName;
    private Boolean isDeleteBeforeImport;
    private JDBCConnection connection;
    private Integer batchSize = 10;

    public Writer(JDBCConnection connection, String tableName, Object model, Boolean isDeleteBeforeImport, Integer batchSize) {
        this.connection = connection;
        this.tableName = tableName;
        this.isDeleteBeforeImport = isDeleteBeforeImport;
        this.batchSize = batchSize;
    }

    private List<String> getAllFieldName(Class obj) {
        List<String> fieldNames = new ArrayList<>();
        Field[] fields = obj.getDeclaredFields();
        for (Field field : fields) {
            fieldNames.add(field.getName().toLowerCase());
        }
        ;
        return fieldNames;
    }

    private List<Object> getAllFieldValues(Object obj) throws IllegalAccessException {
        List<Object> fieldValues = new ArrayList<>();
        Field[] fields = obj.getClass().getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            fieldValues.add(field.get(obj));
        }
        ;
        return fieldValues;
    }

    public Integer deleteAll() {
        String qr = "DELETE from " + tableName;
        Integer res = 0;
        try {
            Connection conn = connection.getConnection();
            Statement stmt = conn.createStatement();
            res = stmt.executeUpdate(qr);
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    public Integer InsertMany(List<Object> objs) throws IllegalAccessException {
        int result = 0;

        try {
            Connection conn = connection.getConnection();
            conn.setAutoCommit(false);
            Statement stmt = conn.createStatement();

            if (!objs.isEmpty()) {
                if (isDeleteBeforeImport == true) {
                    int deletedRows = deleteAll();
                    System.out.println(deletedRows + " rows are deleted");
                }

                List<String> fieldNames = getAllFieldName(objs.get(0).getClass());
                for (int i = 0; i < objs.size(); i++) {
                    Object obj = objs.get(i);
                    String qr = "INSERT INTO " + tableName;
                    List<Object> fieldValues = getAllFieldValues(obj);
                    qr += "(" + String.join(",", fieldNames) + ") values ("
                            + String.join(",", buildInsertQueryValue(fieldValues))
                            + ")";
                    System.out.println(qr);
                    stmt.addBatch(qr);
                    if (i%batchSize==0){
                        int[]count = stmt.executeBatch();
                        result +=count.length;
                    }
                }
                int[]count = stmt.executeBatch();
                result += count.length;
                conn.commit();

                if (result > 0) {
                    System.out.println("successfully inserted");
                } else {
                    System.out.println("unsucessful insertion");
                }
                conn.close();


            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return result;
    }

    private List<String> buildInsertQueryValue(List<Object> values) {
        List<String> insertValues = new ArrayList<>();
        for (Object val : values) {
            switch (val.getClass().getSimpleName()) {
                case "String":
                case "string":
                case "LocalDateTime":
                    insertValues.add("'" + val + "'");
                    break;
                case "int":
                case "Integer":
                case "Long":
                case "long":
                case "Float":
                case "float":
                case "Double":
                case "double":
                    insertValues.add("" + val);
                    break;
                case "Boolean":
                case "boolean":
                    if ((boolean) val == true) {
                        insertValues.add("'1'");
                    } else {
                        insertValues.add("'0'");
                    }
                    break;
                default:
                    break;
            }
        }
        return insertValues;
    }

}
