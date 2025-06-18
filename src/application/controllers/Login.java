package application.controllers;

import java.io.IOException;

import application.utils.DBUtility;
import application.utils.Form;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.InputEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Login {
	private Stage stage;
	private Scene scene;
	private Parent root;

	@FXML
	private AnchorPane loginFormContainer;

	@FXML
	private Label errorEmailAddress;

	@FXML
	private Label errorPassword;

	@FXML
	private Label errorLoginMessage;

	@FXML
	private TextField inputLoginEmailField;

	@FXML
	private PasswordField inputLoginPasswordField;

	@FXML
	private Button btnLogin;

	// hide the error message on input Typing event
	@FXML
	public void resetErrorMessage(InputEvent event) throws Exception {
		errorEmailAddress.setVisible(false);
		errorPassword.setVisible(false);
		errorLoginMessage.setVisible(false);
	}

	// validate Email and Password, check in DB, store in userdata.json
	@FXML
	public void login(ActionEvent event) throws IOException {
		String emailAddress = inputLoginEmailField.getText();
		String password = inputLoginPasswordField.getText();

		// Validate email
		Object[] emailValidationResult = Form.validateEmail(emailAddress);
		boolean isEmailValid = (boolean) emailValidationResult[0];
		String emailErrorMessage = (String) emailValidationResult[1];

		// Validate password
		Object[] passwordValidationResult = Form.validatePassword(password);
		boolean isPasswordValid = (boolean) passwordValidationResult[0];
		String passwordErrorMessage = (String) passwordValidationResult[1];

		// check for the credentials in DB
		if (isEmailValid && isPasswordValid) {
			// hide the Error message Labels
			errorEmailAddress.setVisible(false);
			errorPassword.setVisible(false);

			// SQL Query
			Object[] loginResult = DBUtility.validateLogin(emailAddress, password);
			boolean isCredentialsValid = (boolean) loginResult[0];
			boolean isSuperUser = ((Number) loginResult[1]).intValue() == 1;

			// Credentials valid - redirect to appropriate dashboard
			if (isCredentialsValid) {
				errorLoginMessage.setVisible(false);

				String fxmlPath = isSuperUser ? "/manager/view/DashboardAdmin.fxml" : "/application/fxml/Dashboard.fxml";
				root = FXMLLoader.load(getClass().getResource(fxmlPath));
				stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
				double currentWidth = stage.getWidth();
				double currentHeight = stage.getHeight();
				scene = new Scene(root, currentWidth, currentHeight);

				stage.setScene(scene);
				stage.show();
			} else {
				// Credentials invalid - show error message
				errorLoginMessage.setVisible(true);
				errorLoginMessage.setText("Invalid Email or Password.");
			}
		} else {
			// Show error message labels and update texts
			errorEmailAddress.setVisible(true);
			errorPassword.setVisible(true);
			errorEmailAddress.setText(emailErrorMessage);
			errorPassword.setText(passwordErrorMessage);
		}
	}

	// move to sign up screen
	@FXML
	public void signUp(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("/application/fxml/SignUp.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		double currentWidth = stage.getWidth();
		double currentHeight = stage.getHeight();
		scene = new Scene(root, currentWidth, currentHeight);

		stage.setScene(scene);
		stage.show();
	}

	// move to forgot password screen
	@FXML
	public void forgetPassword(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("/application/fxml/ForgotPassword.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		double currentWidth = stage.getWidth();
		double currentHeight = stage.getHeight();
		scene = new Scene(root, currentWidth, currentHeight);

		stage.setMaximized(true);
		stage.setScene(scene);
		stage.show();
	}

}
