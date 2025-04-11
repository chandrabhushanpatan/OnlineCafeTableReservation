package view;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import dao.MenuItemDAO;
import dao.OrderDAO;
import dao.TableDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Customer;
import model.MenuItem;
import model.Order;
import model.OrderItem;
import model.Table;

public class OrderWindow {
    private Customer customer;
    private Table table;
    private MenuItemDAO menuItemDAO;
    private OrderDAO orderDAO;
    private TableDAO tableDAO;
    private List<OrderItem> currentOrderItems = new ArrayList<>();

    public OrderWindow(Customer customer, Table table) {
        this.customer = customer;
        this.table = table;
        this.menuItemDAO = new MenuItemDAO();
        this.orderDAO = new OrderDAO();
        this.tableDAO = new TableDAO();
    }

    public void show() {
        Stage stage = new Stage();
        stage.setTitle("Place an Order");
        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(10));
        
        // Table Selection (if not provided)
        ComboBox<Table> tableCombo = new ComboBox<>();
        if (table == null) {
            try {
                tableCombo.setItems(FXCollections.observableArrayList(tableDAO.getAllTables()));
                tableCombo.setPromptText("Select a table");
            } catch (Exception e) {
                showAlert("Error", "Failed to load tables: " + e.getMessage());
            }
        }
        
        // Menu Items Table
        TableView<MenuItem> menuTable = new TableView<>();
        TableColumn<MenuItem, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<MenuItem, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        TableColumn<MenuItem, Double> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        menuTable.getColumns().addAll(nameCol, descCol, priceCol);

        try {
            menuTable.setItems(FXCollections.observableArrayList(menuItemDAO.getAllMenuItems()));
        } catch (Exception e) {
            showAlert("Error", "Failed to load menu: " + e.getMessage());
        }
        
        // Order Items Table
        TableView<OrderItem> orderTable = new TableView<>();
        
        TableColumn<OrderItem, String> itemNameCol = new TableColumn<>("Item");
        itemNameCol.setCellValueFactory(cell -> {
            try {
                MenuItem item = menuItemDAO.getMenuItemById(cell.getValue().getItemId());
                return new SimpleStringProperty(item != null ? item.getName() : "Item #" + cell.getValue().getItemId());
            } catch (Exception e) {
                return new SimpleStringProperty("Item #" + cell.getValue().getItemId());
            }
        });
        
        TableColumn<OrderItem, Integer> quantityCol = new TableColumn<>("Qty");
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        TableColumn<OrderItem, Double> priceCol2 = new TableColumn<>("Price");
        priceCol2.setCellValueFactory(new PropertyValueFactory<>("itemPrice"));
        TableColumn<OrderItem, Double> totalCol = new TableColumn<>("Total");
        totalCol.setCellValueFactory(cell -> 
            new javafx.beans.property.SimpleDoubleProperty(
                cell.getValue().getItemPrice() * cell.getValue().getQuantity()
            ).asObject()
        );

        orderTable.getColumns().addAll(itemNameCol, quantityCol, priceCol2, totalCol);
        orderTable.setItems(FXCollections.observableArrayList(currentOrderItems));

        // Quantity Controls
        Spinner<Integer> quantitySpinner = new Spinner<>(1, 10, 1);
        TextArea specialInstructions = new TextArea();
        specialInstructions.setPromptText("Special instructions...");
        specialInstructions.setPrefRowCount(2);

        Button addButton = new Button("Add to Order");
        addButton.setOnAction(e -> {
            MenuItem selectedItem = menuTable.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                OrderItem orderItem = new OrderItem(
                    0, // Will be set when order is created
                    0, // Will be set when order is created
                    selectedItem.getItemId(),
                    quantitySpinner.getValue(),
                    specialInstructions.getText(),
                    selectedItem.getPrice()
                );
                currentOrderItems.add(orderItem);
                orderTable.refresh();
                specialInstructions.clear();
            } else {
                showAlert("Error", "Please select a menu item first");
            }
        });

        Button removeButton = new Button("Remove Selected");
        removeButton.setOnAction(e -> {
            OrderItem selected = orderTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                currentOrderItems.remove(selected);
                orderTable.refresh();
            } else {
                showAlert("Error", "Please select an item to remove");
            }
        });

        // Order Controls
        TextArea notesArea = new TextArea();
        notesArea.setPromptText("Order notes...");
        notesArea.setPrefRowCount(2);

        Label totalLabel = new Label("Total: $0.00");

        Button submitButton = new Button("Submit Order");
        submitButton.setDisable(table == null);
        submitButton.setOnAction(e -> {
            if (currentOrderItems.isEmpty()) {
                showAlert("Error", "Please add items to the order");
                return;
            }

            try {
                // Calculate total
                double total = currentOrderItems.stream()
                    .mapToDouble(oi -> oi.getItemPrice() * oi.getQuantity())
                    .sum();

                // Create order
                Order order = new Order(
                    0, // Will be generated
                    customer.getCustomerId(),
                    table != null ? table.getTableId() : tableCombo.getValue().getTableId(),
                    LocalDateTime.now(),
                    "Pending",
                    total,
                    notesArea.getText(),
                    currentOrderItems
                );

                int orderId = orderDAO.createOrder(order);
                orderDAO.addOrderItems(orderId, currentOrderItems);

                showAlert("Success", 
                    String.format("Order #%d placed successfully!", orderId));
                stage.close();
            } catch (Exception ex) {
                showAlert("Error", "Failed to place order: " + ex.getMessage());
            }
        });

        // Table selection handler
        tableCombo.setOnAction(e -> {
            Table selectedTable = tableCombo.getValue();
            submitButton.setDisable(selectedTable == null);
        });

        // Layout
        HBox menuControls = new HBox(10,
            new Label("Quantity:"), quantitySpinner,
            addButton, removeButton
        );
        menuControls.setPadding(new Insets(10, 0, 10, 0));

        VBox menuBox = new VBox(10,
            new Label("Menu Items"), menuTable,
            new Label("Special Instructions:"), specialInstructions,
            menuControls
        );

        VBox orderBox = new VBox(10,
            new Label("Your Order"), orderTable,
            new Label("Order Notes:"), notesArea,
            totalLabel, submitButton
        );

        if (table == null) {
            VBox topBox = new VBox(10,
                new Label("Select Table:"), tableCombo
            );
            borderPane.setTop(topBox);
        }

        borderPane.setLeft(menuBox);
        borderPane.setRight(orderBox);

        // Update total when order changes
        orderTable.getItems().addListener((ListChangeListener.Change<? extends OrderItem> change) -> {
            double total = orderTable.getItems().stream()
                .mapToDouble(oi -> oi.getItemPrice() * oi.getQuantity())
                .sum();
            totalLabel.setText(String.format("Total: $%.2f", total));
        });

        Scene scene = new Scene(borderPane, 900, 600);
        stage.setScene(scene);
        stage.show();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}