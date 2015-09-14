/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs4400healthinspection;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author sado0726
 */
public class LoginController implements Initializable {

    @FXML TextField usernameInput;
    @FXML PasswordField passwordInput;
    @FXML Label textLabel;
    private String username;
    private String password;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        CS4400HealthInspection.setSessionUser(null);
        CS4400HealthInspection.setSessionPassword(null);
        CS4400HealthInspection.setUserType(CS4400HealthInspection.User.NOTAUSER);
    }
    
    @FXML
    private void onGuestEvent(MouseEvent event) {
        try {
            Stage stage = CS4400HealthInspection.getStage();
            Parent foster = FXMLLoader.load(getClass().getResource("CustMenu.fxml"));

            Scene scene = new Scene(foster);

            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            System.err.println(e);
        }
    }
    
    @FXML
    private void onUserEvent(MouseEvent event) {
        username = usernameInput.getText();
        password = passwordInput.getText();
        if(username.isEmpty() || password.isEmpty()) {
            textLabel.setText("Empty username or password");
        } else {
            try (
                
                Connection con = DriverManager.getConnection(
                            CS4400HealthInspection.MYSQL, 
                            CS4400HealthInspection.USERNAME, 
                            CS4400HealthInspection.PW);
                Statement stmt1 = con.createStatement();
                ResultSet users1 = stmt1.executeQuery("SELECT r.username, r.password "
                        + "FROM registereduser AS r, operatorowner AS i "
                        + "WHERE r.username = i.username");
                Statement stmt2 = con.createStatement();
                ResultSet users2 = stmt2.executeQuery("SELECT r.username, r.password "
                        + "FROM registereduser AS r, inspector AS i "
                        + "WHERE r.username = i.username");
            ) {    
                while (users1.next()) {
                    String u1 = users1.getString("username");
                    String p1 = users1.getString("password");
                    if (username.equals(u1) && password.equals(p1)) {
                        CS4400HealthInspection.setUserType(CS4400HealthInspection.User.RESTOP);
                    } 
                }
                
                while (users2.next()) {
                    String u2 = users2.getString("username");
                    String p2 = users2.getString("password");
                    if (username.equals(u2) && password.equals(p2)) {
                        CS4400HealthInspection.setUserType(CS4400HealthInspection.User.INSPECTOR);
                    } 
                }
                
                CS4400HealthInspection.setSessionUser(username);
                CS4400HealthInspection.setSessionPassword(password);
                
                if(CS4400HealthInspection.getUserType() == CS4400HealthInspection.User.RESTOP) {
                    try {
                        Stage stage = CS4400HealthInspection.getStage();
                        Parent foster = FXMLLoader.load(getClass().getResource("ROMenu.fxml"));
                        Scene scene = new Scene(foster);
                        stage.setScene(scene);
                        stage.show();
                    } catch (IOException e) {
                        System.err.println(e);
                    }
                } else if (CS4400HealthInspection.getUserType() == CS4400HealthInspection.User.INSPECTOR) {
                    try {
                        Stage stage = CS4400HealthInspection.getStage();
                        Parent foster = FXMLLoader.load(getClass().getResource("InspMenu.fxml"));
                        Scene scene = new Scene(foster);
                        stage.setScene(scene);
                        stage.show();
                    } catch (IOException e) {
                        System.err.println(e);
                    }
                } else {
                    textLabel.setText("Wrong username/password. Try again.");
                }
            } catch(SQLException e) {
                System.err.println(e);
            }
        }
    }
}
