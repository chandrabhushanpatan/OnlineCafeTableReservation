package model;

import dao.ManagerDAO;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class ManagerLoginWindow {
    private static final int WINDOW_WIDTH = 600;
    private static final int WINDOW_HEIGHT = 400;
    private static final String BACKGROUND_IMAGE = "https://images.unsplash.com/photo-1555396273-367ea4eb4db5?ixlib=rb-1.2.1&auto=format&fit=crop&w=1350&q=80";

    public void show() {
        Stage stage = new Stage();
        stage.setTitle("Manager Login");

        // Set application icon
        try {
            stage.getIcons().add(new Image("file:src/main/resources/cafe_icon.png"));
        } catch (Exception e) {
            System.out.println("Icon not found, using default");
        }

        // Create background with overlay
        ImageView background = new ImageView(new Image(BACKGROUND_IMAGE, WINDOW_WIDTH, WINDOW_HEIGHT, false, true));
        background.setOpacity(0.6);
        
        // Create overlay pane for better text visibility
        Pane overlay = new Pane();
        overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.3);");

        // Create title label
        Label titleLabel = new Label("Manager Portal");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        titleLabel.setTextFill(Color.WHITE);
        titleLabel.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);");

        // Create form fields
        TextField userField = new TextField();
        userField.setStyle("-fx-font-size: 14px; -fx-padding: 8px;");
        PasswordField passField = new PasswordField();
        passField.setStyle("-fx-font-size: 14px; -fx-padding: 8px;");

        // Create login button with styling
        Button loginButton = createStyledButton("Login", "#FF9800", 16);

        // Add action
        loginButton.setOnAction(e -> {
            String username = userField.getText();
            String password = passField.getText();

            try {
                ManagerDAO managerDAO = new ManagerDAO();
                Manager manager = managerDAO.getManagerByUsername(username);
                
                if (manager != null && manager.getPassword().equals(password)) {
                    ManagerDashboard dashboard = new ManagerDashboard(manager);
                    dashboard.show();
                    stage.close();
                } else {
                    showAlert("Login Failed", "Invalid username or password");
                }
            } catch (Exception ex) {
                showAlert("Error", "Failed to login: " + ex.getMessage());
            }
        });

        // Create form grid
        GridPane formGrid = new GridPane();
        formGrid.setHgap(15);
        formGrid.setVgap(15);
        formGrid.setStyle("-fx-padding: 20px; -fx-background-color: rgba(255, 255, 255, 0.2); -fx-background-radius: 10;");
        
        Label userLabel = createFormLabel("Username:");
        Label passLabel = createFormLabel("Password:");
        
        formGrid.add(userLabel, 0, 0);
        formGrid.add(userField, 1, 0);
        formGrid.add(passLabel, 0, 1);
        formGrid.add(passField, 1, 1);
        formGrid.add(loginButton, 0, 2, 2, 1);
        GridPane.setHalignment(loginButton, javafx.geometry.HPos.CENTER);

        VBox mainContent = new VBox(30, titleLabel, formGrid);
        mainContent.setAlignment(javafx.geometry.Pos.CENTER);
        mainContent.setStyle("-fx-padding: 40px;");
        
        StackPane root = new StackPane();
        root.getChildren().addAll(background, overlay, mainContent);
        
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        stage.setScene(scene);
        stage.setMinWidth(WINDOW_WIDTH);
        stage.setMinHeight(WINDOW_HEIGHT);
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
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.4), 5, 0, 0, 1);"
        );
        button.setOnMouseEntered(e -> button.setStyle(
            button.getStyle() + 
            "-fx-cursor: hand; " +
            "-fx-scale-x: 1.05; " +
            "-fx-scale-y: 1.05; " +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.6), 8, 0, 0, 2);"
        ));
        button.setOnMouseExited(e -> button.setStyle(
            "-fx-background-color: " + color + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-size: " + fontSize + "px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 10 30;" +
            "-fx-background-radius: 5;" +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.4), 5, 0, 0, 1);"
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
        
        // Style the alert dialog
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