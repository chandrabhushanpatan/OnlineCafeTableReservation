package model;

import dao.MenuItemDAO;
import dao.OrderDAO;
import dao.ReservationDAO;
import dao.StaffDAO;
import dao.TableDAO;
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

public class ManagerDashboard {
    private Manager manager;
    private Stage stage;
    private static final String BACKGROUND_IMAGE = "https://images.unsplash.com/photo-1555396273-367ea4eb4db5?ixlib=rb-1.2.1&auto=format&fit=crop&w=1350&q=80";

    public ManagerDashboard(Manager manager) {
        this.manager = manager;
        this.stage = new Stage();
        stage.setTitle("Manager Dashboard - " + manager.getFullName());
    }

    public void show() {
        // Background setup
        ImageView background = new ImageView(new Image(BACKGROUND_IMAGE, 900, 700, false, true));
        background.setOpacity(0.5);
        Pane overlay = new Pane();
        overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.3);");

        // Main content
        VBox mainContent = new VBox(20);
        mainContent.setAlignment(Pos.TOP_CENTER);
        mainContent.setPadding(new Insets(30));
        mainContent.setMaxWidth(700);

        // Welcome label
        Label welcomeLabel = new Label("Manager Dashboard\nWelcome, " + manager.getFullName() + "!");
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
        Button viewStaffButton = createStyledButton("View Staff", "#FF5722");
        Button viewReservationsButton = createStyledButton("View Reservations", "#3F51B5");
        Button viewOrdersButton = createStyledButton("View Orders", "#4CAF50");
        Button viewMenuButton = createStyledButton("View Menu", "#9C27B0");
        Button viewTablesButton = createStyledButton("View Tables", "#607D8B");
        Button logoutButton = createStyledButton("Logout", "#F44336");

        buttonGrid.addRow(0, viewStaffButton, viewReservationsButton);
        buttonGrid.addRow(1, viewOrdersButton, viewMenuButton);
        buttonGrid.addRow(2, viewTablesButton, logoutButton);

        // Output area
        TextArea outputArea = new TextArea();
        outputArea.setEditable(false);
        outputArea.setStyle("-fx-font-size: 14px; -fx-control-inner-background: rgba(255,255,255,0.8);");
        outputArea.setPrefHeight(250);

        // Button actions
        viewStaffButton.setOnAction(e -> showStaff(outputArea));
        viewReservationsButton.setOnAction(e -> showReservations(outputArea));
        viewOrdersButton.setOnAction(e -> showOrders(outputArea));
        viewMenuButton.setOnAction(e -> showMenu(outputArea));
        viewTablesButton.setOnAction(e -> showTables(outputArea));
        logoutButton.setOnAction(e -> stage.close());

        mainContent.getChildren().addAll(welcomeLabel, buttonGrid, outputArea);
        
        StackPane root = new StackPane(background, overlay, mainContent);
        Scene scene = new Scene(root, 900, 700);
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

    private void showStaff(TextArea outputArea) {
        outputArea.clear();
        try {
            var staffList = new StaffDAO().getAllStaff();
            if (staffList.isEmpty()) {
                outputArea.appendText("No staff members found.\n");
            } else {
                outputArea.appendText("Staff Members:\n");
                staffList.forEach(staff -> outputArea.appendText(String.format(
                    "ID: %d - %s (%s) - Status: %s\n",
                    staff.getStaffId(), staff.getFullName(), 
                    staff.getPosition(), staff.getStatus()
                )));
            }
        } catch (Exception ex) {
            outputArea.appendText("Error loading staff: " + ex.getMessage() + "\n");
        }
    }

    private void showReservations(TextArea outputArea) {
        outputArea.clear();
        try {
            var reservations = new ReservationDAO().getAllReservations();
            if (reservations.isEmpty()) {
                outputArea.appendText("No reservations found.\n");
            } else {
                outputArea.appendText("All Reservations:\n");
                reservations.forEach(res -> outputArea.appendText(String.format(
                    "Reservation #%d for %d people at %s (Status: %s)\n",
                    res.getReservationId(), res.getNumberOfGuests(), 
                    res.getReservationTime().toString(), res.getStatus()
                )));
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
                orders.forEach(order -> outputArea.appendText(String.format(
                    "Order #%d - Total: $%.2f (Status: %s)\n",
                    order.getOrderId(), order.getTotalAmount(), order.getStatus()
                )));
            }
        } catch (Exception ex) {
            outputArea.appendText("Error loading orders: " + ex.getMessage() + "\n");
        }
    }

    private void showMenu(TextArea outputArea) {
        outputArea.clear();
        try {
            var menuItems = new MenuItemDAO().getAllMenuItems();
            if (menuItems.isEmpty()) {
                outputArea.appendText("No menu items found.\n");
            } else {
                outputArea.appendText("Menu Items:\n");
                menuItems.forEach(item -> outputArea.appendText(String.format(
                    "ID: %d - %s - $%.2f - Available: %s\n",
                    item.getItemId(), item.getName(), 
                    item.getPrice(), item.isAvailable() ? "Yes" : "No"
                )));
            }
        } catch (Exception ex) {
            outputArea.appendText("Error loading menu: " + ex.getMessage() + "\n");
        }
    }

    private void showTables(TextArea outputArea) {
        outputArea.clear();
        try {
            var tables = new TableDAO().getAllTables();
            if (tables.isEmpty()) {
                outputArea.appendText("No tables found.\n");
            } else {
                outputArea.appendText("Tables:\n");
                tables.forEach(table -> outputArea.appendText(String.format(
                    "ID: %d - %s (%s) - Capacity: %d - Status: %s\n",
                    table.getTableId(), table.getTableNumber(), 
                    table.getTableType(), table.getCapacity(), table.getStatus()
                )));
            }
        } catch (Exception ex) {
            outputArea.appendText("Error loading tables: " + ex.getMessage() + "\n");
        }
    }
}