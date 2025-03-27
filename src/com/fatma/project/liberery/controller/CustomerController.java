/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fatma.project.liberery.controller;

import com.fatma.project.liberery.dao.DBConnection;
import com.fatma.project.liberery.model.People;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author A M
 */
public class CustomerController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private TableColumn<People, String> colAddress;

    @FXML
    private TableColumn<People, Integer> colEmployeeID;

    @FXML
    private TableColumn<People, Integer> colID;

    @FXML
    private TableColumn<People, String> colName;

    @FXML
    private TableColumn<People, String> colPhoneNumber;

    @FXML
    private TableView<People> custTable;

    @FXML
    private TextField cust_address;

    @FXML
    private TextField cust_id;

    @FXML
    private TextField cust_name;

    @FXML
    private TextField cust_phonenumber;

    @FXML
    private TextField employe_id;

    @FXML
    private TextField search_cust;

    String query = "";

    Connection connection = null;

    PreparedStatement prep = null;

    DBConnection conn = new DBConnection();

    ResultSet resultSet = null;

    ObservableList<People> customerList = FXCollections.observableArrayList();
//-------------------------Initialize and show Customer Data----------------------------------------

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            // TODO
            connection = conn.connect();
            loadData();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CustomerController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(CustomerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadData() throws SQLException {
        refreshData();
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colPhoneNumber.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));

        colEmployeeID.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
    }

    private void refreshData() throws SQLException {
        customerList.clear();
        query = "SELECT * FROM people";
        prep = connection.prepareStatement(query);
        resultSet = prep.executeQuery();

        while (resultSet.next()) {
            People people = new People();
            people.setId(resultSet.getInt("id"));
            people.setName(resultSet.getString("name"));
            people.setAddress(resultSet.getString("address"));
            people.setPhoneNumber(resultSet.getString("phoneNumber"));

            people.setEmployeeId(resultSet.getInt("employees_id"));
            customerList.add(people);
        }
        custTable.setItems(customerList);
    }

//--------------------------------ADD Customer--------------------------------------------------
    @FXML
    void addCustomer(ActionEvent event) throws SQLException {
        query = "INSERT INTO people (id , name , phoneNumber,address,employees_id) VALUES (?,?,?,?,?)";
        prep = connection.prepareStatement(query);
        prep.setInt(1, Integer.parseInt(cust_id.getText().toString()));

        prep.setString(2, cust_name.getText().toString());
        prep.setString(3, cust_phonenumber.getText().toString());
        prep.setString(4, cust_address.getText().toString());
        prep.setInt(5, Integer.parseInt(employe_id.getText().toString()));
        prep.executeUpdate();
        loadData();
addToBorrowTable();
        cust_id.setText("");

        cust_name.setText("");
        cust_phonenumber.setText("");
        cust_address.setText("");
        employe_id.setText("");

    }

    void addToBorrowTable() throws SQLException {
        query = "Select * from people where phoneNumber = " + cust_phonenumber.getText().toString();
        prep = connection.prepareStatement(query);
        resultSet = prep.executeQuery();
        
        if (resultSet.next()) {
            query = "INSERT INTO number_of_books_borrow (people_phoneNumber) VALUES (?)";
            prep = connection.prepareStatement(query);
            prep.setString(1, cust_phonenumber.getText().toString());
            prep.executeUpdate();
        }
    }

//--------------------------------EDITE Customer------------------------------------------------
    @FXML
    void editCustomer(ActionEvent event) throws SQLException {
        query = "UPDATE people SET name=? , phoneNumber=? , address=? , employees_id=?   WHERE id=?";
        prep = connection.prepareStatement(query);
        prep.setString(1, cust_name.getText().toString());
        prep.setString(2, cust_phonenumber.getText().toString());
        prep.setString(3, cust_address.getText().toString());
        prep.setInt(4, Integer.parseInt(employe_id.getText().toString()));
        prep.setString(5, cust_id.getText().toString());
        prep.executeUpdate();
        loadData();
        cust_id.setText("");
        cust_name.setText("");
        cust_phonenumber.setText("");
        cust_address.setText("");
        employe_id.setText("");
    }

    private int index = -1;
//-----------------------------GET data from selected row--------------------------------------

    @FXML
    private void getSelectedCul() {
        index = custTable.getSelectionModel().getSelectedIndex();
        if (index <= -1) {
            return;
        }
        cust_id.setText(colID.getCellData(index).toString());
        cust_name.setText(colName.getCellData(index).toString());
        cust_phonenumber.setText(colPhoneNumber.getCellData(index).toString());
        cust_address.setText(colAddress.getCellData(index).toString());
        employe_id.setText(colEmployeeID.getCellData(index).toString());

    }
//----------------------------Back To System Page----------------------------------------------

    @FXML
    void back_from_customer(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/fatma/project/liberery/view/LibereryContent.fxml"));
        Scene scene = new Scene(root);
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
        Stage newStage = new Stage();
        newStage.setScene(scene);
        newStage.setTitle("System");
        newStage.show();
    }

}
