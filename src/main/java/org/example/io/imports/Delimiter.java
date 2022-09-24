package org.example.io.imports;

public class Delimiter {
    String format;
    Integer scale;

    public Delimiter(String format, Integer scale) {
        this.format = format;
        this.scale = scale;
    }

    public Delimiter() {
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public Integer getScale() {
        return scale;
    }

    public void setScale(Integer scale) {
        this.scale = scale;
    }
}

