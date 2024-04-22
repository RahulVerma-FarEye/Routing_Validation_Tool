package com.Backendcode.RoutingBackrndcode.Service;

import com.Backendcode.RoutingBackrndcode.Repository.RouteDataRepository;
import com.Backendcode.RoutingBackrndcode.model.ResponseObject;
import com.Backendcode.RoutingBackrndcode.model.Route;
import com.Backendcode.RoutingBackrndcode.model.RouteDataEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.*;
import java.util.List;



@Service
public class Routeservice {

    @Autowired
    private RouteDataRepository routeDataRepository;

    public Map<String, Object> RouteValidationResults(List<Route> routeList) {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> routeResults = new ArrayList<>();

        int totalOrderIDCount = calculateTotalOrderIDCount(routeList);

        // Calculate bounding box area for all routes
        double boundingBoxArea = calculateBoundingBoxAreaForRoutes(routeList);

        // Calculate total density
        double totalDensity = boundingBoxArea / totalOrderIDCount;



        for (int i = 0; i < routeList.size(); i++) { // <-- Loop variable 'i' declared here
            Map<String, Object> routeResult = new HashMap<>();
            Route route = routeList.get(i);
            List<ResponseObject> segments = route.getResponseObjectList();

            // Calculate unique order count
            int uniqueOrderCount = countUniqueOrderIds(segments);
            routeResult.put("orderIDCount", uniqueOrderCount);

            // Calculate average distance
            double averageDistance = calculateAverageDistance(segments);
            routeResult.put("averageDistance", averageDistance);
            // Calculate order density

            double orderDensity = calculateAreaDensity(segments);
            routeResult.put("AreaDensity", orderDensity);

            // Calculate intersection count
            int intersectionCount = countIntersections(segments);
            routeResult.put("intersectionCount", intersectionCount);

            // Add overlap count for the current route
            int overlapCount = countRouteOverlaps(routeList.get(i), routeList);
            routeResult.put("overlapCount", overlapCount);

            routeResult.put("routeName", route.getRouteName());

            RouteDataEntity routeDataEntity = new RouteDataEntity();
            routeDataEntity.setRouteName(route.getRouteName());
            routeDataEntity.setIntersectionCount(intersectionCount);
            routeDataEntity.setAverageArea(averageDistance);
            routeDataEntity.setOverlapCount(overlapCount);
            routeDataEntity.setTotalRoutes(routeList.size());
            routeDataEntity.setOrderId(uniqueOrderCount);

            // Save the RouteDataEntity object using RouteService
            routeDataRepository.save(routeDataEntity);


            routeResults.add(routeResult);
        }


        result.put("routes", routeResults);
        result.put("totalRouteCount", routeList.size());
        result.put("totalDensity", totalDensity);
        result.put("success", true);

        return result;
    }




    // Calculate bounding box area for all routes combined
    private double calculateBoundingBoxAreaForRoutes(List<Route> routeList) {
        double minLat = Double.MAX_VALUE;
        double maxLat = Double.MIN_VALUE;
        double minLon = Double.MAX_VALUE;
        double maxLon = Double.MIN_VALUE;

        // Find the minimum and maximum latitude and longitude among all segments in all routes
        for (Route route : routeList) {
            List<ResponseObject> segments = route.getResponseObjectList();
            for (ResponseObject segment : segments) {
                Double lat = segment.getLatitude();
                Double lon = segment.getLongitude();
                if (lat != null && lon != null) {
                    if (lat < minLat) minLat = lat;
                    if (lat > maxLat) maxLat = lat;
                    if (lon < minLon) minLon = lon;
                    if (lon > maxLon) maxLon = lon;
                }
            }
        }

        return calculateBoundingBoxArea(minLat, maxLat, minLon, maxLon);
    }


    private int calculateTotalOrderIDCount(List<Route> routeList) {
        int totalOrderIDCount = 0;

        for (Route route : routeList) {
            List<ResponseObject> segments = route.getResponseObjectList();
            totalOrderIDCount += countTotalOrderIds(segments);
        }

        return totalOrderIDCount;
    }





