/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fatma.project.liberery.model;

/**
 *
 * @author A M
 */
public class Book {
    
    private int id;
    private String name;
    private int buyPrices;
    private int borrPrices;
    private int quantity;
    private String description;
    private int outherId;
    private int categoryId; 

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBuyPrices() {
        return buyPrices;
    }

    public void setBuyPrices(int buyPrices) {
        this.buyPrices = buyPrices;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getOutherId() {
        return outherId;
    }

    public void setOutherId(int outherId) {
        this.outherId = outherId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
    

    public int getBorrPrices() {
        return borrPrices;
    }

    public void setBorrPrices(int borrPrices) {
        this.borrPrices = borrPrices;
    }
    
}
