package com.Backendcode.RoutingBackrndcode.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Route {


    private String routeName;
    private List<ResponseObject> responseObjectList;

    // Constructors, getters, and setters

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public List<ResponseObject> getResponseObjectList() {
        return responseObjectList;
    }

    public void setResponseObjectList(List<ResponseObject> responseObjectList) {
        this.responseObjectList = responseObjectList;
    }



}
