package view;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import dao.ReservationDAO;
import dao.TableDAO;
import javafx.collections.FXCollections;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import model.Customer;
import model.Reservation;
import model.Table;

public class ReservationWindow {
    private Customer customer;
    private TableDAO tableDAO;
    private ReservationDAO reservationDAO;
    private static final String BACKGROUND_IMAGE = "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?ixlib=rb-1.2.1&auto=format&fit=crop&w=1350&q=80";

    public ReservationWindow(Customer customer) {
        this.customer = customer;
        this.tableDAO = new TableDAO();
        this.reservationDAO = new ReservationDAO();
    }

    public void show() {
        Stage stage = new Stage();
        stage.setTitle("Make a Reservation");

        // Background setup
        ImageView background = new ImageView(new Image(BACKGROUND_IMAGE, 700, 600, false, true));
        background.setOpacity(0.5);
        Pane overlay = new Pane();
        overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.3);");

        // Main content
        VBox mainContent = new VBox(20);
        mainContent.setAlignment(Pos.TOP_CENTER);
        mainContent.setPadding(new Insets(30));
        mainContent.setMaxWidth(600);

        // Title
        Label titleLabel = new Label("Make a Reservation");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.WHITE);
        titleLabel.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);");

        // Form grid
        GridPane formGrid = new GridPane();
        formGrid.setHgap(15);
        formGrid.setVgap(15);
        formGrid.setPadding(new Insets(20));
        formGrid.setStyle("-fx-background-color: rgba(255, 255, 255, 0.2); -fx-background-radius: 10;");

        // Date and Time Picker
        Label dateLabel = createFormLabel("Date and Time:");
        DatePicker datePicker = new DatePicker();
        datePicker.setValue(LocalDateTime.now().toLocalDate());
        datePicker.setStyle("-fx-font-size: 14px;");
        
        ComboBox<String> hourCombo = new ComboBox<>(FXCollections.observableArrayList(
            "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00",
            "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00"
        ));
        hourCombo.setValue("18:00");
        hourCombo.setStyle("-fx-font-size: 14px;");

        // Duration
        Label durationLabel = createFormLabel("Duration (hours):");
        Spinner<Integer> durationSpinner = new Spinner<>(1, 4, 2);
        durationSpinner.setStyle("-fx-font-size: 14px;");

        // Number of Guests
        Label guestsLabel = createFormLabel("Number of Guests:");
        Spinner<Integer> guestsSpinner = new Spinner<>(1, 20, 2);
        guestsSpinner.setStyle("-fx-font-size: 14px;");

        // Special Requests
        Label requestsLabel = createFormLabel("Special Requests:");
        TextArea requestsArea = new TextArea();
        requestsArea.setPrefRowCount(3);
        requestsArea.setStyle("-fx-font-size: 14px; -fx-control-inner-background: rgba(255,255,255,0.8);");

        // Find Tables Button
        Button findTablesButton = createStyledButton("Find Available Tables", "#4CAF50", 14);

        // Table Selection
        Label tableLabel = createFormLabel("Available Tables:");
        ListView<Table> tablesListView = new ListView<>();
        tablesListView.setPrefHeight(150);
        tablesListView.setStyle("-fx-font-size: 14px; -fx-control-inner-background: rgba(255,255,255,0.8);");

        // Submit Button
        Button submitButton = createStyledButton("Make Reservation", "#2196F3", 16);
        submitButton.setDisable(true);

        // Add components to grid
        formGrid.add(dateLabel, 0, 0);
        formGrid.add(datePicker, 1, 0);
        formGrid.add(hourCombo, 2, 0);
        formGrid.add(durationLabel, 0, 1);
        formGrid.add(durationSpinner, 1, 1);
        formGrid.add(guestsLabel, 0, 2);
        formGrid.add(guestsSpinner, 1, 2);
        formGrid.add(requestsLabel, 0, 3);
        formGrid.add(requestsArea, 1, 3, 2, 1);
        formGrid.add(findTablesButton, 0, 4, 3, 1);
        formGrid.add(tableLabel, 0, 5);
        formGrid.add(tablesListView, 1, 5, 2, 1);
        formGrid.add(submitButton, 0, 6, 3, 1);
        GridPane.setHalignment(findTablesButton, HPos.CENTER);
        GridPane.setHalignment(submitButton, HPos.CENTER);

        // Find Tables Button Action
        findTablesButton.setOnAction(e -> {
            try {
                LocalTime selectedTime = LocalTime.parse(hourCombo.getValue(), 
                    DateTimeFormatter.ofPattern("HH:mm"));
                
                LocalDateTime selectedDateTime = LocalDateTime.of(
                    datePicker.getValue(), 
                    selectedTime
                );
                
                int duration = durationSpinner.getValue();
                int guests = guestsSpinner.getValue();

                List<Table> availableTables = tableDAO.getAvailableTables(
                    selectedDateTime,
                    selectedDateTime.plusHours(duration),
                    guests
                );

                tablesListView.setItems(FXCollections.observableArrayList(availableTables));
                submitButton.setDisable(availableTables.isEmpty());
                
                if (availableTables.isEmpty()) {
                    showAlert("No Tables Available", 
                        "No tables available for the selected time and party size.");
                }
            } catch (Exception ex) {
                showAlert("Error", "Failed to find tables: " + ex.getMessage());
            }
        });

        // Submit Button Action
        submitButton.setOnAction(e -> {
            try {
                Table selectedTable = tablesListView.getSelectionModel().getSelectedItem();
                if (selectedTable == null) {
                    showAlert("Error", "Please select a table");
                    return;
                }

                LocalTime selectedTime = LocalTime.parse(hourCombo.getValue(), 
                    DateTimeFormatter.ofPattern("HH:mm"));
                
                LocalDateTime selectedDateTime = LocalDateTime.of(
                    datePicker.getValue(),
                    selectedTime
                );
                
                int duration = durationSpinner.getValue();
                int guests = guestsSpinner.getValue();
                String requests = requestsArea.getText();

                Reservation reservation = new Reservation(
                    0,
                    customer.getCustomerId(),
                    selectedTable.getTableId(),
                    selectedDateTime,
                    selectedDateTime.plusHours(duration),
                    guests,
                    "Confirmed",
                    requests
                );

                int reservationId = reservationDAO.createReservation(reservation);
                
                showAlert("Success", 
                    String.format("Reservation #%d created successfully!", reservationId));
                stage.close();
            } catch (Exception ex) {
                showAlert("Error", "Failed to create reservation: " + ex.getMessage());
            }
        });

        mainContent.getChildren().addAll(titleLabel, formGrid);
        
        StackPane root = new StackPane(background, overlay, mainContent);
        Scene scene = new Scene(root, 700, 600);
        stage.setScene(scene);
        stage.show();
    }

    private Button createStyledButton(String text, String color, double fontSize) {
        Button button = new Button(text);
        button.setStyle(
            "-fx-background-color: " + color + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-size: " + fontSize + "px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 10 25;" +
            "-fx-background-radius: 5;" +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 5, 0, 0, 1);"
        );
        button.setOnMouseEntered(e -> button.setStyle(
            button.getStyle() + 
            "-fx-cursor: hand; " +
            "-fx-scale-x: 1.05; " +
            "-fx-scale-y: 1.05; "
        ));
        button.setOnMouseExited(e -> button.setStyle(
            "-fx-background-color: " + color + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-size: " + fontSize + "px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 10 25;" +
            "-fx-background-radius: 5;" +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 5, 0, 0, 1);"
        ));
        return button;
    }

    private Label createFormLabel(String text) {
        Label label = new Label(text);
        label.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        label.setTextFill(Color.WHITE);
        label.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.5), 3, 0, 0, 1);");
        return label;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle(
            "-fx-background-color: #f8f8f8;" +
            "-fx-border-color: #e74c3c;" +
            "-fx-border-width: 2px;" +
            "-fx-border-radius: 5px;" +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 10, 0, 0, 0);"
        );
        alert.showAndWait();
    }
}