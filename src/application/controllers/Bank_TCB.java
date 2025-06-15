package application.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.TextField;

import java.io.IOException;

import application.utils.JSONUtility;
import application.utils.JSONUtility.MovieData;
import javafx.event.ActionEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.Label;

public class Bank_TCB {

	private Stage stage;
	private Scene scene;
	private Parent root;

	@FXML
	private AnchorPane childcomponent;

	@FXML
	private Button Cancelbutton;

	@FXML
	private Pane parentcomponent;

	@FXML
	private Button Paybutton;

	@FXML
	private Label MoviePrice;

	@FXML
	private Text Validity;

	@FXML
	private TextField CreditCardInput;

	@FXML private TextField StartDateinput;
	@FXML private TextField TCBInput;

	public void initialize() {
		Validity.setVisible(false);
		getMoviePrice();
		childcomponent.layoutXProperty()
				.bind(parentcomponent.widthProperty().subtract(childcomponent.widthProperty()).divide(2));
		childcomponent.layoutYProperty()
				.bind(parentcomponent.heightProperty().subtract(childcomponent.heightProperty()).divide(2));
	}

	public void getMoviePrice() {
		JSONUtility json = new JSONUtility();
		MovieData movieData = json.getMovieJson();
		String price = String.format("%,d VNĐ", movieData.price);
		MoviePrice.setText(price);
	}

	@FXML
	void HandleCancelButton(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("/application/fxml/SelectPayment.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		double currentWidth = stage.getWidth();
		double currentHeight = stage.getHeight();
		scene = new Scene(root, currentWidth, currentHeight);

		stage.setMaximized(true);
		stage.setScene(scene);
		stage.show();
	}

	@FXML
	void PaybuttonHandler(ActionEvent event) throws IOException {
		if (CreditCardInput.getLength() != 19 ||
			StartDateinput.getLength() != 5 ||
			TCBInput.getText().trim().isEmpty()) {
			Validity.setVisible(true);
		} else {
			Validity.setVisible(false);
			root = FXMLLoader.load(getClass().getResource("/application/fxml/Booked.fxml"));
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			double currentWidth = stage.getWidth();
			double currentHeight = stage.getHeight();
			scene = new Scene(root, currentWidth, currentHeight);

			stage.setMaximized(true);
			stage.setScene(scene);
			stage.show();
		}
	}

	@FXML
	void CreditInputHandler(KeyEvent event) {
		if (CreditCardInput.getLength() % 5 == 0 && CreditCardInput.getLength() < 19) {
			CreditCardInput.appendText(" ");
		}
		if (CreditCardInput.getText().matches(".*[A-Za-z].*")) {
			System.out.println("Invalid character in card number");
			CreditCardInput.deletePreviousChar();
		}
		if (CreditCardInput.getLength() == 19) {
			StartDateinput.requestFocus();
		}
	}

	@FXML
	void StartDateInputHandler(KeyEvent event) {
		if (StartDateinput == null || TCBInput == null) return;

		String text = StartDateinput.getText();

		if (text.length() == 2 && !text.contains("/")) {
			StartDateinput.setText(text + "/");
			StartDateinput.positionCaret(3); 
			event.consume(); 
			return;
		}

		if (text.length() >= 5 && !event.getCode().toString().equals("BACK_SPACE")) {
			event.consume();
			return;
		}

		if (text.length() == 5) {
			TCBInput.requestFocus();
		}
	}

	@FXML
	void NameInputHandler(KeyEvent event) {
		if (TCBInput == null) return; 

		if (TCBInput.getLength() >= 30 && !(event.getCode().toString().equals("BACK_SPACE"))) {
			event.consume();
		}
		if (TCBInput.getLength() >= 30) {
			Paybutton.requestFocus();
		}
	}

	@FXML
	void Handleexit(MouseEvent event) {
		Paybutton.setOnMouseExited(event1 -> Paybutton.setStyle("-fx-scale-x: 1;"));
		Cancelbutton.setOnMouseExited(event1 -> Cancelbutton.setStyle("-fx-scale-x: 1;"));
	}

	@FXML
	void Handlehover(MouseEvent event) {
		Paybutton.setOnMouseEntered(event1 -> Paybutton.setStyle("-fx-scale-x: 1.1;"));
		Cancelbutton.setOnMouseEntered(event1 -> Cancelbutton.setStyle("-fx-scale-x: 1.1;"));
	}
}
