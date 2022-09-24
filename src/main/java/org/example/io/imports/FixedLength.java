package org.example.io.imports;

public class FixedLength {
    private String Format;
    private Integer Length;
    private Integer Scale;

    public FixedLength(String format, Integer length, Integer scale) {
        Format = format;
        Length = length;
        Scale = scale;
    }

    public FixedLength() {
    }

    public String getFormat() {
        return Format;
    }

    public void setFormat(String format) {
        Format = format;
    }

    public Integer getLength() {
        return Length;
    }

    public void setLength(Integer length) {
        Length = length;
    }

    public Integer getScale() {
        return Scale;
    }

    public void setScale(Integer scale) {
        Scale = scale;
    }
}
