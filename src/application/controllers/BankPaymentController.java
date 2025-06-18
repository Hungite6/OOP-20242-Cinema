package application.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

public class BankPaymentController {

	private Stage stage;
	private Scene scene;
	private Parent root;
	private String bankName; // Lưu tên ngân hàng được chọn

	@FXML private AnchorPane childcomponent;
	@FXML private Button Cancelbutton;
	@FXML private Pane parentcomponent;
	@FXML private Button Paybutton;
	@FXML private Label MoviePrice;
	@FXML private Text Validity;
	@FXML private TextField CreditCardInput;
	@FXML private TextField StartDateinput;
	@FXML private TextField AGRBInput;
	@FXML private ImageView bankIcon; // ImageView để hiển thị biểu tượng ngân hàng

	// Hàm để thiết lập ngân hàng được chọn
	public void setBank(String bankName) {
		this.bankName = bankName;
		updateBankIcon();
	}

	// Cập nhật biểu tượng ngân hàng
	private void updateBankIcon() {
		try {
			String iconPath = "/application/resources/bank_icons/" + bankName.toLowerCase() + ".png";
			Image image = new Image(getClass().getResourceAsStream(iconPath));
			bankIcon.setImage(image);
		} catch (Exception e) {
			System.out.println("Không tìm thấy biểu tượng cho ngân hàng: " + bankName);
		}
	}

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
				AGRBInput.getText().trim().isEmpty()) {
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
			System.out.println("Ký tự không hợp lệ trong số thẻ");
			CreditCardInput.deletePreviousChar();
		}
		if (CreditCardInput.getLength() == 19) {
			StartDateinput.requestFocus();
		}
	}

	@FXML
	void StartDateInputHandler(KeyEvent event) {
		if (StartDateinput == null || AGRBInput == null) return;

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
			AGRBInput.requestFocus();
		}
	}

	@FXML
	void NameInputHandler(KeyEvent event) {
		if (AGRBInput == null) return;

		if (AGRBInput.getLength() >= 30 && !(event.getCode().toString().equals("BACK_SPACE"))) {
			event.consume();
		}
		if (AGRBInput.getLength() >= 30) {
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