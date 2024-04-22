package com.Backendcode.RoutingBackrndcode.Repository;

import com.Backendcode.RoutingBackrndcode.model.RouteData;
import com.Backendcode.RoutingBackrndcode.model.RouteDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RouteDataRepository extends JpaRepository<RouteDataEntity, Long> {
    // Add custom query methods if needed
}
