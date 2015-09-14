/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs4400healthinspection;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Pattern;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javax.swing.JOptionPane;


/**
 * FXML Controller class
 *
 * @author Roberto
 */
public class RORestInputInfoController implements Initializable {
    
    @FXML ComboBox CuisineBox;
    @FXML TextField healthPermitID;
    @FXML TextField HPExpirationDate;
    @FXML TextField restNameTextfield;
    @FXML TextField restStreet;
    @FXML TextField restCity;
    @FXML TextField restState;
    @FXML TextField restZipcode;
    @FXML TextField restCounty;
    @FXML TextField restPhone;
    @FXML Label textLabel;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        CuisineBox.getItems().addAll("American", "Chinese", "French", "Greek",
                "Indian", "Italian", "Japanese", "Korean",
                "Mexican", "Thai");
    }  
    
    @FXML
    private void onCancelEvent(MouseEvent event) {
        try {
            Stage stage = CS4400HealthInspection.getStage();
            Parent foster = FXMLLoader.load(getClass().getResource("ROMenu.fxml"));
            Scene scene = new Scene(foster);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println(e);
        }
    }
    
    @FXML
    private void onSubmitEvent(MouseEvent event) {
        String cuisineBox = CuisineBox.getValue() == null ? "" : (String) CuisineBox.getValue();
        String healthPermitIDText = healthPermitID.getText();
        String HPExpirationDateText = HPExpirationDate.getText();
        String restName = restNameTextfield.getText();
        String restStreetText = restStreet.getText();
        String restCityText = restCity.getText();
        String restStateText = restState.getText();
        String restZipcodeText = restZipcode.getText();
        String restCountyText = restCounty.getText();
        String restPhoneText = restPhone.getText();
        if(!Pattern.matches("\\d*", healthPermitIDText)) { 
            textLabel.setText("invalid Health Permit ID: numbers only");
        } else if(!Pattern.matches("\\d{8}", HPExpirationDateText)) {
            textLabel.setText("invalid Health Permit Date: please use this format YYYYMMDD");
        } else if(restName.isEmpty()) { 
            textLabel.setText("invalid restaurant name: cannot be empty");
        } else if(restStreetText.isEmpty()) { 
            textLabel.setText("invalid street name: cannot be empty");
        } else if(restCityText.isEmpty()) { 
            textLabel.setText("invalid city name: cannot be empty");
        } else if(!Pattern.matches("[a-zA-Z][a-zA-Z]", restStateText)) { 
            textLabel.setText("Invalid state: please use state initial e.g. AL for Alabama");
        } else if(!Pattern.matches("\\d{5}", restZipcodeText)) {
            textLabel.setText("Invalid zipcode: please use only 5 digit zipcode");
        } else if(restCountyText.isEmpty()) {
            textLabel.setText("Invalid county: cannot be empty");
        } else if(!Pattern.matches("\\d{10}", restPhoneText)) {
            textLabel.setText("Invalid phone number: please this format XXXXXXXXXXX");
        } else if(cuisineBox.equals("")) {
            textLabel.setText("a cuisine is not chosen");
        } else {
            try (
                Connection con = DriverManager.getConnection(
                        CS4400HealthInspection.MYSQL, 
                        CS4400HealthInspection.USERNAME, 
                        CS4400HealthInspection.PW);
                Statement stmt1 = con.createStatement();
                ResultSet result1 = stmt1.executeQuery("SELECT MAX(rid)"
                        + "FROM restaurant");
                Statement stmt2 = con.createStatement();
                ResultSet result2 = stmt2.executeQuery("SELECT email "
                        + "FROM operatorowner "
                        + "WHERE username=\"" + CS4400HealthInspection.getSessionUser() + "\"");
                Statement stmt3 = con.createStatement();
                Statement stmt4 = con.createStatement();
            ) {
                result1.next();          
                Integer new_rid = result1.getInt("max(rid)") + 1;
                result2.next();
                String email = result2.getString("email");
                stmt3.executeUpdate("INSERT INTO restaurant(rid, phone, name, county, street, city, state, zipcode, cuisine, email) "
                        + "VALUES(" + new_rid + ", " 
                        + restPhoneText + ", \"" 
                        + restName + "\", \"" 
                        + restCountyText + "\", \"" 
                        + restStreetText + "\", \"" 
                        + restCityText + "\", \"" 
                        + restStateText + "\", " 
                        + restZipcodeText + ", \"" 
                        + cuisineBox + "\", \"" 
                        + email + "\")");
                stmt4.executeUpdate("INSERT INTO healthpermit(hpid, expirationdate, rid) "
                        + "VALUES(" + healthPermitIDText + ", " 
                        + HPExpirationDateText + ", " 
                        + new_rid + ")");
                JOptionPane.showMessageDialog(null, "your restaurant's ID is " + new_rid);

                try {
                    Stage stage = CS4400HealthInspection.getStage();
                    Parent foster = FXMLLoader.load(getClass().getResource("ROMenu.fxml"));
                    Scene scene = new Scene(foster);
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException e) {
                   System.err.println(e);
                }
            } catch (SQLException e) {
                System.err.println(e);
            }
        } 
    }
}
