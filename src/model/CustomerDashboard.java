package model;

import dao.OrderDAO;
import dao.ReservationDAO;
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
import view.OrderWindow;
import view.ReservationWindow;

public class CustomerDashboard {
    private Customer customer;
    private Stage stage;
    private static final String BACKGROUND_IMAGE = "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?ixlib=rb-1.2.1&auto=format&fit=crop&w=1350&q=80";

    public CustomerDashboard(Customer customer) {
        this.customer = customer;
        this.stage = new Stage();
        stage.setTitle("Customer Dashboard - " + customer.getFullName());
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
        Label welcomeLabel = new Label("Welcome, " + customer.getFullName() + "!");
        welcomeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        welcomeLabel.setTextFill(Color.WHITE);
        welcomeLabel.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);");

        // Button styling
        String buttonStyle = "-fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 10 25; " +
                "-fx-background-radius: 5; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 5, 0, 0, 1);";
        String hoverStyle = "-fx-cursor: hand; -fx-scale-x: 1.03; -fx-scale-y: 1.03;";

        // Buttons
        Button bookTableButton = createStyledButton("Book a Table", "#4CAF50");
        Button placeOrderButton = createStyledButton("Place an Order", "#2196F3");
        Button viewReservationsButton = createStyledButton("View My Reservations", "#9C27B0");
        Button viewOrdersButton = createStyledButton("View My Orders", "#FF9800");
        Button updateProfileButton = createStyledButton("Update Profile", "#607D8B");
        Button logoutButton = createStyledButton("Logout", "#F44336");

        // Button grid
        GridPane buttonGrid = new GridPane();
        buttonGrid.setHgap(15);
        buttonGrid.setVgap(15);
        buttonGrid.setAlignment(Pos.CENTER);
        buttonGrid.addRow(0, bookTableButton, placeOrderButton);
        buttonGrid.addRow(1, viewReservationsButton, viewOrdersButton);
        buttonGrid.addRow(2, updateProfileButton, logoutButton);

        // Output area
        TextArea outputArea = new TextArea();
        outputArea.setEditable(false);
        outputArea.setStyle("-fx-font-size: 14px; -fx-control-inner-background: rgba(255,255,255,0.8);");
        outputArea.setPrefHeight(200);

        // Button actions
        bookTableButton.setOnAction(e -> new ReservationWindow(customer).show());
        placeOrderButton.setOnAction(e -> new OrderWindow(customer, null).show());
        viewReservationsButton.setOnAction(e -> showReservations(outputArea));
        viewOrdersButton.setOnAction(e -> showOrders(outputArea));
        updateProfileButton.setOnAction(e -> new CustomerProfileWindow(customer).show());
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
            var reservations = new ReservationDAO().getReservationsByCustomer(customer.getCustomerId());
            if (reservations.isEmpty()) {
                outputArea.appendText("You have no reservations.\n");
            } else {
                outputArea.appendText("Your Reservations:\n");
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
            var orders = new OrderDAO().getOrdersByCustomer(customer.getCustomerId());
            if (orders.isEmpty()) {
                outputArea.appendText("You have no orders.\n");
            } else {
                outputArea.appendText("Your Orders:\n");
                for (var order : orders) {
                    outputArea.appendText(String.format(
                        "Order #%d - Total: $%.2f (Status: %s)\n",
                        order.getOrderId(),
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