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
public class MonthlySummaryController implements Initializable {

    @FXML TextField monthTextfield;
    @FXML TextField yearTextfield;
    @FXML TableColumn countyCol;
    @FXML TableColumn cuisineCol;
    @FXML TableColumn inspCountCol;
    @FXML TableColumn failCountCol;
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
        countyCol.setCellValueFactory(new PropertyValueFactory<>("county"));
        cuisineCol.setCellValueFactory(new PropertyValueFactory<>("cuisine"));
        inspCountCol.setCellValueFactory(new PropertyValueFactory<>("inspCount"));
        failCountCol.setCellValueFactory(new PropertyValueFactory<>("failCount"));
    }    
    
    @FXML
    private void onSubmitEvent(MouseEvent event) {
        String year = yearTextfield.getText();
        String month = monthTextfield.getText();
        if(!Pattern.matches("\\d\\d\\d\\d", year)) {
            textLabel.setText("invalid year: please put 4 digits e.g. 1992");
        } else if(!Pattern.matches("\\d\\d", month)) {
            textLabel.setText("invalid month: please put 2 digits e.g. 02 for February");
        } else {
            data.clear();
            int monthNum = Integer.parseInt(month);
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
                ResultSet rs = stmt.executeQuery("SELECT county, cuisine, COUNT(*)"
                        + " as inspections, SUM(passfail = \"fail\") as failed "
                        + "FROM inspection NATURAL JOIN restaurant "
                        + "WHERE idate LIKE(\"" + yearTextfield.getText() + "-" 
                        + monthNumString + "-__\") "
                        + "GROUP BY county, cuisine");
            ){
                String countyString = "";
                int subtotalInsp = 0;
                int subtotalFail = 0;
                int grandtotalInsp = 0;
                int grandtotalFail = 0;
                
                while(rs.next()) {
                    if(countyString.equalsIgnoreCase(rs.getString("county"))) {
                        countyString = "";
                    } else {
                        if(!data.isEmpty()) {
                            ReportData rd = new ReportData("", "Sub Total", subtotalInsp, subtotalFail);
                            data.add(rd);
                            grandtotalInsp += subtotalInsp;
                            grandtotalFail += subtotalFail;
                            subtotalInsp = 0;
                            subtotalFail = 0;
                        }
                        countyString = rs.getString("county");
                    }
                    String cuisine = rs.getString("cuisine");
                    int inspCount = rs.getInt("inspections");
                    int failCount = rs.getInt("failed");
                    subtotalInsp += inspCount;
                    subtotalFail += failCount;
                    ReportData rd = new ReportData(countyString, cuisine, inspCount, failCount);
                    data.add(rd);
                }
                
                ReportData rd = new ReportData("", "Sub Total", subtotalInsp, subtotalFail);
                data.add(rd);
                grandtotalInsp += subtotalInsp;
                grandtotalFail += subtotalFail;
                ReportData rdgrand = new ReportData("Grand Total", "", grandtotalInsp, grandtotalFail);
                data.add(rdgrand);
                
            } catch (SQLException e) {
                System.err.println(e);
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
     * this controller. The county that the restaurants are in, the cuisine, 
     * and the count for the inspection done in that county 
     * and the number of failure from all the inspection 
     */
    public class ReportData {
        private String county;
        private String cuisine;
        private int inspCount;
        private int failCount;
        
        public ReportData(String county, String cuisine, int inspCount, int failCount) {
            this.county = county;
            this.cuisine = cuisine;
            this.inspCount = inspCount;
            this.failCount = failCount;
        }
        
        public String getCounty() {
            return county;
        }
        
        public String getCuisine() {
            return cuisine;
        }
        
        public int getInspCount() {
            return inspCount;
        }
        
        public int getFailCount() {
            return failCount;
        }
        
        public void setCounty(String month) {
            this.county = month;
        }
        
        public void setCuisine(String cuisine) {
            this.cuisine = cuisine;
        }
        
        public void setInspCount(int inspCount) {
            this.inspCount = inspCount;
        }
        
        public void setfailCount(int failCount) {
            this.failCount = failCount;
        }
        
        @Override
        public String toString() {
            return county + " " + cuisine + " " + inspCount + " " + failCount;
        }
    }
}
