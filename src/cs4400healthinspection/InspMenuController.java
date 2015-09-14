/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs4400healthinspection;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Roberto
 */
public class InspMenuController implements Initializable {

    @FXML Label welcomeLabel;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        assert CS4400HealthInspection.getUserType() == CS4400HealthInspection.User.INSPECTOR;
        welcomeLabel.setText("welcome " + CS4400HealthInspection.getSessionUser());
    }
    
    @FXML
    private void onReturnEvent(MouseEvent event) {
        CS4400HealthInspection.setSessionUser(null);
        CS4400HealthInspection.setSessionPassword(null); 
        try {
            Stage stage = CS4400HealthInspection.getStage();
            Parent foster = FXMLLoader.load(getClass().getResource("Login.fxml"));
            Scene scene = new Scene(foster);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
          System.err.println(e);
        }
    }
    
    @FXML
    private void onSummaryEvent(MouseEvent event) {
        try {
            Stage stage = CS4400HealthInspection.getStage();
            Parent foster = FXMLLoader.load(getClass().getResource("InspReportMenu.fxml"));
            Scene scene = new Scene(foster);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println(e);
        }
    }
    
    @FXML
    private void onInsertEvent(MouseEvent event) {
        try {
            Stage stage = CS4400HealthInspection.getStage();
            Parent foster = FXMLLoader.load(getClass().getResource("InspInsertInspection.fxml"));
            Scene scene = new Scene(foster);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println(e);
        }
    }
    
}
