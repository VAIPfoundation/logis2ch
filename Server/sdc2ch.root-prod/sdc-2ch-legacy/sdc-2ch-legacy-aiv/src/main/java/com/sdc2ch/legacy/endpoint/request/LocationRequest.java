package com.sdc2ch.legacy.endpoint.request;

import java.text.DateFormat;
import java.util.List;

import com.google.gson.GsonBuilder;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocationRequest {
    private String name;
    private String version;
    private int id;
    private List<GpsData> datas;













}
