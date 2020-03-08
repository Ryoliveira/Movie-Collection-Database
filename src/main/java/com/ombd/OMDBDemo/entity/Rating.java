package com.ombd.OMDBDemo.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Rating {

    @JsonProperty("Source")
    private String source;

    @JsonProperty("Value")
    private String value;

}
