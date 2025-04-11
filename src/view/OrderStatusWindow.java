package view;

import dao.OrderDAO;
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
import model.Order;

public class OrderStatusWindow {
    private static final String BACKGROUND_IMAGE = "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?ixlib=rb-1.2.1&auto=format&fit=crop&w=1350&q=80";

    public void show() {
        Stage stage = new Stage();
        stage.setTitle("Update Order Status");
        
        // Background setup
        ImageView background = new ImageView(new Image(BACKGROUND_IMAGE, 600, 400, false, true));
        background.setOpacity(0.5);
        Pane overlay = new Pane();
        overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.3);");

        // Main content
        VBox mainContent = new VBox(20);
        mainContent.setAlignment(Pos.TOP_CENTER);
        mainContent.setPadding(new Insets(30));
        mainContent.setMaxWidth(500);

        // Title
        Label titleLabel = new Label("Update Order Status");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.WHITE);
        titleLabel.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);");

        // Form grid
        GridPane formGrid = new GridPane();
        formGrid.setHgap(15);
        formGrid.setVgap(15);
        formGrid.setPadding(new Insets(20));
        formGrid.setStyle("-fx-background-color: rgba(255, 255, 255, 0.2); -fx-background-radius: 10;");

        // Order selection
        ComboBox<Order> orderCombo = new ComboBox<>();
        orderCombo.setPromptText("Select an order");
        orderCombo.setStyle("-fx-font-size: 14px; -fx-pref-width: 300px;");
        
        // Status selection
        ComboBox<String> statusCombo = new ComboBox<>(
            FXCollections.observableArrayList(
                "Pending", "Preparing", "Ready", "Served", "Cancelled"
            )
        );
        statusCombo.setPromptText("Select new status");
        statusCombo.setStyle("-fx-font-size: 14px;");
        
        // Update button
        Button updateButton = createStyledButton("Update Status", "#FF9800", 16);
        updateButton.setDisable(true);
        
        // Output area
        TextArea outputArea = new TextArea();
        outputArea.setEditable(false);
        outputArea.setStyle("-fx-font-size: 14px; -fx-control-inner-background: rgba(255,255,255,0.8);");
        outputArea.setPrefHeight(150);

        // Load orders
        try {
            OrderDAO orderDAO = new OrderDAO();
            orderCombo.setItems(FXCollections.observableArrayList(orderDAO.getAllOrders()));
        } catch (Exception e) {
            outputArea.appendText("Error loading orders: " + e.getMessage() + "\n");
        }
        
        // Order selection handler
        orderCombo.setOnAction(e -> {
            Order selected = orderCombo.getValue();
            if (selected != null) {
                statusCombo.setValue(selected.getStatus());
                updateButton.setDisable(false);
            } else {
                updateButton.setDisable(true);
            }
        });
        
        // Update button handler
        updateButton.setOnAction(e -> {
            Order selectedOrder = orderCombo.getValue();
            String newStatus = statusCombo.getValue();
            
            if (selectedOrder != null && newStatus != null) {
                try {
                    OrderDAO orderDAO = new OrderDAO();
                    if (orderDAO.updateOrderStatus(selectedOrder.getOrderId(), newStatus)) {
                        outputArea.appendText(String.format(
                            "Order #%d status updated to '%s'\n",
                            selectedOrder.getOrderId(),
                            newStatus
                        ));
                        // Refresh the order list
                        orderCombo.setItems(FXCollections.observableArrayList(orderDAO.getAllOrders()));
                        orderCombo.getSelectionModel().clearSelection();
                    } else {
                        outputArea.appendText("Failed to update order status\n");
                    }
                } catch (Exception ex) {
                    outputArea.appendText("Error updating order: " + ex.getMessage() + "\n");
                }
            }
        });

        // Add components to form
        formGrid.add(createFormLabel("Order:"), 0, 0);
        formGrid.add(orderCombo, 1, 0);
        formGrid.add(createFormLabel("New Status:"), 0, 1);
        formGrid.add(statusCombo, 1, 1);
        formGrid.add(updateButton, 0, 2, 2, 1);
        GridPane.setHalignment(updateButton, HPos.CENTER);

        mainContent.getChildren().addAll(titleLabel, formGrid, outputArea);
        
        StackPane root = new StackPane(background, overlay, mainContent);
        Scene scene = new Scene(root, 600, 400);
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
            "-fx-padding: 10 30;" +
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
            "-fx-padding: 10 30;" +
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
}