package com.example.simplepois.data.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;
import java.util.List;

public class PoiInfoList {

    public final List<PoiInfo> list;

    @JsonCreator
    public PoiInfoList(@JsonProperty("list") List<PoiInfo> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "PoiInfoList{" +
                "list=" + Arrays.toString(list.toArray()) +
                '}';
    }
}
