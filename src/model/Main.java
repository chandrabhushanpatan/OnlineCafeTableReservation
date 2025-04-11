package model;



import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Declare UI Components
        Label lblTitle = new Label("All Users");
        Button btnDisplay = new Button("Display All");
        Button btnClear = new Button("Clear All");
        Button btnClose = new Button("Close");
        TableView<Person> tblUsers = new TableView<>();

        // Position UI Components
        lblTitle.relocate(50, 0);
        btnDisplay.relocate(50, 300);
        btnClear.relocate(150, 300);
        btnClose.relocate(250, 300);
        tblUsers.relocate(50, 30);
        tblUsers.setPrefHeight(250);
        tblUsers.setPrefWidth(500);

        // Set Font for Title
        lblTitle.setFont(new javafx.scene.text.Font("Arial", 20));

        // Define Table Columns
        TableColumn<Person, Integer> colUid = new TableColumn<>("UID");
        TableColumn<Person, String> colFullName = new TableColumn<>("NAME");
        TableColumn<Person, String> colAddress = new TableColumn<>("ADDRESS");

        // Bind Columns to Person Properties
        colUid.setCellValueFactory(new PropertyValueFactory<>("pid"));
        colFullName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));

        // Add Columns to Table
        tblUsers.getColumns().add(colUid);
        tblUsers.getColumns().add(colFullName);
        tblUsers.getColumns().add(colAddress);

        // Sample Data
        ObservableList<Person> users = FXCollections.observableArrayList(
                new Person(1, "Ram ", "Lahan "),
                new Person(2, "Shyam ", "Kathmandu "),
                new Person(3, "Radha ", "Balakhu")
        );

        // Button Actions
        btnDisplay.setOnAction(e -> tblUsers.setItems(users));
        btnClear.setOnAction(e -> tblUsers.getItems().clear());
        btnClose.setOnAction(e -> primaryStage.close());

        // Add Components to Pane
        Pane root = new Pane();
        root.getChildren().addAll(lblTitle, btnDisplay, btnClear, btnClose, tblUsers);

        // Set Scene and Stage
        Scene scene = new Scene(root, 600, 400);
        primaryStage.setTitle("User Management System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
