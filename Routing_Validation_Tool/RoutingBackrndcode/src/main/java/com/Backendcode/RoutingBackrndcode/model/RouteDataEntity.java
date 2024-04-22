package com.Backendcode.RoutingBackrndcode.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class RouteDataEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String routeName;
    private int intersectionCount;
    private double area;
    private double averageArea;
    private int overlapCount;
    private int totalRoutes;
    private int orderId;


    public RouteDataEntity() {
    }

    public RouteDataEntity(Long id, String routeName, int intersectionCount, double area, double averageArea, int overlapCount, int totalRoutes, int orderId) {
        this.id = id;
        this.routeName = routeName;
        this.intersectionCount = intersectionCount;
        this.area = area;
        this.averageArea = averageArea;
        this.overlapCount = overlapCount;
        this.totalRoutes = totalRoutes;
        this.orderId = orderId;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public int getIntersectionCount() {
        return intersectionCount;
    }

    public void setIntersectionCount(int intersectionCount) {
        this.intersectionCount = intersectionCount;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public double getAverageArea() {
        return averageArea;
    }

    public void setAverageArea(double averageArea) {
        this.averageArea = averageArea;
    }

    public int getOverlapCount() {
        return overlapCount;
    }

    public void setOverlapCount(int overlapCount) {
        this.overlapCount = overlapCount;
    }

    public int getTotalRoutes() {
        return totalRoutes;
    }

    public void setTotalRoutes(int totalRoutes) {
        this.totalRoutes = totalRoutes;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }


}
