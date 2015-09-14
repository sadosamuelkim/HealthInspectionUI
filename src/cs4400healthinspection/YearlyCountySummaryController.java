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
 * @author sado0726
 */
public class YearlyCountySummaryController implements Initializable {

    @FXML TextField year;
    @FXML TextField county;
    @FXML TableColumn monthCol;
    @FXML TableColumn inspCountCol;
    @FXML TableView bigTable;
    @FXML Label textLabel;
    
    private ObservableList<ReportData> data;
    public static final String MONTHARRAY[] = {"January", "February", "March",
        "April", "May", "June", "July", "August", "September", "October", 
        "November", "December"};
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        data = FXCollections.observableArrayList();
        assert bigTable != null;
        monthCol.setCellValueFactory(new PropertyValueFactory<>("month"));
        inspCountCol.setCellValueFactory(new PropertyValueFactory<>("inspCount"));
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
            int totalCount = 0;
            for(int monthNum = 1; monthNum <= 12; monthNum++) {
                String monthNumString;
                if(monthNum < 10) {
                    monthNumString = "0" + monthNum;
                } else {
                    monthNumString = "" + monthNum;
                }

                try (
                    Connection con = DriverManager.getConnection(
                                CS4400HealthInspection.MYSQL, 
                                CS4400HealthInspection.USERNAME, 
                                CS4400HealthInspection.PW);
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT COUNT(i.idate)"
                            + "FROM inspection AS i LEFT JOIN restaurant AS r ON i.rid=r.rid "
                            + "WHERE i.idate LIKE(\"" + year.getText() + "-" + monthNumString + "-__\")"
                            + "AND county=\"" + county.getText() + "\"");
                ){
                        rs.next();
                        String month = MONTHARRAY[monthNum - 1]; 
                        int count = rs.getInt("COUNT(i.idate)");
                        totalCount += count;
                        ReportData rd = new ReportData(month, count);
                        data.add(rd);
                        System.out.println(rd);
                } catch (SQLException e) {
                    System.out.println(e);
                }
            }
            ReportData rd = new ReportData("Grand Total", totalCount);
            data.add(rd);
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
     * this controller. The month and how many inspection done for that 
     * given month is on the table.
     */ 
    public class ReportData {
        private String month;
        private int inspCount;
        
        public ReportData(String month, int inspCount) {
            this.month = month;
            this.inspCount = inspCount;
        }
        
        public String getMonth() {
            return month;
        }
        
        public int getInspCount() {
            return inspCount;
        }
        
        public void setMonth(String month) {
            this.month = month;
        }
        
        public void setInspCount(int inspCount) {
            this.inspCount = inspCount;
        }
        
        @Override
        public String toString() {
            return month + " " + inspCount;
        }
    }
}
