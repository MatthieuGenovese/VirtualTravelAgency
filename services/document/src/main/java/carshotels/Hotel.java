/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carshotels;

/**
 *
 * @author lm203441
 */
public class Hotel {
    
    private String name;
    
    private double price;
    
    private String date;
    
    private String destination;
    
    public Hotel(String name, double price, String date, String destination) {
        this.price = price;
        this.date = date;
        this.destination = destination;
    }
    
    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getDate() {
        return date;
    }

    public String getDestination() {
        return destination;
    }
    
    
}
