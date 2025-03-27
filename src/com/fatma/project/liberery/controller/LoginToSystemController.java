/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fatma.project.liberery.controller;

import com.fatma.project.liberery.dao.DBConnection;
import com.fatma.project.liberery.model.Users;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author A M
 */
public class LoginToSystemController implements Initializable {

    /**
     * Initializes the controller class.
     */
    DBConnection connManager = new DBConnection();
    @FXML
    private TextField txt_password;

    @FXML
    private TextField txt_username;

    @FXML
    void goToSystem(ActionEvent event) throws SQLException, IOException, ClassNotFoundException {
        String username = txt_username.getText().toString();
        String password = txt_password.getText().toString();
         boolean isAuthenticated = false;
        try (
                Connection connection = connManager.connect();
                Statement state = connection.createStatement();) {
            
            
            String sqlQuery = "SELECT * FROM employees";
            ResultSet result = state.executeQuery(sqlQuery);
            while (result.next()) {
                Users currentUser = new Users();
                currentUser.setUsername(result.getString("username"));
                currentUser.setPassword(result.getString("password"));
                  if (username.equals(currentUser.getUsername()) && password.equals(currentUser.getPassword())) {
                isAuthenticated = true;
                break;
            }
            }
             if (isAuthenticated) {
         
            Parent root = FXMLLoader.load(getClass().getResource("/com/fatma/project/liberery/view/FirstPage.fxml"));
            Scene scene = new Scene(root);
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close(); 
            Stage newStage = new Stage();
            newStage.setScene(scene);
            newStage.setTitle("Select option");
            newStage.show();
        } else {
            System.out.println("❌ Username or password is incorrect.");
        }

        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}
