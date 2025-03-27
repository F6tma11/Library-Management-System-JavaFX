/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fatma.project.liberery.controller;

import com.fatma.project.liberery.dao.DBConnection;
import com.fatma.project.liberery.model.Book;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javafx.application.ConditionalFeature.FXML;
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
public class BooksPageController implements Initializable {

    /**
     * Initializes the controller class.
     */
    int index = -1;

    String query = null;

    Connection conn = null;

    PreparedStatement stat = null;

    ResultSet result = null;

    ObservableList<Book> bookList = FXCollections.observableArrayList();

    @FXML
    private TableColumn<Book, Integer> book_borrow_price;

    @FXML
    private TableColumn<Book, Integer> book_buy_price;

    @FXML
    private TableColumn<Book, Integer> book_categories;

    @FXML
    private TableColumn<Book, String> book_description;

    @FXML
    private TableColumn<Book, Integer> book_id;

    @FXML
    private TableColumn<Book, String> book_name;

    @FXML
    private TableColumn<Book, Integer> book_outher;

    @FXML
    private TableColumn<Book, Integer> book_quantity;

    @FXML
    private TableView<Book> books_table;

    @FXML
    private TextField txt_Quentity;

    @FXML
    private TextField txt_borrow_price;

    @FXML
    private TextField txt_buy_price;

    @FXML
    private TextField txt_category;

    @FXML
    private TextField txt_description;

    @FXML
    private TextField txt_name;

    @FXML
    private TextField txt_outher;

    @FXML
    private TextField txt_search;
    
    
    @FXML
    private TextField txt_id;
//-------------------------Initialize and show Customer Data----------------------------------------

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        try {
            conn = DBConnection.connect();
            loadData();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(BooksPageController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(BooksPageController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadData() throws SQLException {
        refrechTable();
        book_borrow_price.setCellValueFactory(new PropertyValueFactory<Book, Integer>("borrPrices"));
        book_buy_price.setCellValueFactory(new PropertyValueFactory<Book, Integer>("buyPrices"));
        book_description.setCellValueFactory(new PropertyValueFactory<Book, String>("description"));
        book_id.setCellValueFactory(new PropertyValueFactory<Book, Integer>("id"));
        book_name.setCellValueFactory(new PropertyValueFactory<Book, String>("name"));
        book_quantity.setCellValueFactory(new PropertyValueFactory<Book, Integer>("quantity"));
        book_categories.setCellValueFactory(new PropertyValueFactory<Book, Integer>("categoryId"));
        book_outher.setCellValueFactory(new PropertyValueFactory<Book, Integer>("outherId"));

    }

    private void refrechTable() throws SQLException {
        bookList.clear();
        query = "SELECT * FROM books";
        stat = conn.prepareStatement(query);
        result = stat.executeQuery();

        while (result.next()) {
            Book book = new Book();
            book.setId(result.getInt("id"));
            book.setName(result.getString("name"));
            book.setBuyPrices(result.getInt("buy_prices"));
            book.setBorrPrices(result.getInt("borr_prices"));
            book.setQuantity(result.getInt("quantity"));
            book.setDescription(result.getString("description"));
            book.setOutherId(result.getInt("outher_id"));
            book.setCategoryId(result.getInt("categories_id"));
            bookList.add(book);
        }
        books_table.setItems(bookList);
        books_table.refresh();

    }
//--------------------------------ADD Customer--------------------------------------------------

    @FXML
    void addBook(ActionEvent event) throws SQLException {

        query = "INSERT INTO books (name, buy_prices,borr_prices,quantity,description,outher_id,categories_id) VALUES (?,?,?,?,?,?,?)";
        stat = conn.prepareStatement(query);
        stat.setString(1, txt_name.getText().toString());
        stat.setInt(2, Integer.parseInt(txt_buy_price.getText()));
        stat.setInt(3, Integer.parseInt(txt_borrow_price.getText()));
        stat.setString(4, txt_Quentity.getText().toString());
        stat.setString(5, txt_description.getText().toString());
        stat.setInt(6, Integer.parseInt(txt_outher.getText()));
        stat.setInt(7, Integer.parseInt(txt_category.getText()));
        stat.executeUpdate();
        loadData();
        clearTextFieldData();
    }

//--------------------------------EDITE Customer------------------------------------------------


    @FXML
    void editBook(ActionEvent event) throws SQLException {
        try {
        String name = txt_name.getText().trim();
        double buy_price = Double.parseDouble(txt_buy_price.getText().trim());
        double borr_price = Double.parseDouble(txt_borrow_price.getText().trim());
        int quantity = Integer.parseInt(txt_Quentity.getText().trim());
        String description = txt_description.getText().trim();
        int outher = Integer.parseInt(txt_outher.getText().trim());
        int category = Integer.parseInt(txt_category.getText().trim());
        int id = Integer.parseInt(txt_id.getText().trim());

        query = "UPDATE books SET name=?, buy_prices=?, borr_prices=?, quantity=?, description=?, outher_id=?, categories_id=? WHERE id=?";
        
        try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setString(1, name);
            preparedStatement.setDouble(2, buy_price);
            preparedStatement.setDouble(3, borr_price);
            preparedStatement.setInt(4, quantity);
            preparedStatement.setString(5, description);
            preparedStatement.setInt(6, outher);
            preparedStatement.setInt(7, category);
            preparedStatement.setInt(8, id);

            int rowsAffected = preparedStatement.executeUpdate();
          
            
            if (rowsAffected > 0) {
                System.out.println("✅ Book updated successfully!");
                loadData();
                clearTextFieldData();
            } else {
                System.out.println("⚠️ No book was updated. Check if the ID exists.");
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    } catch (NumberFormatException e) {
        System.out.println("❌ Error: Invalid number format.");
    }

    }

    void clearTextFieldData() {
        txt_name.setText("");
        txt_buy_price.setText("");
        txt_borrow_price.setText("");
        txt_Quentity.setText("");
        txt_description.setText("");
        txt_outher.setText("");
        txt_category.setText("");
       txt_id .setText("");
    }

    @FXML
    void getSelectedColumn(MouseEvent event) {
        index = books_table.getSelectionModel().getSelectedIndex();
        if (index <= -1) {
            return;
        }
        txt_id.setText(book_id.getCellData(index).toString());
        txt_name.setText(book_name.getCellData(index).toString());
        txt_buy_price.setText(book_buy_price.getCellData(index).toString());
        txt_borrow_price.setText(book_borrow_price.getCellData(index).toString());
        txt_Quentity.setText(book_quantity.getCellData(index).toString());
        txt_description.setText(book_description.getCellData(index).toString());
        txt_category.setText(book_categories.getCellData(index).toString());
        txt_outher.setText(book_outher.getCellData(index).toString());
    }
//----------------------------Back To System Page----------------------------------------------

    @FXML
    void back_to_system(MouseEvent event) throws IOException {
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
