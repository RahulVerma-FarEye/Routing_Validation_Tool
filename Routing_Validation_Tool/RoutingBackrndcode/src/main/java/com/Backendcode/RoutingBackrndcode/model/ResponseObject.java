package com.Backendcode.RoutingBackrndcode.model;



public class ResponseObject {

String orderId;
String arrTime;
String endTime;
Double travellingDistance;
Double travellingTime;
Float serviceTime;
Double latitude;
Double longitude;
String jobType;




    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getArrTime() {
        return arrTime;
    }

    public void setArrTime(String arrTime) {
        this.arrTime = arrTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Double getTravellingDistance() {
        return travellingDistance;
    }

    public void setTravellingDistance(Double travellingDistance) {
        this.travellingDistance = travellingDistance;
    }

    public Double getTravellingTime() {
        return travellingTime;
    }

    public void setTravellingTime(Double travellingTime) {
        this.travellingTime = travellingTime;
    }

    public Float getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(Float serviceTime) {
        this.serviceTime = serviceTime;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }



}
