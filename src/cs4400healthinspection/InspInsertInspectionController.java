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
public class InspInsertInspectionController implements Initializable {

    @FXML TextField inspIDTextfield;
    @FXML TextField restIDTextfield;
    @FXML TextField dateTextfield;
    @FXML TableView bigTable;
    @FXML Label textLabel;
    @FXML TableColumn itemNumberCol;
    @FXML TableColumn itemDescrCol;
    @FXML TableColumn CriticalCol;
    @FXML TextField t1Col;
    @FXML TextField t2Col;
    @FXML TextField t3Col;
    @FXML TextField t4Col;
    @FXML TextField t5Col;
    @FXML TextField t6Col;
    @FXML TextField t7Col;
    @FXML TextField t8Col;
    @FXML TextField t9Col;
    @FXML TextField t10Col;
    @FXML TextField t11Col;
    @FXML TextField t12Col;
    @FXML TextField t13Col;
    @FXML TextField t14Col;
    @FXML TextField t15Col;
    
    TextField[] textfields;
    private ObservableList<ReportData> data;

    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        textfields = new TextField[]{t1Col, t2Col, t3Col, t4Col, t5Col, t6Col, t7Col, t8Col, t9Col, t10Col, t11Col, t12Col, t13Col, t14Col, t15Col}; 
        data = FXCollections.observableArrayList();
        assert bigTable != null;
        itemNumberCol.setCellValueFactory(new PropertyValueFactory<>("number"));
        itemDescrCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        CriticalCol.setCellValueFactory(new PropertyValueFactory<>("critical"));

        data.clear();
    
