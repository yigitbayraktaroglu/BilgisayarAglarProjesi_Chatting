package com.example.messenger;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private AnchorPane ap_main;
    @FXML
    private Button button_gonder;
    @FXML
    private TextField tf_mesaj;
    @FXML
    private VBox vbox_mesaj;
    @FXML
    private ScrollPane sp_main;
    private Server server;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            server = new Server(new ServerSocket(1234));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("error");
        }
        vbox_mesaj.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                sp_main.setVvalue((Double) newValue);
            }
        });
        // bu kisim kendi yazdgımız mesajları ekrana ekler
        server.receiveMessageFromClient(vbox_mesaj);
        button_gonder.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String messageToSend = tf_mesaj.getText();
                if (!messageToSend.isEmpty()) {
                    HBox hBox = new HBox();//baloncuk olustur
                    hBox.setAlignment(Pos.CENTER_RIGHT);
                    hBox.setPadding(new Insets(5, 5, 5, 10));
                    Text text = new Text(messageToSend);
                    TextFlow textFlow = new TextFlow(text);
                    textFlow.setStyle("-fx-color: rgb(239,242,255);-fx-background-color: rgb(37,211,102);-fx-background-radius:20px");
                    textFlow.setPadding(new Insets(5, 10, 5, 10));

                    hBox.getChildren().add(textFlow);//baloncuga messageToSened i aktarır
                    vbox_mesaj.getChildren().add(hBox);
                    server.sendMessageToClient(messageToSend);
                    tf_mesaj.clear();
                }
            }
        });
    }
    // bu kisim client kısmından gelen mesajları ekrana ekler
    public static void addLabel(String messageFromClient, VBox vbox) {
        HBox hBox = new HBox();//mesaj baloncugunu olusturur
        hBox.setAlignment(Pos.CENTER_LEFT);//mesaj baloncugunu sola yatırır
        hBox.setPadding(new Insets(5, 5, 5, 10));
        Text text = new Text(messageFromClient);
        TextFlow textFlow = new TextFlow(text);
        textFlow.setStyle("-fx-background-color: rgb(233,233,235);-fx-background-radius:20px");
        textFlow.setPadding(new Insets(5, 10, 5, 10));
        hBox.getChildren().add(textFlow);//baloncugun icine mesaji ekler
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                vbox.getChildren().add(hBox);
            }
        });

    }
}