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
import java.util.HashSet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Roberto
 */
public class CustSearchResultController implements Initializable {

    @FXML TableView bigTable;
    @FXML TableColumn nameCol;
    @FXML TableColumn addressCol;
    @FXML TableColumn cuisineCol;
    @FXML TableColumn scoreCol;
    @FXML TableColumn dateCol;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        HashSet<Restaurant> restaurants = CustSearchController.getRestaurantHashSet();
        ObservableList<Restaurant> data = FXCollections.observableArrayList(restaurants);
        
        assert bigTable != null;
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        cuisineCol.setCellValueFactory(new PropertyValueFactory<>("cuisine"));
        scoreCol.setCellValueFactory(new PropertyValueFactory<>("inspScore"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("inspDate"));
        
        bigTable.setItems(data);     
    }  
    
    @FXML
    private void onReturnEvent(MouseEvent event) {
        try {
            Stage stage = CS4400HealthInspection.getStage();
            Parent foster = FXMLLoader.load(getClass().getResource("CustSearch.fxml"));
            Scene scene = new Scene(foster);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println(e);
        }
    }
    
}
