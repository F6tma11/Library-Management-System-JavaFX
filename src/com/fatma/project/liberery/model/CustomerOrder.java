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
public class CustomerOrder {

    private String phoneNumber;
    private int bookId;
    private String bookname;
    private int borrowPrice;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getBookname() {
        return bookname;
    }

    public void setBookname(String bookname) {
        this.bookname = bookname;
    }

    public int getBorrowPrice() {
        return borrowPrice;
    }

    public void setBorrowPrice(int borrowPrice) {
        this.borrowPrice = borrowPrice;
    }

  
}
