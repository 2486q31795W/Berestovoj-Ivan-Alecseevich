package com.train.trainroutemanagement;

import java.util.ArrayList;

public class Route {
    private String trainNumber;
    private String routeName;
    private ArrayList<RoutePoint> routePoints;

    public Route(String trainNumber, String routeName) {
        this.trainNumber = trainNumber;
        this.routeName = routeName;
        this.routePoints = new ArrayList<>();
    }

    public void addRoutePoint(RoutePoint point) {
        routePoints.add(point);
    }

    public void removeRoutePoint(RoutePoint point) {
        routePoints.remove(point);
    }

    public void editRoutePoint(int index, RoutePoint newPoint) {
        routePoints.set(index, newPoint);
    }

    public String getTrainNumber() {
        return trainNumber;
    }

    public void setTrainNumber(String trainNumber) {
        this.trainNumber = trainNumber;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public ArrayList<RoutePoint> getRoutePoints() {
        return routePoints;
    }

    public void setRoutePoints(ArrayList<RoutePoint> routePoints) {
        this.routePoints = routePoints;
    }
}
