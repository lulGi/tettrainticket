package com.lul.train.domain.route.aggregate;

import com.lul.common.core.domain.AggregateRoot;
import com.lul.common.core.exception.BusinessException;
import com.lul.common.core.exception.ErrorCode;
import lombok.Getter;

@Getter
public class Route extends AggregateRoot<String> {

    private String routeCode;
    private String originStationId;
    private String destinationStationId;
    private Integer distance;
    private Integer estimatedDuration;

    /**
     * Private constructor - force use factory method
     */
    private Route(){
        super();
    }

    /**
     * Factory method to create new Route
     */
    public static Route create(
            String routeCode,
            String originStationId,
            String destinationStationId,
            Integer distance,
            Integer estimatedDuration
    ){

        //Validation route code
        validateRouteCode(routeCode);
        validateDistance(distance);
        validateDuration(estimatedDuration);
        validateStations(originStationId, destinationStationId);

        Route route = new Route();
        route.routeCode = routeCode;
        route.originStationId = originStationId;
        route.destinationStationId = destinationStationId;
        route.distance = distance;
        route.estimatedDuration = estimatedDuration;

        return route;
    }

    /**
     * Update route information
     */
    public void updateInfo(Integer distance, Integer estimatedDuration) {
        if(distance != null ) {
            validateDistance(distance);
            this.distance = distance;
        }
        if(estimatedDuration != null) {
            validateDuration(estimatedDuration);
            this.estimatedDuration = estimatedDuration;
        }
    }


    private static void validateRouteCode(String routeCode) {
        if (routeCode == null || routeCode.isBlank()) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, "Route code cannot be empty");
        }
        if (!routeCode.matches("^[A-Z0-9]{2,10}-[A-Z0-9]{2,10}$")) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR,
                    "Route code must be in format: ORIGIN-DEST (e.g., HN-SG)");
        }
    }

    private static void validateStations(String originStationId, String destinationStationId) {
        if (originStationId == null || originStationId.isBlank()) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, "Origin station ID cannot be empty");
        }
        if (destinationStationId == null || destinationStationId.isBlank()) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, "Destination station ID cannot be empty");
        }
        if (originStationId.equals(destinationStationId)) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR,
                    "Origin and destination stations must be different");
        }
    }

    private static void validateDistance(Integer distance) {
        if (distance == null || distance <= 0) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, "Distance must be greater than 0");
        }
    }

    private static void validateDuration(Integer estimatedDuration) {
        if (estimatedDuration == null || estimatedDuration <= 0) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR,
                    "Estimated duration must be greater than 0");
        }
    }

}
