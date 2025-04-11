package model;

import dao.OrderDAO;
import dao.ReservationDAO;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import view.OrderStatusWindow;

public class StaffDashboard {
    private Staff staff;
    private Stage stage;
    private static final String BACKGROUND_IMAGE = "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?ixlib=rb-1.2.1&auto=format&fit=crop&w=1350&q=80";

    public StaffDashboard(Staff staff) {
        this.staff = staff;
        this.stage = new Stage();
        stage.setTitle("Staff Dashboard - " + staff.getFullName());
    }

    public void show() {
        // Background setup
        ImageView background = new ImageView(new Image(BACKGROUND_IMAGE, 800, 600, false, true));
        background.setOpacity(0.5);
        Pane overlay = new Pane();
        overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.3);");

        // Main content
        VBox mainContent = new VBox(20);
        mainContent.setAlignment(Pos.TOP_CENTER);
        mainContent.setPadding(new Insets(30));
        mainContent.setMaxWidth(600);

        // Welcome label
        Label welcomeLabel = new Label("Staff Dashboard\nWelcome, " + staff.getFullName() + " (" + staff.getPosition() + ")");
        welcomeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        welcomeLabel.setTextFill(Color.WHITE);
        welcomeLabel.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);");
        welcomeLabel.setTextAlignment(TextAlignment.CENTER);

        // Button grid
        GridPane buttonGrid = new GridPane();
        buttonGrid.setHgap(15);
        buttonGrid.setVgap(15);
        buttonGrid.setAlignment(Pos.CENTER);

        // Buttons
        Button viewReservationsButton = createStyledButton("View Reservations", "#3F51B5");
        Button viewOrdersButton = createStyledButton("View Orders", "#4CAF50");
        Button updateOrderStatusButton = createStyledButton("Update Order Status", "#FF9800");
        Button logoutButton = createStyledButton("Logout", "#F44336");

        buttonGrid.addRow(0, viewReservationsButton, viewOrdersButton);
        buttonGrid.addRow(1, updateOrderStatusButton, logoutButton);

        // Output area
        TextArea outputArea = new TextArea();
        outputArea.setEditable(false);
        outputArea.setStyle("-fx-font-size: 14px; -fx-control-inner-background: rgba(255,255,255,0.8);");
        outputArea.setPrefHeight(250);

        // Button actions
        viewReservationsButton.setOnAction(e -> showReservations(outputArea));
        viewOrdersButton.setOnAction(e -> showOrders(outputArea));
        updateOrderStatusButton.setOnAction(e -> new OrderStatusWindow().show());
        logoutButton.setOnAction(e -> stage.close());

        mainContent.getChildren().addAll(welcomeLabel, buttonGrid, outputArea);
        
        StackPane root = new StackPane(background, overlay, mainContent);
        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.show();
    }

    private Button createStyledButton(String text, String color) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; " +
                "-fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 10 25; " +
                "-fx-background-radius: 5; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 5, 0, 0, 1);");
        button.setOnMouseEntered(e -> button.setStyle(button.getStyle() + "-fx-cursor: hand; -fx-scale-x: 1.03; -fx-scale-y: 1.03;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; " +
                "-fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 10 25; " +
                "-fx-background-radius: 5; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 5, 0, 0, 1);"));
        return button;
    }

    private void showReservations(TextArea outputArea) {
        outputArea.clear();
        try {
            var reservations = new ReservationDAO().getAllReservations();
            if (reservations.isEmpty()) {
                outputArea.appendText("No reservations found.\n");
            } else {
                outputArea.appendText("All Reservations:\n");
                for (var reservation : reservations) {
                    outputArea.appendText(String.format(
                        "Reservation #%d for %d people at %s (Status: %s)\n",
                        reservation.getReservationId(),
                        reservation.getNumberOfGuests(),
                        reservation.getReservationTime().toString(),
                        reservation.getStatus()
                    ));
                }
            }
        } catch (Exception ex) {
            outputArea.appendText("Error loading reservations: " + ex.getMessage() + "\n");
        }
    }

    private void showOrders(TextArea outputArea) {
        outputArea.clear();
        try {
            var orders = new OrderDAO().getAllOrders();
            if (orders.isEmpty()) {
                outputArea.appendText("No orders found.\n");
            } else {
                outputArea.appendText("All Orders:\n");
                for (var order : orders) {
                    outputArea.appendText(String.format(
                        "Order #%d - Table: %d - Total: $%.2f (Status: %s)\n",
                        order.getOrderId(),
                        order.getTableId(),
                        order.getTotalAmount(),
                        order.getStatus()
                    ));
                    for (var item : order.getOrderItems()) {
                        outputArea.appendText(String.format(
                            "  - %d x Item #%d ($%.2f each)\n",
                            item.getQuantity(),
                            item.getItemId(),
                            item.getItemPrice()
                        ));
                    }
                }
            }
        } catch (Exception ex) {
            outputArea.appendText("Error loading orders: " + ex.getMessage() + "\n");
        }
    }
}