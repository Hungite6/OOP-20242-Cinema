package application.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

@SuppressWarnings("unused")
public class BankSelection {

    private void switchScene(MouseEvent event, String fxmlPath) {
        try {
            Parent newRoot = FXMLLoader.load(getClass().getResource(fxmlPath));
            Scene newScene = new Scene(newRoot);
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(newScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleVTBClick(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/fxml/BankPayment.fxml"));
        Parent root = loader.load();
        BankPaymentController controller = loader.getController();
        controller.setBank("vtb"); // Truyền tên ngân hàng
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void handleVCBClick(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/fxml/BankPayment.fxml"));
        Parent root = loader.load();
        BankPaymentController controller = loader.getController();
        controller.setBank("vcb"); // Truyền tên ngân hàng
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void handleAGRBClick(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/fxml/BankPayment.fxml"));
        Parent root = loader.load();
        BankPaymentController controller = loader.getController();
        controller.setBank("agb"); // Truyền tên ngân hàng
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void handleTCBClick(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/fxml/BankPayment.fxml"));
        Parent root = loader.load();
        BankPaymentController controller = loader.getController();
        controller.setBank("tcb"); // Truyền tên ngân hàng
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void handleBIDVClick(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/fxml/BankPayment.fxml"));
        Parent root = loader.load();
        BankPaymentController controller = loader.getController();
        controller.setBank("bidv"); // Truyền tên ngân hàng
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void handleMBClick(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/fxml/BankPayment.fxml"));
        Parent root = loader.load();
        BankPaymentController controller = loader.getController();
        controller.setBank("mb"); // Truyền tên ngân hàng
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
