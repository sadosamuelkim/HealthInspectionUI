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
import java.util.HashSet;
import java.util.regex.Pattern;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;

import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;




/**
 * FXML Controller class
 *
 * @author Roberto
 */
public class CustSearchController implements Initializable {
    
    @FXML ComboBox CuisineBox;
    @FXML ComboBox ScoreBox;
    @FXML TextField restName;    
    @FXML TextField restScore;
    @FXML TextField restZip;
    @FXML Label textLabel;
    
    private String name;
    private String address;
    private String cuisine;
    private int score;
    private String date;
    
    private static HashSet<Restaurant> restaurants;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        CuisineBox.getItems().addAll("American", "Chinese", "French", "Greek",
                "Indian", "Italian", "Japanese", "Korean",
                "Mexican", "Thai");
        ScoreBox.getItems().addAll(">", "<");
        restaurants = new HashSet<>();
    }
    
    @FXML
    private void onSubmitEvent(MouseEvent event) {
        String cuisineBox = (CuisineBox.getValue() == null) ? "" : (String) CuisineBox.getValue();
        String scoreBox = (ScoreBox.getValue() == null) ? "" : (String) ScoreBox.getValue();
        String nameText = restName.getText();
        String scoreText = restScore.getText();
        String zipText = restZip.getText();
        if(scoreText.equals("") || zipText.equals("")) {
            textLabel.setText("both score and zipcode required");  
        } else if(scoreBox.equals("")){
            textLabel.setText("choose > or <");
        } else if (!Pattern.matches("1?\\d?\\d",scoreText)) {
            textLabel.setText("Invalid score: must be between 0 to 100");
        } else if (!Pattern.matches("\\d{5}", zipText)) {
            textLabel.setText("Invalid zipcode: please put 5 digit zipcode");
        } else {
            try (
                Connection con = DriverManager.getConnection(
                        CS4400HealthInspection.MYSQL, 
                        CS4400HealthInspection.USERNAME, 
                        CS4400HealthInspection.PW);
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT r.name, r.street, r.city,"
                        + " r.state, r.zipcode, r.cuisine, i.idate, i.totalscore "
                        + "FROM restaurant r LEFT JOIN inspection i on r.rid=i.rid "
                        + "WHERE idate = (Select MAX(idate) FROM inspection i2 "
                        + "WHERE i2.rid = r.rid) "
                        + "AND zipcode =" + zipText + " "
                        + "AND totalscore" 
                        +  scoreBox + scoreText 
                        + " AND cuisine LIKE (\"%" + cuisineBox 
                        +"\") AND name LIKE (\"%" + nameText 
                        + "\") GROUP BY name");
            ) { 
                while(rs.next()) {
                    name = rs.getString("name");
                    address = rs.getString("street") + ", " 
                            + rs.getString("city") + ", " 
                            + rs.getString("state") + " " 
                            + rs.getString("zipcode");
                    cuisine = rs.getString("cuisine");
                    score = rs.getInt("totalscore");
                    date = rs.getString("idate");
                    Restaurant rest = new Restaurant(name, address, cuisine, score, date);
                    restaurants.add(rest);
                }
                try {
                    Stage stage = CS4400HealthInspection.getStage();
                    Parent foster = FXMLLoader.load(getClass().getResource("CustSearchResult.fxml"));
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
    
    @FXML
    private void onReturnEvent(MouseEvent event) {
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
               
    public static HashSet<Restaurant> getRestaurantHashSet() {
        return restaurants;
    }
 }    
    
