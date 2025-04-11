package main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import model.CustomerLoginWindow;
import model.ManagerLoginWindow;
import model.StaffLoginWindow;

public class CafeReservationSystem extends Application {
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;
    private static final String BACKGROUND_IMAGE = "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?ixlib=rb-1.2.1&auto=format&fit=crop&w=1350&q=80";

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Cafe Reservation System");
        
        // Set application icon
        try {
            primaryStage.getIcons().add(new Image("file:src/main/resources/cafe_icon.png"));
        } catch (Exception e) {
            System.out.println("Icon not found, using default");
        }

        // Create background with overlay
        ImageView background = new ImageView(new Image(BACKGROUND_IMAGE, WINDOW_WIDTH, WINDOW_HEIGHT, false, true));
        background.setOpacity(0.7);
        
        // Create overlay pane for better text visibility
        Pane overlay = new Pane();
        overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.3);");
        
        // Create title label
        javafx.scene.control.Label titleLabel = new javafx.scene.control.Label("Welcome to MyCafe ");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        titleLabel.setTextFill(Color.WHITE);
        titleLabel.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);");

        // Create buttons with styling
        Button customerButton = createStyledButton("Customer", "#4CAF50", 18);
        Button staffButton = createStyledButton("Staff", "#2196F3", 18);
        Button managerButton = createStyledButton("Manager", "#FF9800", 18);

        customerButton.setOnAction(e -> {
            CustomerLoginWindow customerLogin = new CustomerLoginWindow();
            customerLogin.show();
        });

        staffButton.setOnAction(e -> {
            StaffLoginWindow staffLogin = new StaffLoginWindow();
            staffLogin.show();
        });

        managerButton.setOnAction(e -> {
            ManagerLoginWindow managerLogin = new ManagerLoginWindow();
            managerLogin.show();
        });

        VBox buttonBox = new VBox(30, customerButton, staffButton, managerButton);
        buttonBox.setStyle("-fx-padding: 40; -fx-alignment: center;");
        
        VBox mainContent = new VBox(50, titleLabel, buttonBox);
        mainContent.setAlignment(javafx.geometry.Pos.CENTER);
        
        StackPane root = new StackPane();
        root.getChildren().addAll(background, overlay, mainContent);
        
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(WINDOW_WIDTH);
        primaryStage.setMinHeight(WINDOW_HEIGHT);
        primaryStage.show();
    }

    private Button createStyledButton(String text, String color, double fontSize) {
        Button button = new Button(text);
        button.setStyle(
            "-fx-background-color: " + color + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-size: " + fontSize + "px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 15 40;" +
            "-fx-background-radius: 8;" +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.4), 10, 0, 0, 2);"
        );
        button.setOnMouseEntered(e -> button.setStyle(
            button.getStyle() + 
            "-fx-cursor: hand; " +
            "-fx-scale-x: 1.05; " +
            "-fx-scale-y: 1.05; " +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.6), 15, 0, 0, 3);"
        ));
        button.setOnMouseExited(e -> button.setStyle(
            "-fx-background-color: " + color + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-size: " + fontSize + "px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 15 40;" +
            "-fx-background-radius: 8;" +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.4), 10, 0, 0, 2);"
        ));
        return button;
    }

    public static void main(String[] args) {
        DatabaseInitializer.initializeDatabase();
        launch(args);
    }
}