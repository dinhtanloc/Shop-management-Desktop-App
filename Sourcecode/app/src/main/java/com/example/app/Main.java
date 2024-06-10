package com.example.app;

import com.example.app.controllers.LoginWindowController;
import com.example.app.utils.AESCryptoprocessor;
import com.example.app.utils.MySQLConnector;
import com.example.app.utils.Storing;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        String storedUsername = Storing.getValueToPreferences("username");
        String storedPassword = Storing.getValueToPreferences("password");

        System.out.println(storedUsername + "-" + storedPassword);

        AESCryptoprocessor aesCryptoprocessor = new AESCryptoprocessor();
        storedPassword = aesCryptoprocessor.decrypt(storedPassword);
        MySQLConnector connector = MySQLConnector.getInstance();

        System.out.println(storedUsername + "-" + storedPassword);
        LoginWindowController login =new LoginWindowController();
        if(login.login(storedUsername,storedPassword)){
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("main-window.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1080, 680);
            stage.setTitle("MainWindow!");
            stage.setScene(scene);
            stage.show();
        }

        else {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login-window.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1080, 680);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();}
    }

    public static void main(String[] args) {
        launch();
    }
}