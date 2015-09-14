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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author sado0726
 */
public class ROSearchCriteriaController implements Initializable {

    @FXML TableColumn restIDCol;
    @FXML TableColumn restNameCol;
    @FXML TableColumn addressCol;
    @FXML TableView bigTable;
    @FXML Label textLabel;

    private ObservableList<Restaurant> data;
    private static int rid;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        data = FXCollections.observableArrayList();
        assert bigTable != null;
        restIDCol.setCellValueFactory(new PropertyValueFactory<>("rid"));
        restNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address")); 
        
        try (
            Connection con = DriverManager.getConnection(
                        CS4400HealthInspection.MYSQL, 
                        CS4400HealthInspection.USERNAME, 
                        CS4400HealthInspection.PW);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT rid, name, street, city, state, zipcode "
                    + "FROM restaurant WHERE email IN (SELECT email "
                    + "FROM operatorowner "
                    + "WHERE username=\"" + CS4400HealthInspection.getSessionUser() + "\")");
                ) {
            while(rs.next()) {
                int rid = rs.getInt("rid");
                String name = rs.getString("name");
                String address = rs.getString("street") + " " + rs.getString("city") 
                        + " " + rs.getString("state") + " " + rs.getString("zipcode");
                Restaurant rest = new Restaurant(rid, name, address);
                data.add(rest);
            }
            bigTable.setItems(data);
        } catch(SQLException e) {
            System.err.println(e);
        }
    } 
    
    @FXML
    private void onReturnEvent(MouseEvent event) {
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
        if(bigTable.getSelectionModel().isEmpty()) {
            textLabel.setText("please make a selection");
        } else {    
            rid = ((Restaurant) bigTable.getSelectionModel().getSelectedItem()).ridProperty().get();
            try {
                Stage stage = CS4400HealthInspection.getStage();
                Parent foster = FXMLLoader.load(getClass().getResource("ROInspectionResult.fxml"));
                Scene scene = new Scene(foster);
                stage.setScene(scene);
                stage.show();            
            } catch (IOException e) {
                System.err.println(e);
            }
        }
    }
    
    public static int getRid() {
        return rid;
    }
}
