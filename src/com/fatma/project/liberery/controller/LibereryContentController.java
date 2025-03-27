/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fatma.project.liberery.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author A M
 */
public class LibereryContentController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    void goToBooksPage(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("/com/fatma/project/liberery/view/BooksPage.fxml"));
        Scene scene = new Scene(root);
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
        Stage newStage = new Stage();
        newStage.setScene(scene);
        newStage.setTitle("Books");
        newStage.show();
    }

    @FXML
    void arrow_back(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/fatma/project/liberery/view/FirstPage.fxml"));
        Scene scene = new Scene(root);
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
        Stage newStage = new Stage();
        newStage.setScene(scene);
        newStage.setTitle("First page");
        newStage.show();
    }

    @FXML
    void goToCustomerPage(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/fatma/project/liberery/view/Customer.fxml"));
        Scene scene = new Scene(root);
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
        Stage newStage = new Stage();
        newStage.setScene(scene);
        newStage.setTitle("Customer");
        newStage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}
