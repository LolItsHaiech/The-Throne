package client.scenes;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.scene.SubScene;
import exceptions.AuthenticationException;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import obj.auth.User;

public class LoginSubScene extends SubScene {

    private final VBox root;
    private final TextField displayNameField;
    private final TextField usernameField;
    private final PasswordField passwordField;
    private final PasswordField comfirmPasswordField;
    private final Label messageLabel;
    private boolean isRegisterMode = false;

    public LoginSubScene() {
        root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPrefSize(1280, 720);


        // Title
        Label titleLabel = new Label("THE THRONE");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 48));
        titleLabel.setTextFill(Color.GOLD);


        VBox formContainer = new VBox(15);
        formContainer.setAlignment(Pos.CENTER);
        formContainer.setMaxWidth(400);
        formContainer.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7); " +
                                       "-fx-background-radius: 10; " +
                                       "-fx-padding: 30;");

        // Form title
        Label formTitle = new Label("Login");
        formTitle.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        formTitle.setTextFill(Color.WHITE);

        displayNameField = new TextField();
        displayNameField.setPromptText("Display Name");
        displayNameField.setPrefHeight(40);
        displayNameField.setStyle("-fx-font-size: 14;");
        displayNameField.setVisible(false);
        displayNameField.setManaged(false);

        usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setPrefHeight(40);
        usernameField.setStyle("-fx-font-size: 14;");


        passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setPrefHeight(40);
        passwordField.setStyle("-fx-font-size: 14;");

        comfirmPasswordField = new PasswordField();
        comfirmPasswordField.setPromptText("Confirm Password");
        comfirmPasswordField.setPrefHeight(40);
        comfirmPasswordField.setStyle("-fx-font-size: 14;");
        comfirmPasswordField.setVisible(false);
        comfirmPasswordField.setManaged(false);

        messageLabel = new Label();
        messageLabel.setTextFill(Color.RED);
        messageLabel.setFont(Font.font("Arial", 12));

        // Login/Register button
        Button actionButton = new Button("Login");
        actionButton.setPrefHeight(40);
        actionButton.setPrefWidth(150);
        actionButton.setStyle("-fx-background-color: #4CAF50; " +
                                      "-fx-text-fill: white; " +
                                      "-fx-font-size: 16; " +
                                      "-fx-font-weight: bold;");
        actionButton.setOnAction(e -> handleAction());

        // Toggle button
        Button toggleButton = new Button("Need to register?");
        toggleButton.setStyle("-fx-background-color: transparent; " +
                                      "-fx-text-fill: #4CAF50; " +
                                      "-fx-underline: true;");
        toggleButton.setOnAction(e -> toggleMode(formTitle, actionButton, toggleButton));

        // Add components to form
        formContainer.getChildren().addAll(
                formTitle, displayNameField, usernameField, comfirmPasswordField, passwordField,
                messageLabel, actionButton, toggleButton
        );

        root.getChildren().addAll(titleLabel, formContainer);

        getContentRoot().getChildren().add(root);
    }

    private void toggleMode(Label formTitle, Button actionButton, Button toggleButton) {
        isRegisterMode = !isRegisterMode;

        if (isRegisterMode) {
            formTitle.setText("Register");
            actionButton.setText("Register");
            toggleButton.setText("Already have an account?");
            comfirmPasswordField.setVisible(true);
            comfirmPasswordField.setManaged(true);
            displayNameField.setVisible(true);
            displayNameField.setManaged(true);
        } else {
            formTitle.setText("Login");
            actionButton.setText("Login");
            toggleButton.setText("Need to register?");
            comfirmPasswordField.setVisible(false);
            comfirmPasswordField.setManaged(false);
            displayNameField.setVisible(false);
            displayNameField.setManaged(false);
        }

        clearMessage();
    }

    private void handleAction() {
        String displayName = displayNameField.getText().trim();
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = comfirmPasswordField.getText();

        if (username.isEmpty() ||
                password.isEmpty() ||
                (isRegisterMode && (confirmPassword.isEmpty() || displayName.isEmpty()))) {

            showMessage("Please fill in all fields", Color.RED);
            return;
        }

        if (isRegisterMode && !password.equals(confirmPassword)) {
            showMessage("Password and Confirm password are not the same", Color.RED);
            return;
        }

        try {
            User user;
            if (isRegisterMode) {
                user = User.register(displayName, username, password, confirmPassword); // todo
                showMessage("Registration successful! Welcome " + username, Color.GREEN);
            } else {
                user = User.login(username, password);
                showMessage("Login successful! Welcome back " + username, Color.GREEN);
            }

            FXGL.runOnce(() -> {
                FXGL.getSceneService().popSubScene();
//                    ((TheThrone) FXGL.getApp()).showMainMenu(user); todo
            }, javafx.util.Duration.seconds(1));

        } catch (AuthenticationException e) {
            showMessage("Wrong credentials", Color.RED);
        }
    }

    private void showMessage(String message, Color color) {
        messageLabel.setText(message);
        messageLabel.setTextFill(color);
    }

    private void clearMessage() {
        messageLabel.setText("");
    }
}