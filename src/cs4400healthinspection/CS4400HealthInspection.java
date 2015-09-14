/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs4400healthinspection;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author sado0726
 */
public class CS4400HealthInspection extends Application {
    private static Parent root;
    private static Stage stage;
    private static String sessionUser;
    private static String sessionPassword;
    static final String USERNAME = "cs4400_Group_31";
    static final String PW = "5Zes70Sx";
    static final String MYSQL = "jdbc:mysql://academic-mysql.cc.gatech.edu/cs4400_Group_31";
    public static enum User {RESTOP, INSPECTOR, NOTAUSER};
    private static User userType;
    
    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    /**
     * Parent is a scene used to refer back on the layout of the GUI. This will
     * change every time switching from one java controller to next. 
     * @return root
     */
    public static Parent getParent() {
        return root;
    }
    
    /**
     * gets the Stage of the frame from the GUI. By setting scene, we can set up
     * a new scene which we will use setScene() method.
     * @return stage
     */
    public static Stage getStage() {
        return stage;
    }
    
    /**
     * sets the username of the current user 
     * @param username 
     */
    public static void setSessionUser(String username) {
            sessionUser = username;
    }
    
    /**
     * gets the username of the current user
     * @return 
     */
    public static String getSessionUser() {
        return sessionUser;
    }
    
    /**
     * gets the password of the current user
     * @return 
     */
    public static String getSessionPassword() {
        return sessionPassword;
    }
    
    /**
     * set the password of the current user
     * @param pw 
     */
    public static void setSessionPassword(String pw) {
        sessionPassword = pw;
    }
    
    public static void setUserType(User userType) {
        CS4400HealthInspection.userType = userType; 
    }
    
    public static User getUserType() {
        return userType;
    }
}
