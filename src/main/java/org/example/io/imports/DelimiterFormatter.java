package org.example.io.imports;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DelimiterFormatter {
    private final String dateLayout = "yyyy-MM-dd HH:mm:ss";
    private Object model;
    private Map<Integer, FixedLength> formatcols;

    private Map<Integer, FixedLength> getIndexes(Object model
//            ,Class tagValue
    ) {
        Map<Integer, FixedLength> mp = new HashMap<Integer, FixedLength>();
        for (int i = 0; i < model.getClass().getDeclaredFields().length; i++) {
            Field field = model.getClass().getDeclaredFields()[i];
//            if(field.isAnnotationPresent(tagValue)){
//                 String c  =  field.getAnnotation(tagValue).toString();
//            }
            FixedLength fixedLengthVal = new FixedLength();
            if (field.isAnnotationPresent(MyLength.class)) {
                int length = field.getAnnotation(MyLength.class).val();
                if (length < 0) {
                    throw new RuntimeException();
                }
                fixedLengthVal.setLength(length);
            }
            if (field.isAnnotationPresent(MyScale.class)) {
                int scale = field.getAnnotation(MyScale.class).val();
                fixedLengthVal.setScale(scale);

            }
            if (field.isAnnotationPresent(MyFormat.class)) {
                MyFormat formatValue = field.getAnnotation(MyFormat.class);
                if (formatValue.dateFormat() != "") {
                    fixedLengthVal.setFormat(formatValue.dateFormat());
                } else if (fixedLengthVal.getScale() == null && formatValue.scale() != 0) {
                    fixedLengthVal.setFormat("" + formatValue.scale());
                    fixedLengthVal.setScale(formatValue.scale());
                }
            }
            mp.put(i, fixedLengthVal);
        }
        return mp;
    }

    public DelimiterFormatter(Object model) {
        Map<Integer, FixedLength> formatcols = getIndexes(model
//                ,MyFormat.class
        );
        this.model = model;
        this.formatcols = formatcols;
    }

    public Object toStruct(List<String> lines) throws Exception {
        Object record = Class.forName(model.getClass().getName()).newInstance();
        scanLine(lines, model, record, formatcols);
        return record;
    }

    public void scanLine(List<String> lines, Object model, Object record, Map<Integer, FixedLength> formatCols) throws Exception {
        Field[] modelFields = model.getClass().getDeclaredFields();
        for (int i = 0; i < modelFields.length; i++) {
            String line = lines.get(i);
            Field field = modelFields[i];
            FixedLength format = formatCols.get(i);
            field.setAccessible(true);
            String fieldType = field.getType().getSimpleName();
            switch (fieldType) {
                case "String":
                case "string":
                    field.set(record, line);
                    break;
                case "Integer":
                case "int":
                    field.setInt(record, Integer.parseInt(line));
                    break;
                case "Long":
                case "long":
                    field.setLong(record, Long.parseLong(line));
                    break;
                case "boolean":
                case "Boolean":
                    field.setBoolean(record, Boolean.parseBoolean(line));
                    break;
                case "float":
                case "Float":
                    field.setFloat(record, Float.parseFloat(line));
                    break;
                case "double":
                case "Double":
                    field.setDouble(record, Double.parseDouble(line));
                    break;
                case "LocalDateTime":
                    LocalDateTime dateField;
                    if (format.getFormat().length() > 0) {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format.getFormat());

                        dateField = LocalDateTime.parse(line,formatter);
                    } else {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateLayout);
                        dateField = LocalDateTime.parse(line,formatter);
                    }
                    field.set(record, dateField);
                    break;

            }
        }
    }

}
