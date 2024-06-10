package com.example.app.controllers;

import com.example.app.Main;
import com.example.app.utils.AESCryptoprocessor;
import com.example.app.utils.MySQLConnector;
import com.example.app.utils.Storing;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginWindowController implements Initializable {
    @FXML
    private Button loginBtn;
    @FXML
    private TextField usninput;
    @FXML
    private PasswordField pssinput;
    @FXML
    private Label errorText;
    @FXML
    private CheckBox saveBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        errorText.setText("");
        loginBtn.setOnAction(event -> {

            String username = usninput.getText();
            String password = pssinput.getText();
            savePassword(username, password);

//




            //neu connect thanh cong
            if (login(username,password)){
                //CHuyen sang man hinh chinh
                handleLogin();
                try{
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("main-window.fxml"));
                    Scene scene = new Scene(fxmlLoader.load(), 1080, 680);
                    stage.setTitle("MainWindow");
                    stage.setScene(scene);
                }catch (Exception e){
                    System.out.println(e);
                }
            }
            else {
                errorText.setText("Sai tài khoản hoặc mật khẩu");
            }




        });
        autoFillCredentials();


    }
    void savePassword(String username,String password){
        Storing.putValueToPreferences("username",username);
        AESCryptoprocessor aesCryptoprocessor = new AESCryptoprocessor();


        String encryptedPassword = aesCryptoprocessor.encrypt(password);
        Storing.putValueToPreferences("password",encryptedPassword);
        System.out.println("username"+username);
        System.out.println("pass"+encryptedPassword);

    }
    public boolean login(String username,String password){
        if ((username.equals("locdinh.myshop@gmail.com") || username.equals("tuananh.myshop@gmail.com")) && (password.equals("0123456789"))){
            MySQLConnector mySQLConnector = MySQLConnector.getInstance();
            return mySQLConnector.Connect();
        }
        else{
            return false;
        }
    }

    private void handleLogin() {
        // Your actual login logic here
        if (saveBox.isSelected()) {
            AESCryptoprocessor aesCryptoprocessor = new AESCryptoprocessor();
            String encryptedPassword = aesCryptoprocessor.encrypt(pssinput.getText());
            Storing.putValueToPreferences(usninput.getText(), encryptedPassword);
        }
        else{
            Storing.putValueToPreferences(usninput.getText(), "");


        }
    }

    private void autoFillCredentials() {
        Storing store=new Storing();
        usninput.setText(store.getUsername());
        String encryptedPassword = store.getEncryptedPassword(store.getUsername());
        if (!encryptedPassword.isEmpty()) {
            AESCryptoprocessor aesCryptoprocessor = new AESCryptoprocessor();
            pssinput.setText(aesCryptoprocessor.decrypt(encryptedPassword));
        }


    }
}
