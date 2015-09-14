/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs4400healthinspection;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Roberto
 */
public class CustComplaintPageController implements Initializable {

    @FXML TextField restaurantNameTextfield;
    @FXML TextField streetNameTextfield;
    @FXML TextField cityTextfield;
    @FXML TextField stateTextfield;
    @FXML TextField zipcodeTextfield;
    @FXML TextField dateTextfield;
    @FXML TextField firstNameTextfield;
    @FXML TextField lastNameTextfield;
    @FXML TextField customerPhoneTextfield;
    @FXML TextArea complaintTextArea;
    @FXML Label textLabel;
    
    private TextField[] textfieldArray;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
    @FXML
    private void onCancelEvent(MouseEvent event) {
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
    private void onSubmitEvent(MouseEvent event) throws SQLException {
        boolean accepted;
        String name = restaurantNameTextfield.getText();
        String street = streetNameTextfield.getText();
        String city = cityTextfield.getText();
        String state = stateTextfield.getText();
        String zipcode = zipcodeTextfield.getText();
        String date = dateTextfield.getText();
        String firstName = firstNameTextfield.getText();
        String lastName = lastNameTextfield.getText();
        String customerPhone = customerPhoneTextfield.getText();
        String complaint = complaintTextArea.getText();
        if (name.isEmpty()) {
            textLabel.setText("Invalid restaurant name; cannot be empty");
        } else if (street.isEmpty()) {
            textLabel.setText("Invalid street name; cannot be empty");
        } else if (!Pattern.matches(".+",city)) {
            textLabel.setText("Invalid city name");
        } else if (!Pattern.matches("[a-zA-Z][a-zA-Z]",state)) {
            textLabel.setText("Invalid state, please put the state initial e.g AL for alabama");
        } else if (!Pattern.matches("\\d\\d\\d\\d\\d",zipcode)) {
            textLabel.setText("Invalid zipcode, please put 5 digit zipcode");
        } else if (!Pattern.matches("\\d\\d\\d\\d\\d\\d\\d\\d",date)) {
            textLabel.setText("Invalid year, please put date in this format YYYY-MM-DD");
        } else if (!Pattern.matches("[a-zA-Z]+",firstName)) {
            textLabel.setText("Invalid first name, only accept alphabetic characters");
        } else if (!Pattern.matches("[a-zA-Z]+",lastName)) {
            textLabel.setText("Invalid last name, only accept alphabetic characters");
        } else if (!Pattern.matches("\\d{10}",customerPhone)) {
            textLabel.setText("Invalid phone number, please put phone number in this format XXXXXXXXXX");
        } else if (complaint.isEmpty()) {
            textLabel.setText("Invalid input for complaint: cannot be empty");
        } else {
            try (
                Connection con = DriverManager.getConnection(
                        CS4400HealthInspection.MYSQL, 
                        CS4400HealthInspection.USERNAME, 
                        CS4400HealthInspection.PW);
                Statement stmt1 = con.createStatement();
                ResultSet rs = stmt1.executeQuery("SELECT rid FROM restaurant "
                        + "WHERE street=\"" + streetNameTextfield.getText() + "\" "
                        + "AND city=\"" + cityTextfield.getText() + "\" "
                        + "AND state=\"" + stateTextfield.getText() + "\" "
                        + "AND zipcode=\"" + zipcodeTextfield.getText() + "\"");
                Statement stmt2 = con.createStatement();
                //extra: check if date and phone is correctly formatt
                Statement stmt3 = con.createStatement();
            ) { 
                rs.next();
                Integer rid = rs.getInt("rid");
                stmt2.executeUpdate("INSERT INTO complaint(rid, cdate, phone, description)"
                        + " VALUES(" + rid + ", " + dateTextfield.getText() + ", "
                        + customerPhoneTextfield.getText() + ", \"" 
                        + complaintTextArea.getText() + "\")");
                stmt3.executeUpdate("INSERT INTO customer(phone, firstname, lastname)"
                        + " VALUES(" + customerPhoneTextfield.getText() + ", \"" 
                        + firstNameTextfield.getText() + "\", \"" 
                        + lastNameTextfield.getText() + "\")");
                accepted = true;
            } catch (SQLException e) {
                System.err.println(e);
                textLabel.setText("No match found. Please see if all the input is correct.");
                accepted = false;
            }
            
            if (accepted) {
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
        }
    }   
}
