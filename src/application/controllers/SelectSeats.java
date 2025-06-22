package application.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
//import javafx.stage.Screen;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

import application.utils.JSONUtility;
import application.utils.JSONUtility.MovieData;

public class SelectSeats implements Initializable {
	private Stage stage;
	private Scene scene;
	private Parent root;

	public JSONUtility util;

	public String selectedSeats[] = {};

	public String name;

	public int totalPrice = 0, basePrice = 0;

	@FXML
	// private GridPane selectSeatsWrap;
	private AnchorPane seatsPane;
	@FXML
	private ScrollPane scrollPane;
	@FXML
	private HBox premiumHbox, normalHbox, vipHbox;

	@FXML
	private Label premiumPrice, normalPrice, vipPrice;

	@FXML
	private Button proceedToPaymentBtn, cancelBtn;

	public SelectSeats() {
		this.util = new JSONUtility();
	}

	public void handleCancelBtnClick(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("/application/fxml/Dashboard.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		double currentWidth = stage.getWidth();
		double currentHeight = stage.getHeight();
		scene = new Scene(root, currentWidth, currentHeight);

		stage.setMaximized(true);
		stage.setScene(scene);
		stage.show();
	}

	public void handleProceedToPaymentPageClick(ActionEvent event) throws IOException {
		if(selectedSeats.length > 0){
			util.updateMovieJson(selectedSeats, totalPrice, basePrice, name);
			@SuppressWarnings("unused")
			MovieData moviedata = util.getMovieJson();
	
			root = FXMLLoader.load(getClass().getResource("/application/fxml/SelectPayment.fxml"));
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			double currentWidth = stage.getWidth();
			double currentHeight = stage.getHeight();
			scene = new Scene(root, currentWidth, currentHeight);
	
			stage.setMaximized(true);
			stage.setScene(scene);
			stage.show();
		}
	}

	public String getSeatCode(int num) {
		char[] chs = new char[10];
		char startChar = 'A';
		for (int i = 0; i < chs.length; i++) {
			chs[i] = (char) (startChar + i);
		}
		String st2 = Character.toString(chs[num % 10]);
		String str = Integer.toString(num / 10 + 1) + st2;
		return str;
	}

	public int getSeatNumber(String seatCode) {
		char[] chs = new char[10];
		char startChar = 'A';
		for (int i = 0; i < chs.length; i++) {
			chs[i] = (char) (startChar + i);
		}
		int seatCol = -1;
		for (int i = 0; i < chs.length; i++) {
			if (chs[i] == seatCode.toCharArray()[1]) {
				seatCol = i;
				break;
			}
		}
		int seatRow = (Integer.parseInt(seatCode.substring(0, 1)) - 1) * 10;
		int seatNum = seatRow + seatCol;
		return seatNum;
	}

	public boolean checkAvailability(String seat, String[] booked) {
		List<String> list = Arrays.asList(booked);
		boolean contains = list.contains(seat);
		return contains;
	}

	public int seatLevel(String str) {
		if (str.startsWith("1")) {
			return 2; // Premium (+70,000)
		} else if (str.startsWith("20")) {
			return 1; // VIP (+50,000)
		}
		return 0; // Base
	}

	public String[] getUpdatedSelection(String[] orgArr, int method, String el) {
		String[] newArr = {};
		// Addition of Seat
		if (method == 1) {
			newArr = Arrays.copyOf(orgArr, orgArr.length + 1);
			newArr[orgArr.length] = el;
		}
		// Removal of Seat
		if (method == 0) {
			for (int i = 0; i < orgArr.length; i++) {
				if (el != orgArr[i]) {
					newArr[i] = orgArr[i];
				}
			}
		}
		return newArr;
	}

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		// util = new JSONUtility();
		MovieData moviedata = util.getMovieJson();

		if (moviedata == null) {
			cancelBtn.fire();
		}
        basePrice = moviedata.basePrice;
		name = moviedata.name;

		premiumPrice.setText(String.format("%,d VNĐ", basePrice + 50000));
		normalPrice.setText(String.format("%,d VNĐ", basePrice));
		vipPrice.setText(String.format("%,d VNĐ", basePrice + 70000));

		// double paneWidth = scrollPane.getWidth();
		double paneWidth = Screen.getPrimary().getBounds().getWidth();
		seatsPane.setPrefWidth(paneWidth);
		GridPane selectSeatsWrap1 = new GridPane();
		// selectSeatsWrap1.setVgap(10);
		selectSeatsWrap1.setHgap(10);
		GridPane selectSeatsWrap2 = new GridPane();
		selectSeatsWrap2.setVgap(10);
		selectSeatsWrap2.setHgap(10);
		GridPane selectSeatsWrap3 = new GridPane();
		// selectSeatsWrap3.setVgap(10);
		selectSeatsWrap3.setHgap(10);

		for (int i = 199; i >= 0; i--) {
			boolean isBooked = false;
			String str = this.getSeatCode(i);
			Button btn = new Button();
			btn.setText(str);
			isBooked = this.checkAvailability(str, moviedata.bookedSeats);
			// if (i % 3 == 0) {
			// isBooked = true;
			// }
			if (isBooked) {
				btn.getStyleClass().add("booked-seats");
			} else {
				btn.getStyleClass().add("available-seats");
				btn.setOnAction(event -> handleSelection(event));
			}
			if (i >= 190) {
				selectSeatsWrap1.add(btn, i % 10, i / 10);
			} else if (i < 10) {
				selectSeatsWrap3.add(btn, i % 10, i / 10);
			} else {
				selectSeatsWrap2.add(btn, i % 10, 18 - i / 10);
			}
		}
		premiumHbox.setPadding(new Insets(10, 0, 50, 0));
		normalHbox.setPadding(new Insets(10, 0, 100, 0));
		vipHbox.setPadding(new Insets(10, 0, 100, 0));
		premiumHbox.getChildren().add(selectSeatsWrap1);
		normalHbox.getChildren().add(selectSeatsWrap2);
		vipHbox.getChildren().add(selectSeatsWrap3);
	}

	public void handleSelection(ActionEvent event) {
		Button btn = ((Button) event.getSource());
		String seat = btn.getText();
		boolean alreadySelected = Arrays.stream(selectedSeats).anyMatch(e -> e.equals(seat)); // Sử dụng equals thay ==
		if (alreadySelected) {
			// Bỏ chọn ghế
			selectedSeats = Arrays.stream(selectedSeats).filter(el -> !el.equals(seat)).toArray(String[]::new);
			btn.getStyleClass().remove("selected-seats");
		} else {
			// Chọn ghế mới
			selectedSeats = Arrays.copyOf(selectedSeats, selectedSeats.length + 1);
			selectedSeats[selectedSeats.length - 1] = seat;
			btn.getStyleClass().add("selected-seats");
		}

		// Tính lại totalPrice từ selectedSeats
		totalPrice = 0;
		for (String selectedSeat : selectedSeats) {
			int seatLevel = seatLevel(selectedSeat);  // Lấy cấp độ ghế
			int seatPrice;

			if (seatLevel == 2) {
				seatPrice = basePrice + 70000;
			} else if (seatLevel == 1) {
				seatPrice = basePrice + 50000;
			} else {
				seatPrice = basePrice;
			}

			totalPrice += seatPrice;
		}
	}

}
