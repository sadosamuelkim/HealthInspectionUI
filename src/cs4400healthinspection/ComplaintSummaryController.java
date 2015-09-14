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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
public class ComplaintSummaryController implements Initializable {

    @FXML TextField yearTextfield;
    @FXML TextField minComplaintTextfield;
    @FXML TextField maxScoreTextfield;
    @FXML TableColumn restaurantCol;
    @FXML TableColumn addressCol;
    @FXML TableColumn restopNameCol;
    @FXML TableColumn restopEmailCol;
    @FXML TableColumn scoreCol;
    @FXML TableColumn numComCol;
    @FXML TableColumn complaintCol;
    @FXML Label textLabel;
    @FXML TableView bigTable;
    
    private ObservableList<ReportData> data;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        assert CS4400HealthInspection.getUserType() == CS4400HealthInspection.User.INSPECTOR;
        data = FXCollections.observableArrayList();
        assert bigTable != null;
        restaurantCol.setCellValueFactory(new PropertyValueFactory<>("restaurant"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        restopNameCol.setCellValueFactory(new PropertyValueFactory<>("restopName"));
        restopEmailCol.setCellValueFactory(new PropertyValueFactory<>("restopEmail"));
        scoreCol.setCellValueFactory(new PropertyValueFactory<>("score"));
        numComCol.setCellValueFactory(new PropertyValueFactory<>("numCom"));
        complaintCol.setCellValueFactory(new PropertyValueFactory<>("complaint"));
    } 
    
    @FXML
    private void onSubmitEvent(MouseEvent event) {
        String maxScore = maxScoreTextfield.getText();
        String year = yearTextfield.getText();
        String minComplaint = minComplaintTextfield.getText();

        if(!Pattern.matches("\\d\\d\\d\\d",year)) {
            textLabel.setText("Invalid input for year. please put a valid year.");
        } else if(!Pattern.matches("\\d*",minComplaint)) {
            textLabel.setText("Invalid input for min complaint. please only put numbers");
        } else if(!Pattern.matches("1?\\d?\\d", maxScore)) {
            textLabel.setText("Invalid input for max score. Must be between 0 and 100.");
        } else {
            data.clear();
            Connection con = null;
            Statement stmt = null;
            Statement stmt2 = null;
            ResultSet rs = null;
            ResultSet rs2 = null;
            try {
                con = DriverManager.getConnection(
                            CS4400HealthInspection.MYSQL, 
                            CS4400HealthInspection.USERNAME, 
                            CS4400HealthInspection.PW);
                stmt = con.createStatement();
                stmt2 = con.createStatement();
                rs = stmt.executeQuery("select c.rid, name, street, city, state,"
                        + " zipcode, firstname, lastname, o.email, i.totalscore, "
                        + "count(distinct c.description) as numofcom from restaurant"
                        + " as r join operatorowner as o on r.email=o.email "
                        + "join inspection as i on i.rid = r.rid join complaint as"
                        + " c on c.rid = r.rid where totalscore < " 
                        + maxScore +" and r.rid in "
                        + "(select rid from contains where idate>\"" 
                        + (Integer.parseInt(year)-1) 
                        + "-12-31\" and idate<\"" 
                        + (Integer.parseInt(year)+1) 
                        + "-1-1\" and score < 8 and itemnum in "
                        + "(SELECT itemnum from item where critical =\"Y\") "
                        + "group by rid) group by name having count"
                        + "(distinct c.description) >= "
                        + minComplaint);
                
                while(rs.next()) {
                    String restaurant = rs.getString("name");
                    String address = rs.getString("street") + ", " 
                            + rs.getString("city") + ", " 
                            + rs.getString("state") + " " 
                            + rs.getString("zipcode");
                    String restopName = rs.getString("firstname") 
                            + " " + rs.getString("lastname");
                    String restopEmail = rs.getString("email");
                    int score = rs.getInt("totalscore");
                    int nomCom = rs.getInt("numofcom");
                    rs2 = stmt2.executeQuery("select description "
                            + "from complaint as com "
                            + "where rid=" + rs.getString("rid") + " and cdate>\"2014-12-31\" "
                            + "and cdate<\"2016-1-1\"");
                    String complaints = "";
                    while(rs2.next()) {
                        complaints += rs2.getString("description") + "\n";
                    }
                    ReportData rd = new ReportData(restaurant, address, 
                                            restopName, restopEmail, score, nomCom, complaints);
                    data.add(rd);
                }            
            } catch (SQLException e) {
                System.err.println(e);
            } finally {
                if(rs != null) {try {rs.close();} catch (SQLException e){System.err.println(e);}}
                if(rs2 != null) {try {rs2.close();} catch (SQLException e){System.err.println(e);}}
                if(stmt != null) {try {stmt.close();} catch (SQLException e){System.err.println(e);}}
                if(stmt2 != null) {try {stmt2.close();} catch (SQLException e){System.err.println(e);}}
                if(con != null) {try {con.close();} catch (SQLException e){System.err.println(e);}}
            }
        }
        bigTable.setItems(data);        
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
     * this controller. The name and address of the restaurant, the email
     * and the name of the operator responsible for the restaurant, the latest 
     * health inspection for the restaurant, and the complaint along with the 
     * number of complaints. 
     */
    public class ReportData {
        private String restaurant;
        private String address;
        private String restopName;
        private String restopEmail;        
        private int score;
        private int numCom;
        private String complaint;
        
        public ReportData(String restaurant, String address, String restopName, 
                String restopEmail, int score, int numCom, String complaint) {
            this.restaurant = restaurant;
            this.address = address;
            this.restopName = restopName;
            this.restopEmail = restopEmail;
            this.score = score;
            this.numCom = numCom;
            this.complaint = complaint;
        }

        public String getRestaurant() {
            return restaurant;
        }
        
        public String getAddress() {
            return address;
        }
        
        public String getRestopName() {
            return restopName;
        }
        
        public String getRestopEmail() {
            return restopEmail;
        }
        
        public int getScore() {
            return score;
        }
        
        public int getNumCom() {
            return numCom;
        }
        
        public String getComplaint() {
            return complaint;
        }
        
        public void setRestaurant(String restaurant) {
            this.restaurant = restaurant;
        }
        
        public void setAddress(String address) {
            this.address = address;
        }
        
        public void setRestopName(String restopName) {
            this.restopName = restopName;
        }
        
        public void setRestopEmail(String restopEmail) {
            this.restopEmail = restopEmail;
        }
        
        public void setScore(int score) {
            this.score = score;
        }
        
        public void setNumCom(int numCom) {
            this.numCom = numCom;
        }
        
        public void setComplaint(String complaint) {
            this.complaint = complaint;
        }
        
        @Override
        public String toString() {
            return restaurant + " " + address + " " + restopName 
                    + " " + restopEmail + " " + score 
                    + " " + numCom + " " + complaint;
        }
    }
}
