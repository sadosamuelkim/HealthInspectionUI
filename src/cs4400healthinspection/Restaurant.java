/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs4400healthinspection;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Roberto
 */
public class Restaurant {
    public SimpleStringProperty name = new SimpleStringProperty();
    public SimpleStringProperty address = new SimpleStringProperty();
    public SimpleStringProperty cuisine = new SimpleStringProperty();
    public SimpleIntegerProperty rid = new SimpleIntegerProperty();
    public SimpleIntegerProperty inspScore = new SimpleIntegerProperty();
    public SimpleStringProperty inspDate = new SimpleStringProperty();
    
    /**
     * Constructor for ROSearchCriteriaController, which uses the restaurant ID
     * restaurant's name and address for the columns.
     * @param rid
     * @param name
     * @param address 
     */
    public Restaurant(int rid, String name, String address) {
        this.rid.set(rid);
        this.name.set(name);
        this.address.set(address);
    }
    
    /**
     * Constructor for CustSearchResultController, which uses the restaurant's 
     * name, address, the type of cuisine, the score of the last inspection, 
     * and the date of a the last inspection of restaurants searched by a guest
     * @param name
     * @param address
     * @param cuisine
     * @param score
     * @param date 
     */
    public Restaurant(String name, String address, String cuisine, int score, String date) {
        this.name.set(name);
        this.address.set(address);
        this.cuisine.set(cuisine);
        this.inspScore.set(score);
        this.inspDate.set(date);
    }
    
    /**
     * obtaining the property of name in String
     * @return name one of the properties of restaurants 
     */
    public SimpleStringProperty nameProperty() {
        return name;
    }
    
    /**
     * obtaining the property of address in String
     * @return address one of the properties of restaurants
     */
    public SimpleStringProperty addressProperty() {
        return address;
    }
    
    /**
     * obtaining the property of cuisine in String
     * @return cuisine one of the properties of restaurants
     */
    public SimpleStringProperty cuisineProperty() {
        return cuisine;
    }  
    
    /**
     * obtaining the property of restaurant ID in Integer
     * @return rid one of the properties of restaurants
     */
    public SimpleIntegerProperty ridProperty() {
        return rid;
    }     
    
    /**
     * obtaining the property of inspector's score in Integer
     * @return inspScore one of the properties of restaurants
     */    
    public SimpleIntegerProperty inspScoreProperty() {
        return inspScore;
    }              

    /**
     * obtaining the property of inspector's date in Integer
     * @return inspDate one of the properties of restaurants
     */    
    public SimpleStringProperty inspDateProperty() {
        return inspDate;
    }     
}