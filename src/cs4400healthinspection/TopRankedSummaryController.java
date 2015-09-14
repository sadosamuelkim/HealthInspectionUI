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
import java.util.regex.Pattern;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Roberto
 */
public class TopRankedSummaryController implements Initializable {

    @FXML TextField year;
    @FXML TextField county;
    @FXML TableColumn cuisineCol;
    @FXML TableColumn restaurantCol;
    @FXML TableColumn addressCol;
    @FXML TableColumn scoreCol;
    @FXML TableView bigTable;
    @FXML Label textLabel;
    
    private ObservableList<ReportData> data;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        data = FXCollections.observableArrayList();
        assert bigTable != null;
        cuisineCol.setCellValueFactory(new PropertyValueFactory<>("cuisine"));
        restaurantCol.setCellValueFactory(new PropertyValueFactory<>("restaurant"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        scoreCol.setCellValueFactory(new PropertyValueFactory<>("score"));
    } 
    
    @FXML
    private void onSubmitEvent(MouseEvent event) {
        String yearText = year.getText();
        String countyText = county.getText();
        if(!Pattern.matches("\\d\\d\\d\\d", yearText)) {
            textLabel.setText("Invalid year: please put 4 digits e.g. 1992");
        } else if(countyText.isEmpty()) {
            textLabel.setText("Invalid county: cannot have empty county");
        } else {
            data.clear();
            try (
                Connection con = DriverManager.getConnection(
                            CS4400HealthInspection.MYSQL, 
                            CS4400HealthInspection.USERNAME, 
                            CS4400HealthInspection.PW);
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT r.cuisine, r.name, "
                        + "r.street,r.city, r.state, r.zipcode, MAX(i.totalscore) "
                        + "FROM restaurant r JOIN inspection i on r.rid = i.rid "
                        + "WHERE idate like(\"" + year.getText() + "-__-__\") "
                        + "AND county=\"" + county.getText() + "\" "
                        + "GROUP BY cuisine ORDER BY MAX(i.totalscore) ASC");
            ){

                while(rs.next()) {
                    String cuisine = rs.getString("cuisine");
                    String name = rs.getString("name");
                    String address = rs.getString("street") + ", " + rs.getString("city") 
                            + ", " + rs.getString("state") + " " + rs.getInt("zipcode");
                    int totalscore = rs.getInt("MAX(i.totalscore)");
                    ReportData rd = new ReportData(cuisine, name, address, totalscore);
                    data.add(rd);

                }
            } catch (SQLException e) {
                System.out.println(e);
            }
            bigTable.setItems(data);
        }
    }
    
    @FXML
    private void onReturnEvent(MouseEvent event) {
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
    
    /**
     * A class that carries all the values to be entered into the table for 
     * this controller. The name, the cuisine and the address of the restaurant 
     * along with the score of the given year divided in to county 
     */    
    public class ReportData {
        private String cuisine;
        private String restaurant;
        private String address;
        private int score;
        
        public ReportData(String cuisine, String restaurant, String address, int score) {
            this.cuisine = cuisine;
            this.restaurant = restaurant;            
            this.address = address;
            this.score = score;
        }
        
        public String getCuisine() {
            return cuisine;
        }

        public String getRestaurant() {
            return restaurant;
        }
        
        public String getAddress() {
            return address;
        }
        
        public int getScore() {
            return score;
        }
        
        public void setCuisine(String cuisine) {
            this.cuisine = cuisine;
        }
        
        public void setRestaurant(String restaurant) {
            this.restaurant = restaurant;
        }
        
        public void setAddress(String address) {
            this.address = address;
        }
        
        public void setScore(int score) {
            this.score = score;
        }
        
        @Override
        public String toString() {
            return  cuisine + " " + restaurant + " " + address + " " + score;
        }
    }
}