    private int countRouteOverlaps(Route currentRoute, List<Route> routeList) {
        int overlapCount = 0;
        List<ResponseObject> currentSegments = currentRoute.getResponseObjectList();

        // Iterate through each other route
        for (int i = 0; i < routeList.size(); i++) {
            if (!currentRoute.equals(routeList.get(i))) {
                Route otherRoute = routeList.get(i);
                List<ResponseObject> otherSegments = otherRoute.getResponseObjectList();

                // Check for overlap between current route and other route
                boolean overlapFound = checkForOverlap(currentSegments, otherSegments);
                if (overlapFound) {
                    overlapCount++;
                }
            }
        }

        return overlapCount;
    }





    private boolean checkForOverlap(List<ResponseObject> segments1, List<ResponseObject> segments2) {
        // Iterate through each segment of route1 and check for overlaps with route2
        for (ResponseObject segment1Start : segments1.subList(1, segments1.size() - 1)) {
            for (ResponseObject segment1End : segments1.subList(1, segments1.size() - 1)) {
                for (ResponseObject segment2Start : segments2.subList(1, segments2.size() - 1)) {
                    for (ResponseObject segment2End : segments2.subList(1, segments2.size() - 1)) {
                        // Check if the two segments intersect
                        if (doSegmentsIntersect(segment1Start, segment1End, segment2Start, segment2End)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }




    //Function to count intersections in a single route
private int countIntersections(List<ResponseObject> segments) {
    int intersectionCount = 0;

    // Iterate over each pair of segments
    for (int i = 0; i < segments.size() - 1; i++) {
        ResponseObject segment1Start = segments.get(i);
        ResponseObject segment1End = segments.get(i + 1);

        for (int j = i + 2; j < segments.size() - 1; j++) {
            ResponseObject segment2Start = segments.get(j);
            ResponseObject segment2End = segments.get(j + 1);
            // Check if the two segments intersect
            if (doSegmentsIntersect(segment1Start, segment1End, segment2Start, segment2End)) {
                intersectionCount++;
            }
        }
    }

    return intersectionCount;
}



// Function to check if two segments intersect
    private boolean doSegmentsIntersect(ResponseObject p1, ResponseObject q1, ResponseObject p2, ResponseObject q2) {
        if (p1.getLatitude() == null || p1.getLongitude() == null ||
                q1.getLatitude() == null || q1.getLongitude() == null ||
                p2.getLatitude() == null || p2.getLongitude() == null ||
                q2.getLatitude() == null || q2.getLongitude() == null) {
            // If any of the values is null, return false as segments cannot intersect
            return false;
        }

        int o1 = orientation(p1, q1, p2);
        int o2 = orientation(p1, q1, q2);
        int o3 = orientation(p2, q2, p1);
        int o4 = orientation(p2, q2, q1);

        if (o1 != o2 && o3 != o4) {
            return true;
        }

        if (o1 == 0 && onSegment(p1, p2, q1)) {
            return true;
        }

        if (o2 == 0 && onSegment(p1, q2, q1)) {
            return true;
        }

        if (o3 == 0 && onSegment(p2, p1, q2)) {
            return true;
        }

        return o4 == 0 && onSegment(p2, q1, q2);
    }



    private int orientation(ResponseObject p, ResponseObject q, ResponseObject r) {
        double val = (q.getLongitude() - p.getLongitude()) * (r.getLatitude() - q.getLatitude()) -
                (q.getLatitude() - p.getLatitude()) * (r.getLongitude() - q.getLongitude());

        if (val == 0) {
            return 0;
        }

        return (val > 0) ? 1 : 2;
    }



    private boolean onSegment(ResponseObject p, ResponseObject q, ResponseObject r) {
        if (q.getLatitude() <= Math.max(p.getLatitude(), r.getLatitude()) &&
                q.getLatitude() >= Math.min(p.getLatitude(), r.getLatitude()) &&
                q.getLongitude() <= Math.max(p.getLongitude(), r.getLongitude()) &&
                q.getLongitude() >= Math.min(p.getLongitude(), r.getLongitude())) {
            return true;
        }

        return false;
    }





    public double calculateAreaDensity(List<ResponseObject> segments) {
        int totalOrderCount = countTotalOrderIds(segments);

        // Get the area covered by the route
        double routeArea = calculateRouteArea(segments)  ;

        // Calculate area density
        double areaDensity = totalOrderCount > 0 ? routeArea / totalOrderCount : 0.0;

        return areaDensity;
    }




    private int countTotalOrderIds(List<ResponseObject> segments) {
        HashSet<String> totalOrderIds = new HashSet<>();

        // Get the order ID of the starting node if it exists
        String startingOrderId = segments.isEmpty() ? null : getOrderId(segments.get(0));

        // Iterate over segments, including starting node and last node if same as starting node
        for (ResponseObject segment : segments) {
            String orderId = getOrderId(segment);

            // Include the starting node and last node if same as starting node,
            // and also include segments with null order IDs or matching specific conditions
            if (orderId != null && !orderId.equals("break") && !orderId.equals("tab")) {
                totalOrderIds.add(orderId);
            }
        }

        return totalOrderIds.size();
    }






    private int countUniqueOrderIds(List<ResponseObject> segments) {
        HashSet<String> uniqueOrderIds = new HashSet<>();

        // Get the order ID of the starting node if it exists
        String startingOrderId = segments.isEmpty() ? null : getOrderId(segments.get(0));

        // Iterate over segments, excluding the starting node and last node if same as starting node
        for (int i = 1; i < segments.size() - 1; i++) {
            ResponseObject segment = segments.get(i);
            String orderId = getOrderId(segment);

            // Exclude the starting node and last node if same as starting node,
            // and also exclude segments with null order IDs or matching specific conditions
            if (orderId != null && !orderId.equals(startingOrderId) && !orderId.equals("break") && !orderId.equals("tab")) {
                uniqueOrderIds.add(orderId);
            }
        }

        return uniqueOrderIds.size();
    }




    // Method to get the order ID associated with a segment

    private String getOrderId(ResponseObject segment) {
        return segment.getOrderId();
    }







    public double calculateRouteArea(List<ResponseObject> segments) {

        // Initialize bounding box coordinates
        double minLat = Double.MAX_VALUE;
        double maxLat = Double.MIN_VALUE;
        double minLon = Double.MAX_VALUE;
        double maxLon = Double.MIN_VALUE;

        // Find the bounding box of the route
        for (ResponseObject segment : segments) {
            Double lat = segment.getLatitude();
            Double lon = segment.getLongitude();
            if (lat != null && lon != null) {
                if (lat < minLat) minLat = lat;
                if (lat > maxLat) maxLat = lat;
                if (lon < minLon) minLon = lon;
                if (lon > maxLon) maxLon = lon;
            }
        }

        // Calculate the area covered by the bounding box
        double routeArea = calculateBoundingBoxArea(minLat, maxLat, minLon, maxLon);

        return routeArea;
    }





    private double calculateBoundingBoxArea(double minLat, double maxLat, double minLon, double maxLon) {
        // Calculate the area covered by the bounding box (approximated as a rectangle)
        double latDistance = maxLat - minLat;
        double lonDistance = maxLon - minLon;

        // Calculate the area using the bounding box dimensions
        double area = latDistance * lonDistance;
        return area;
    }









    public double calculateAverageDistance(List<ResponseObject> segments) {

        double totalDistance = 0.0;

        // Iterate over segments and calculate total distance
        for (int i = 0; i < segments.size() - 1; i++) {
            ResponseObject currentSegment = segments.get(i);
            ResponseObject nextSegment = segments.get(i + 1);


            if (currentSegment.getLatitude() != null && currentSegment.getLongitude() != null &&
                    nextSegment.getLatitude() != null && nextSegment.getLongitude() != null) {
                double distance = calculateDistance(currentSegment, nextSegment);
                totalDistance += distance;
            }
        }

        // Calculate average distance
        int segmentCount = segments.size() - 1; // Number of segments is one less than number of nodes
        return totalDistance / segmentCount;
    }






    private double calculateDistance(ResponseObject segment1, ResponseObject segment2) {


        if (segment1.getLatitude() == null || segment1.getLongitude() == null ||
                segment2.getLatitude() == null || segment2.getLongitude() == null) {
            // Handle the case where latitude or longitude is null, for example by returning a default value
            return 0.0;
        }
        final int R = 6371; // Radius of the earth in km

        double lat1 = Math.toRadians(segment1.getLatitude());
        double lon1 = Math.toRadians(segment1.getLongitude());
        double lat2 = Math.toRadians(segment2.getLatitude());
        double lon2 = Math.toRadians(segment2.getLongitude());

        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(lat1) * Math.cos(lat2) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double distance = R * c; // Distance in kilometers

        return distance;
    }



}