        try (
            Connection con = DriverManager.getConnection(
                        CS4400HealthInspection.MYSQL, 
                        CS4400HealthInspection.USERNAME, 
                        CS4400HealthInspection.PW);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT itemnum,description,critical FROM item");
        ){
            while(rs.next()){
                int number = rs.getInt("itemnum");
                String description = rs.getString("description");
                String critical = rs.getString("critical");
                ReportData rd = new ReportData(number, description, critical);
                data.add(rd);
            }
        }catch (SQLException e) {
            System.err.println(e);
        }
        bigTable.setItems(data);    
    }
    
    @FXML
    private void onSubmitEvent(MouseEvent event) {
        String inspID = inspIDTextfield.getText();
        String restID = restIDTextfield.getText();
        String date = dateTextfield.getText();
        if(!Pattern.matches("\\d*", inspID)) {
            textLabel.setText("insp ID can only take numbers");
        } else if(!Pattern.matches("\\d*", restID)) {
            textLabel.setText("rest ID can only take numbers");
        } else if(!Pattern.matches("\\d{8}",date)) {
            textLabel.setText("invalid date: please enter date in format YYYYMMDD");
        } else {
            int totalscore = 0;
            String passfail = "PASS";
            for(int i = 0; i < textfields.length; i++) {
                if(textfields[i].getText().equals("")) {
                    System.out.println("slot " + i + " empty");
                } else {
                    if(i < 8) {
                        if(Integer.parseInt(textfields[i].getText()) < 8)
                        passfail = "FAIL";
                    }
                    totalscore += Integer.parseInt(textfields[i].getText());
                }
            }
            if(totalscore < 75) {
                passfail = "FAIL";
            }
            try (
                Connection con = DriverManager.getConnection(
                        CS4400HealthInspection.MYSQL, 
                        CS4400HealthInspection.USERNAME, 
                        CS4400HealthInspection.PW);
                Statement stmt0 = con.createStatement();    
                Statement stmt1 = con.createStatement();
                Statement stmt2 = con.createStatement();
                Statement stmt3 = con.createStatement();
                Statement stmt4 = con.createStatement();
                Statement stmt5 = con.createStatement();
                Statement stmt6 = con.createStatement();
                Statement stmt7 = con.createStatement();
                Statement stmt8 = con.createStatement();
                Statement stmt9 = con.createStatement();
                Statement stmta = con.createStatement();
                Statement stmtb = con.createStatement();
                Statement stmtc = con.createStatement();
                Statement stmtd = con.createStatement();
                Statement stmte = con.createStatement();
                Statement stmtf = con.createStatement();
            ) {
                stmt0.executeUpdate("INSERT INTO inspection(rid, iid, idate, totalscore, passfail) VALUES(" + restIDTextfield.getText() + "," + inspIDTextfield.getText() + ", \"" + dateTextfield.getText() + "\"," + totalscore + ",\"" + passfail + "\")");
                stmt1.executeUpdate("INSERT INTO contains(itemnum, rid, idate, score) VALUES(" + 1 + ", " + restIDTextfield.getText() + ",\"" + dateTextfield.getText() + "\"," + Integer.parseInt(textfields[0].getText()) + ")");
                stmt2.executeUpdate("INSERT INTO contains(itemnum, rid, idate, score) VALUES(" + 2 + ", " + restIDTextfield.getText() + ",\"" + dateTextfield.getText() + "\"," + Integer.parseInt(textfields[1].getText()) + ")");
                stmt3.executeUpdate("INSERT INTO contains(itemnum, rid, idate, score) VALUES(" + 3 + ", " + restIDTextfield.getText() + ",\"" + dateTextfield.getText() + "\"," + Integer.parseInt(textfields[2].getText()) + ")");
                stmt4.executeUpdate("INSERT INTO contains(itemnum, rid, idate, score) VALUES(" + 4 + ", " + restIDTextfield.getText() + ",\"" + dateTextfield.getText() + "\"," + Integer.parseInt(textfields[3].getText()) + ")");
                stmt5.executeUpdate("INSERT INTO contains(itemnum, rid, idate, score) VALUES(" + 5 + ", " + restIDTextfield.getText() + ",\"" + dateTextfield.getText() + "\"," + Integer.parseInt(textfields[4].getText()) + ")");
                stmt6.executeUpdate("INSERT INTO contains(itemnum, rid, idate, score) VALUES(" + 6 + ", " + restIDTextfield.getText() + ",\"" + dateTextfield.getText() + "\"," + Integer.parseInt(textfields[5].getText()) + ")");
                stmt7.executeUpdate("INSERT INTO contains(itemnum, rid, idate, score) VALUES(" + 7 + ", " + restIDTextfield.getText() + ",\"" + dateTextfield.getText() + "\"," + Integer.parseInt(textfields[6].getText()) + ")");
                stmt8.executeUpdate("INSERT INTO contains(itemnum, rid, idate, score) VALUES(" + 8 + ", " + restIDTextfield.getText() + ",\"" + dateTextfield.getText() + "\"," + Integer.parseInt(textfields[7].getText()) + ")");
                stmt9.executeUpdate("INSERT INTO contains(itemnum, rid, idate, score) VALUES(" + 9 + ", " + restIDTextfield.getText() + ",\"" + dateTextfield.getText() + "\"," + Integer.parseInt(textfields[8].getText()) + ")");
                stmta.executeUpdate("INSERT INTO contains(itemnum, rid, idate, score) VALUES(" + 10 + ", " + restIDTextfield.getText() + ",\"" + dateTextfield.getText() + "\"," + Integer.parseInt(textfields[9].getText()) + ")");
                stmtb.executeUpdate("INSERT INTO contains(itemnum, rid, idate, score) VALUES(" + 11 + ", " + restIDTextfield.getText() + ",\"" + dateTextfield.getText() + "\"," + Integer.parseInt(textfields[10].getText()) + ")");
                stmtc.executeUpdate("INSERT INTO contains(itemnum, rid, idate, score) VALUES(" + 12 + ", " + restIDTextfield.getText() + ",\"" + dateTextfield.getText() + "\"," + Integer.parseInt(textfields[11].getText()) + ")");
                stmtd.executeUpdate("INSERT INTO contains(itemnum, rid, idate, score) VALUES(" + 13 + ", " + restIDTextfield.getText() + ",\"" + dateTextfield.getText() + "\"," + Integer.parseInt(textfields[12].getText()) + ")");
                stmte.executeUpdate("INSERT INTO contains(itemnum, rid, idate, score) VALUES(" + 14 + ", " + restIDTextfield.getText() + ",\"" + dateTextfield.getText() + "\"," + Integer.parseInt(textfields[13].getText()) + ")");
                stmtf.executeUpdate("INSERT INTO contains(itemnum, rid, idate, score) VALUES(" + 15 + ", " + restIDTextfield.getText() + ",\"" + dateTextfield.getText() + "\"," + Integer.parseInt(textfields[14].getText()) + ")");

                try {
                    Stage stage = CS4400HealthInspection.getStage();
                    Parent foster = FXMLLoader.load(getClass().getResource("InspInsertComment.fxml"));
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
            Parent foster = FXMLLoader.load(getClass().getResource("InspMenu.fxml"));
            Scene scene = new Scene(foster);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
           System.err.println(e);
        }
    }
    
    /**
     * A class that carries all the values to be entered into the table for 
     * this controller. The item number and the description along with whether
     * it's critical of not are the three values. 
     */
    public class ReportData {
        
        private int number;
        private String description;
        private String critical;
       
        public ReportData(int number, String description, String critical) {
            this.number = number;
            this.description = description;
            this.critical = critical;
        }
        
        public int getNumber() {
            return number;
        }

        public String getDescription() {
            return description;
        }
        
        public String getCritical() {
            return critical;
        }
        
        public void setNumber (int number) {
            this.number = number;
        }
        
        public void setDescription (String description) {
            this.description = description;
        }
        
        public void setCritical(String critical) {
            this.critical = critical;
        }
    }
}
