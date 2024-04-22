package com.Backendcode.RoutingBackrndcode.Controller;

import com.Backendcode.RoutingBackrndcode.Service.Routeservice;
import com.Backendcode.RoutingBackrndcode.model.RouteList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/Route")
@CrossOrigin(origins = "http://localhost:3000")
public class RouteController {

    private final Routeservice routeService;
    private Map<String, Object> routeValidationResult;
    private boolean validationSuccessful;
    @Autowired
    public RouteController(Routeservice routeService) {
        this.routeService = routeService;
    }

    @PostMapping("/Route_Results")
    public Map<String, Object> routeValidationResults(@RequestBody RouteList routeList) {
        routeValidationResult = routeService.RouteValidationResults(routeList.getRouteList());

        validationSuccessful = isValidationSuccessful(routeValidationResult);
        return Map.of("result", validationSuccessful);
    }

    @GetMapping("/Route_Results")
    public Map<String, Object> getRouteValidationResults() {
        if (validationSuccessful) {
            return routeValidationResult;
        } else {
            return Map.of("error", "Validation not successful");
        }
    }



    private boolean isValidationSuccessful(Map<String, Object> validationResults) {
        return validationResults != null;
    }

    @DeleteMapping("/Route_Results")
    public Map<String, Object> deleteRouteValidationResults() {
        if (validationSuccessful) {
            routeValidationResult = null;
            validationSuccessful = false;
            return Map.of("success", "Route validation results deleted successfully");
        } else {
            return Map.of("error", "No route validation results to delete");
        }



}

}
