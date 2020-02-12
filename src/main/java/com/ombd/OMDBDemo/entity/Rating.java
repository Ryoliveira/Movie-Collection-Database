package com.ombd.OMDBDemo.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Rating {

    @JsonProperty("Source")
    private String source;

    @JsonProperty("Value")
    private String value;

    public Rating() {
    }

    public Rating(String source, String value) {
        this.source = source;
        this.value = value;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Rating{" +
                "source='" + source + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
