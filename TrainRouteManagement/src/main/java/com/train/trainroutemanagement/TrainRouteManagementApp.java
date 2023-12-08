package com.train.trainroutemanagement;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TrainRouteManagementApp extends Application {

    private TableView<Route> routeTableView;
    private List<Route> routes;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Маршруты поездов");

        routes = new ArrayList<>();
        routeTableView = new TableView<>();
        TableColumn<Route, String> trainNumberColumn = new TableColumn<>("Номер поезда");
        trainNumberColumn.setCellValueFactory(new PropertyValueFactory<>("trainNumber"));
        TableColumn<Route, String> routeNameColumn = new TableColumn<>("Название маршрута");
        routeNameColumn.setCellValueFactory(new PropertyValueFactory<>("routeName"));

        // Setup table view
        routeTableView.getColumns().addAll(trainNumberColumn, routeNameColumn);
        routeTableView.setItems(FXCollections.observableArrayList(routes));

        Button addRouteButton = new Button("Добавить маршрут");
        addRouteButton.setStyle("-fx-background-color: #8F9779; -fx-text-fill: white;");
        addRouteButton.setOnAction(e -> showAddRouteDialog());

        Button viewRouteButton = new Button("Посмотреть маршрут");
        viewRouteButton.setStyle("-fx-background-color: #8F9779; -fx-text-fill: white;");
        viewRouteButton.setOnAction(e -> showRouteDetails());

        Button editRouteButton = new Button("Редактировать маршрут");
        editRouteButton.setStyle("-fx-background-color: #8F9779; -fx-text-fill: white;");
        editRouteButton.setOnAction(e -> showEditRouteDialog());

        Button deleteRouteButton = new Button("Удалить маршрут");
        deleteRouteButton.setStyle("-fx-background-color: #8F9779; -fx-text-fill: white;");
        deleteRouteButton.setOnAction(e -> {
            Route selectedRoute = routeTableView.getSelectionModel().getSelectedItem();
            if (selectedRoute != null) {
                routes.remove(selectedRoute);
                routeTableView.setItems(FXCollections.observableArrayList(routes));
            }
        });

        VBox root = new VBox(10);
        root.setStyle("-fx-background-color: #D2E7D2;");
        root.setPadding(new Insets(10));
        root.getChildren().addAll(routeTableView, addRouteButton, viewRouteButton, editRouteButton, deleteRouteButton);

        Scene scene = new Scene(root, 500, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showAddRouteDialog() {
        Dialog<Route> dialog = new Dialog<>();
        dialog.setTitle("Добавить маршрут");
        dialog.setHeaderText("Добавить маршрут");

        ButtonType addButtonType = new ButtonType("Добавить", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        VBox content = new VBox(10);
        content.setPadding(new Insets(10));
        TextField trainNumberField = new TextField();
        trainNumberField.setPromptText("Номер поезда");
        TextField routeNameField = new TextField();
        routeNameField.setPromptText("Название маршрута");

        content.getChildren().addAll(trainNumberField, routeNameField);
        dialog.getDialogPane().setContent(content);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == addButtonType) {
                String trainNumber = trainNumberField.getText();
                String routeName = routeNameField.getText();
                return new Route(trainNumber, routeName);
            }
            return null;
        });

        Optional<Route> result = dialog.showAndWait();
        result.ifPresent(routes::add);
        routeTableView.setItems(FXCollections.observableArrayList(routes));
    }


    private void showRouteDetails() {
        Route selectedRoute = routeTableView.getSelectionModel().getSelectedItem();
        if (selectedRoute != null) {
            Stage detailsStage = new Stage();
            detailsStage.initModality(Modality.WINDOW_MODAL);
            detailsStage.initOwner(routeTableView.getScene().getWindow());
            detailsStage.setTitle("Делали маршрута: " + selectedRoute.getRouteName());

            TableView<RoutePoint> routeDetailsTableView = new TableView<>();
            TableColumn<RoutePoint, String> stationColumn = new TableColumn<>("Станция");
            stationColumn.setCellValueFactory(new PropertyValueFactory<>("stationName"));
            TableColumn<RoutePoint, String> arrivalColumn = new TableColumn<>("Время прибытия");
            arrivalColumn.setCellValueFactory(new PropertyValueFactory<>("arrivalTime"));
            TableColumn<RoutePoint, String> departureColumn = new TableColumn<>("Время отправления");
            departureColumn.setCellValueFactory(new PropertyValueFactory<>("departureTime"));

            routeDetailsTableView.getColumns().addAll(stationColumn, arrivalColumn, departureColumn);
            routeDetailsTableView.getItems().addAll(selectedRoute.getRoutePoints());

            VBox detailsRoot = new VBox(10);
            detailsRoot.setPadding(new Insets(10));
            detailsRoot.getChildren().add(routeDetailsTableView);

            Scene detailsScene = new Scene(detailsRoot, 300, 250);
            detailsStage.setScene(detailsScene);
            detailsStage.show();
        }
    }

    private void showEditRouteDialog() {
        Route selectedRoute = routeTableView.getSelectionModel().getSelectedItem();
        if (selectedRoute != null) {
            Dialog<Route> dialog = new Dialog<>();
            dialog.setTitle("Редактировать маршрут");
            dialog.setHeaderText("Редактировать маршрут");

            ButtonType applyButtonType = new ButtonType("Сохранить", ButtonBar.ButtonData.APPLY);
            dialog.getDialogPane().getButtonTypes().addAll(applyButtonType, ButtonType.CANCEL);

            // Form content for editing route details
            VBox content = new VBox(10);
            content.setPadding(new Insets(10));
            TextField trainNumberField = new TextField(selectedRoute.getTrainNumber());
            trainNumberField.setPromptText("Номер поезда");
            TextField routeNameField = new TextField(selectedRoute.getRouteName());
            routeNameField.setPromptText("Название маршрута");

            // Form content for editing route points
            TableView<RoutePoint> routePointsTableView = new TableView<>();
            TableColumn<RoutePoint, String> stationColumn = new TableColumn<>("Станция");
            stationColumn.setCellValueFactory(new PropertyValueFactory<>("stationName"));
            TableColumn<RoutePoint, String> arrivalColumn = new TableColumn<>("Время прибытия");
            arrivalColumn.setCellValueFactory(new PropertyValueFactory<>("arrivalTime"));
            TableColumn<RoutePoint, String> departureColumn = new TableColumn<>("Время отправления");
            departureColumn.setCellValueFactory(new PropertyValueFactory<>("departureTime"));

            routePointsTableView.getColumns().addAll(stationColumn, arrivalColumn, departureColumn);

            for (RoutePoint routePoint : selectedRoute.getRoutePoints()) {
                routePointsTableView.getItems().add(routePoint);
            }

            Button addRoutePointButton = new Button("Добавить точку маршрута");
            addRoutePointButton.setOnAction(e -> {
                Dialog<RoutePoint> addPointDialog = new Dialog<>();
                addPointDialog.setTitle("Добавить точку маршрута");
                addPointDialog.setHeaderText("Добавить точку маршрута");

                ButtonType addPointButtonType = new ButtonType("Добавить", ButtonBar.ButtonData.OK_DONE);
                addPointDialog.getDialogPane().getButtonTypes().addAll(addPointButtonType, ButtonType.CANCEL);

                VBox addPointContent = new VBox(10);
                addPointContent.setPadding(new Insets(10));
                TextField stationNameField = new TextField();
                stationNameField.setPromptText("Станция");
                TextField arrivalTimeField = new TextField();
                arrivalTimeField.setPromptText("Время прибытия");
                TextField departureTimeField = new TextField();
                departureTimeField.setPromptText("Время отправления");

                addPointContent.getChildren().addAll(stationNameField, arrivalTimeField, departureTimeField);
                addPointDialog.getDialogPane().setContent(addPointContent);

                addPointDialog.setResultConverter(buttonType -> {
                    if (buttonType == addPointButtonType) {
                        String stationName = stationNameField.getText();
                        String arrivalTime = arrivalTimeField.getText();
                        String departureTime = departureTimeField.getText();
                        return new RoutePoint(stationName, arrivalTime, departureTime);
                    }
                    return null;
                });

                Optional<RoutePoint> result = addPointDialog.showAndWait();
                result.ifPresent(selectedRoute::addRoutePoint);
                routePointsTableView.setItems(FXCollections.observableArrayList(selectedRoute.getRoutePoints()));
            });

            Button removeRoutePointButton = new Button("Удалить точку маршрута");
            removeRoutePointButton.setOnAction(e -> {
                RoutePoint selectedRoutePoint = routePointsTableView.getSelectionModel().getSelectedItem();
                if (selectedRoutePoint != null) {
                    selectedRoute.removeRoutePoint(selectedRoutePoint);
                    routePointsTableView.setItems(FXCollections.observableArrayList(selectedRoute.getRoutePoints()));
                }
            });

            Button editRoutePointButton = new Button("Редактировать точку маршрута");
            editRoutePointButton.setOnAction(e -> {
                RoutePoint selectedRoutePoint = routePointsTableView.getSelectionModel().getSelectedItem();
                if (selectedRoutePoint != null) {
                    Dialog<RoutePoint> editPointDialog = new Dialog<>();
                    editPointDialog.setTitle("Редактировать точку маршрута");
                    editPointDialog.setHeaderText("Редактировать точку маршрута");

                    ButtonType applyEditButtonType = new ButtonType("Сохранить", ButtonBar.ButtonData.APPLY);
                    editPointDialog.getDialogPane().getButtonTypes().addAll(applyEditButtonType, ButtonType.CANCEL);

                    VBox editPointContent = new VBox(10);
                    editPointContent.setPadding(new Insets(10));
                    TextField stationNameField = new TextField(selectedRoutePoint.getStationName());
                    TextField arrivalTimeField = new TextField(selectedRoutePoint.getArrivalTime());
                    TextField departureTimeField = new TextField(selectedRoutePoint.getDepartureTime());

                    editPointContent.getChildren().addAll(stationNameField, arrivalTimeField, departureTimeField);
                    editPointDialog.getDialogPane().setContent(editPointContent);

                    editPointDialog.setResultConverter(buttonType -> {
                        if (buttonType == applyEditButtonType) {
                            selectedRoutePoint.setStationName(stationNameField.getText());
                            selectedRoutePoint.setArrivalTime(arrivalTimeField.getText());
                            selectedRoutePoint.setDepartureTime(departureTimeField.getText());
                            return selectedRoutePoint;
                        }
                        return null;
                    });

                    editPointDialog.showAndWait();
                    routePointsTableView.refresh();
                }
            });

                    content.getChildren().addAll(trainNumberField, routeNameField, routePointsTableView, addRoutePointButton, removeRoutePointButton, editRoutePointButton);
            dialog.getDialogPane().setContent(content);

            // Convert the result to a route when the apply button is clicked
            dialog.setResultConverter(buttonType -> {
                if (buttonType == applyButtonType) {
                    selectedRoute.setTrainNumber(trainNumberField.getText());
                    selectedRoute.setRouteName(routeNameField.getText());
                    return selectedRoute;
                }
                return null;
            });

            dialog.showAndWait();
            routeTableView.refresh();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}