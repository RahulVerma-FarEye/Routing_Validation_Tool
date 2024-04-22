package com.Backendcode.RoutingBackrndcode.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RouteData {

    private List<RouteSegments> routeSegments;
    private List<Route> routes;

    public List<RouteSegments> getRouteSegments() {
        return routeSegments;
    }

    public void setRouteSegments(List<RouteSegments> routeSegments) {
        this.routeSegments = routeSegments;
    }

    public List<Route> getRoutes() {

        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }

}
