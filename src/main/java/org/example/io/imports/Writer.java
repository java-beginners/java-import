package org.example.io.imports;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;

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
    public Integer insert(Object obj){
        Connection conn = connection.getConnection();
        boolean result = false;
        try {
            List<String> fieldNames = getAllFieldName(obj.getClass());
            Statement stmt = conn.createStatement();
            String qr = "INSERT INTO " + tableName;
            List<Object> fieldValues = getAllFieldValues(obj);
            qr += "(" + String.join(",", fieldNames) + ") values ("
                    + String.join(",", buildInsertQueryValue(fieldValues))
                    + ")";
            result = stmt.execute(qr);
            if (result) {
                System.out.println("successfully inserted");
            } else {
                System.out.println("unsucessful insertion");
            }
            conn.close();

        } catch (SQLException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        return result ?1:0;
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
                    if (i % batchSize == 0) {
                        int[] count = stmt.executeBatch();
                        result += count.length;
                    }
                }
                int[] count = stmt.executeBatch();
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

    public Integer createTable(String name, Object model, String primaryKey) throws IllegalAccessException {
        Integer res = 0;
        String qr = "create table " + name + "( ";
        Field[] fs = model.getClass().getDeclaredFields();
        for (int i = 0; i < fs.length; i++) {
            String fn = fs[i].getName();
            String ft = fs[i].getType().getSimpleName();
            switch (ft) {
                case "String":
                case "string":
                    if (fs[i].isAnnotationPresent(MyLength.class)) {
                        qr += fn + " varchar(" + fs[i].getAnnotation(MyLength.class).val() + "),";
                    }else{
                        qr += fn +" varchar(max)";
                    }
                    ;
                    break;
                case "Integer":
                case "int":
                    qr += fn + " int,";
                    break;
                case "Float":
                case "float":
                    qr += fn + " float,";
                    break;
                case "double":
                case "Double":
                    qr += fn + " double,";
                    break;
                case "Date":
                case "LocalDate":
                    qr+=fn+" datetime,";
                    break;
                default:
                    break;
            }
        }
        if (primaryKey != null) {
            qr +="primary key(" + primaryKey+"))";
        }else{
            qr = qr.substring(0,qr.length()-1)+")";
        }
        try {
            Connection conn = connection.getConnection();
            Statement stmt = conn.createStatement();
            res = stmt.executeUpdate(qr);
            conn.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return res;
    }
}
