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
public class ROInspectionResultController implements Initializable {

    @FXML TableView bigTable;
    @FXML TableColumn ItemNumberCol;
    @FXML TableColumn ItemDescriptionCol;
    @FXML TableColumn Score1;
    @FXML TableColumn Score2;
    @FXML Label Date1;
    @FXML Label Date2;
    
    private ObservableList<ResultData> data;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        data = FXCollections.observableArrayList();
        assert bigTable != null;
        ItemNumberCol.setCellValueFactory(new PropertyValueFactory<>("itemNum"));
        ItemDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("itemDesc"));
        Score1.setCellValueFactory(new PropertyValueFactory<>("score1")); 
        Score2.setCellValueFactory(new PropertyValueFactory<>("score2")); 
        int rid = ROSearchCriteriaController.getRid();
        Connection con = null;
        Statement stmt1 = null;
        Statement stmt2 = null;
        Statement stmt3 = null;
        ResultSet rs1 = null;
        ResultSet rs2 = null;
        ResultSet rs3 = null;
        
        try {
            con = DriverManager.getConnection(
                    CS4400HealthInspection.MYSQL, 
                    CS4400HealthInspection.USERNAME, 
                    CS4400HealthInspection.PW);
            stmt1 = con.createStatement();
            stmt2 = con.createStatement();
            stmt3 = con.createStatement();
            rs1 = stmt1.executeQuery("SELECT MAX(i.idate), MAX(j.idate) "
                    + "FROM inspection i, inspection j "
                    + "WHERE i.rid =" + rid + " AND j.rid = " + rid + " AND i.idate "
                    + "NOT IN (SELECT MAX(idate) FROM inspection WHERE rid =" + rid + ")");
            rs1.next();
            String date1 = rs1.getString("MAX(i.idate)");
            String date2 = rs1.getString("MAX(j.idate)");
            if(date1 == null || date2 == null) {
                System.err.println("this functionality requires at least two report");
            } else {
            //TODO when time allows, fix when there are only one inspection on the restaurants
                rs2 = stmt2.executeQuery("SELECT i.passfail, j.passfail, i.totalscore, j.totalscore "
                        + "FROM inspection i, inspection j "
                        + "WHERE i.idate = \"" + date1 + "\" "
                        + "AND j.idate = \"" + date2 + "\"");

                rs2.next();        
                String passfail1 = rs2.getString("i.passfail");
                String passfail2 = rs2.getString("j.passfail");
                String totalscore1 = rs2.getString("i.totalscore");
                String totalscore2 = rs2.getString("j.totalscore");

                rs3 = stmt3.executeQuery("SELECT i.itemnum, k.description, i.score, j.score "
                        + "FROM contains AS i, contains AS j, item AS k "
                        + "WHERE i.itemnum=j.itemnum AND j.itemnum=k.itemnum "
                        + "AND i.idate = \"" + date1 + "\" AND i.rid = " + rid + " "
                        + "AND j.idate = \"" + date2 + "\" AND j.rid = " + rid + "");
                
                while(rs3.next()) {
                    String itemNum = rs3.getString("i.itemnum");
                    String itemDesc = rs3.getString("k.description");
                    String score1 = rs3.getString("i.score");
                    String score2 = rs3.getString("j.score");
                    ResultData rd = new ResultData(itemNum, itemDesc, score1, score2);
                    data.add(rd);
                }
                Date1.setText(date1);
                Date2.setText(date2);
                ResultData rd1 = new ResultData("TOTAL SCORE", "", totalscore1, totalscore2);
                data.add(rd1);
                ResultData rd2 = new ResultData("RESULT", "", passfail1, passfail2);
                data.add(rd2);
            }
            bigTable.setItems(data);
            
        } catch (SQLException e) {
                System.err.println(e);
        } finally {
            if(rs1 != null) {try {rs1.close();} catch (SQLException e){System.err.println(e);}}
            if(rs2 != null) {try {rs2.close();} catch (SQLException e){System.err.println(e);}}
            if(rs3 != null) {try {rs3.close();} catch (SQLException e){System.err.println(e);}}
            if(stmt1 != null) {try {stmt1.close();} catch (SQLException e){System.err.println(e);}}
            if(stmt2 != null) {try {stmt2.close();} catch (SQLException e){System.err.println(e);}}
            if(stmt3 != null) {try {stmt3.close();} catch (SQLException e){System.err.println(e);}}
            if(con != null) {try {con.close();} catch (SQLException e){System.err.println(e);}}
        }        
    }
    
    @FXML
    private void onReturnEvent(MouseEvent event) {
        try {
            Stage stage = CS4400HealthInspection.getStage();
            Parent foster = FXMLLoader.load(getClass().getResource("ROSearchCriteria.fxml"));
            Scene scene = new Scene(foster);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println(e);
        }
    }
    
    /**
     * A class that carries all the values to be entered into the table for 
     * this controller. the item number and the description and scores for past 
     * two inspections.
     */    
    public class ResultData {
        private String itemNum;
        private String itemDesc;
        private String score1;
        private String score2;
        
        public ResultData(String itemNum, String itemDesc, String score1, String score2) {
            this.itemNum = itemNum;
            this.itemDesc = itemDesc;
            this.score1 = score1;
            this.score2 = score2;
        }
        
        public String getItemNum() {
            return itemNum;
        }
        
        public String getItemDesc() {
            return itemDesc;
        }
            
        public String getScore1() {
            return score1;
        }
        
        public String getScore2() {
            return score2;
        }
        
        public void setItemNum(String itemNum) {
            this.itemNum = itemNum;
        }
        
        public void setItemDesc(String itemDesc) {
            this.itemDesc = itemDesc;
        }
        
        public void setScore1(String score1) {
            this.score1 = score1;
        }
        
        public void setScore2(String score2) {
            this.score2 = score2;
        }
                
        @Override
        public String toString() {
            return itemNum + " " + itemDesc + " " + score1 + " " + score2;
        }
    }
}
