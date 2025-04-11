package model;

import dao.CustomerDAO;
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

public class CustomerRegistrationWindow {
    private static final String BACKGROUND_IMAGE = "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?ixlib=rb-1.2.1&auto=format&fit=crop&w=1350&q=80";

    public void show() {
        Stage stage = new Stage();
        stage.setTitle("Customer Registration");

        // Background setup
        ImageView background = new ImageView(new Image(BACKGROUND_IMAGE, 600, 500, false, true));
        background.setOpacity(0.5);
        Pane overlay = new Pane();
        overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.3);");

        // Main content
        VBox mainContent = new VBox(15);
        mainContent.setAlignment(Pos.TOP_CENTER);
        mainContent.setPadding(new Insets(30));
        mainContent.setMaxWidth(500);

        // Title
        Label titleLabel = new Label("Create Your Account");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.WHITE);
        titleLabel.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);");

        // Form grid
        GridPane formGrid = new GridPane();
        formGrid.setHgap(15);
        formGrid.setVgap(15);
        formGrid.setPadding(new Insets(20));
        formGrid.setStyle("-fx-background-color: rgba(255, 255, 255, 0.2); -fx-background-radius: 10;");

        // Form fields
        TextField nameField = createStyledTextField();
        TextField emailField = createStyledTextField();
        TextField phoneField = createStyledTextField();
        TextField addressField = createStyledTextField();
        TextField usernameField = createStyledTextField();
        PasswordField passwordField = createStyledPasswordField();
        PasswordField confirmPasswordField = createStyledPasswordField();

        Button registerButton = createStyledButton("Register", "#4CAF50", 16);
        registerButton.setOnAction(e -> {
            try {
                // Validate inputs
                if (!passwordField.getText().equals(confirmPasswordField.getText())) {
                    showAlert("Error", "Passwords do not match");
                    return;
                }

                // Create new customer
                Customer newCustomer = new Customer(
                    0,
                    nameField.getText(),
                    emailField.getText(),
                    phoneField.getText(),
                    addressField.getText(),
                    usernameField.getText(),
                    passwordField.getText()
                );

                // Save to database
                int newId = new CustomerDAO().insertCustomer(newCustomer);
                if (newId > 0) {
                    showAlert("Success", "Registration successful! ID: " + newId);
                    stage.close();
                } else {
                    showAlert("Error", "Registration failed");
                }
            } catch (Exception ex) {
                showAlert("Error", ex.getMessage());
            }
        });

        // Add components to grid
        formGrid.add(createFormLabel("Full Name:"), 0, 0);
        formGrid.add(nameField, 1, 0);
        formGrid.add(createFormLabel("Email:"), 0, 1);
        formGrid.add(emailField, 1, 1);
        formGrid.add(createFormLabel("Phone:"), 0, 2);
        formGrid.add(phoneField, 1, 2);
        formGrid.add(createFormLabel("Address:"), 0, 3);
        formGrid.add(addressField, 1, 3);
        formGrid.add(createFormLabel("Username:"), 0, 4);
        formGrid.add(usernameField, 1, 4);
        formGrid.add(createFormLabel("Password:"), 0, 5);
        formGrid.add(passwordField, 1, 5);
        formGrid.add(createFormLabel("Confirm Password:"), 0, 6);
        formGrid.add(confirmPasswordField, 1, 6);
        formGrid.add(registerButton, 0, 7, 2, 1);
        GridPane.setHalignment(registerButton, HPos.CENTER);

        mainContent.getChildren().addAll(titleLabel, formGrid);
        
        StackPane root = new StackPane(background, overlay, mainContent);
        Scene scene = new Scene(root, 600, 500);
        stage.setScene(scene);
        stage.show();
    }

    private TextField createStyledTextField() {
        TextField field = new TextField();
        field.setStyle("-fx-font-size: 14px; -fx-padding: 8px; -fx-background-radius: 5;");
        return field;
    }

    private PasswordField createStyledPasswordField() {
        PasswordField field = new PasswordField();
        field.setStyle("-fx-font-size: 14px; -fx-padding: 8px; -fx-background-radius: 5;");
        return field;
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