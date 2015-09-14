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
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author sado0726
 */
public class InspInsertCommentController implements Initializable {

    @FXML TextField inspIDTextfield;
    @FXML TextField restIDTextfield;
    @FXML TextField dateTextfield;
    @FXML Label textLabel;
    @FXML TableView bigTable;
    @FXML TextField rowI1;
    @FXML TextField rowC1;
    @FXML TextField rowI2;
    @FXML TextField rowC2;
    @FXML TextField rowI3;
    @FXML TextField rowC3;
    @FXML TextField rowI4;
    @FXML TextField rowC4;
    @FXML TextField rowI5;
    @FXML TextField rowC5;
    @FXML TextField rowI6;
    @FXML TextField rowC6;
    @FXML TextField rowI7;
    @FXML TextField rowC7;
    @FXML TextField rowI8;
    @FXML TextField rowC8;
    @FXML TextField rowI9;
    @FXML TextField rowC9;
    @FXML TextField rowI10;
    @FXML TextField rowC10;
    @FXML TextField rowI11;
    @FXML TextField rowC11;
    @FXML TextField rowI12;
    @FXML TextField rowC12;
    @FXML TextField rowI13;
    @FXML TextField rowC13;
    @FXML TextField rowI14;
    @FXML TextField rowC14;
    @FXML TextField rowI15;
    @FXML TextField rowC15; 
    
    private TextField[] textfieldArray;
    private TextField[] textfieldArray2;

    boolean submitonce = false;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        textfieldArray = new TextField[15];
        textfieldArray[0] = rowI1;
        textfieldArray[1] = rowI2;
        textfieldArray[2] = rowI3;
        textfieldArray[3] = rowI4;
        textfieldArray[4] = rowI5;
        textfieldArray[5] = rowI6;
        textfieldArray[6] = rowI7;
        textfieldArray[7] = rowI8;
        textfieldArray[8] = rowI9;
        textfieldArray[9] = rowI10; 
        textfieldArray[10] = rowI11;
        textfieldArray[11] = rowI12;
        textfieldArray[12] = rowI13;
        textfieldArray[13] = rowI14;
        textfieldArray[14] = rowI15;
        
        textfieldArray2 = new TextField[15];
        textfieldArray2[0] = rowC1;
        textfieldArray2[1] = rowC2;
        textfieldArray2[2] = rowC3;
        textfieldArray2[3] = rowC4;
        textfieldArray2[4] = rowC5;
        textfieldArray2[5] = rowC6;
        textfieldArray2[6] = rowC7;
        textfieldArray2[7] = rowC8;
        textfieldArray2[8] = rowC9;
        textfieldArray2[9] = rowC10; 
        textfieldArray2[10] = rowC11;
        textfieldArray2[11] = rowC12;
        textfieldArray2[12] = rowC13;
        textfieldArray2[13] = rowC14;
        textfieldArray2[14] = rowC15;
    } 
 
    @FXML
    private void onSubmitEvent(MouseEvent event) { 
        if(!submitonce) {
            for( int i =0 ; i<textfieldArray.length; i++ ) {
                if(!textfieldArray[i].getText().equals("") || !textfieldArray2[i].getText().equals("")) {
                   try(Connection con = DriverManager.getConnection(
                            CS4400HealthInspection.MYSQL, 
                            CS4400HealthInspection.USERNAME, 
                            CS4400HealthInspection.PW);
                        Statement stmt = con.createStatement();
                    ){   
                        stmt.executeUpdate("INSERT INTO includes(itemnum, rid, idate, comment) "
                                    + "VALUES(" + textfieldArray[i].getText() + ", " 
                                    + restIDTextfield.getText() + ", \"" 
                                    + dateTextfield.getText() + "\", \"" 
                                    + textfieldArray2[i].getText() + "\")"); 
                    } catch (SQLException e){
                        System.err.println(e);
                    }
                }
            }
            submitonce = true;
            textLabel.setText("Comment insert complete");
        } else {
            textLabel.setText("Press button to the right to go back to the menu");
        }
    }
    
    @FXML
    private void onReturnEvent(MouseEvent event) {
        try {            
            Stage stage = CS4400HealthInspection.getStage();
            Parent foster = FXMLLoader.load(getClass().getResource("InspMenu.fxml"));
            Scene scene = new Scene(foster);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
           System.err.println(e);
        }
    }
    
}
