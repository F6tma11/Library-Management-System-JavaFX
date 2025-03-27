/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fatma.project.liberery.controller;

import com.fatma.project.liberery.dao.DBConnection;
import com.fatma.project.liberery.model.Book;
import com.fatma.project.liberery.model.CustomerOrder;
import com.fatma.project.liberery.model.People;
import com.sun.deploy.util.FXLoader;
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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author A M
 */
public class BuyingOrBorrowingController implements Initializable {

    /**
     * Initializes the controller class.
     */
    private static int price = 0;
    @FXML
    private TableView<CustomerOrder> borrow_book_table;
    @FXML
    private TableColumn<CustomerOrder, Integer> book_id;

    @FXML
    private TableColumn<CustomerOrder, String> book_name;

    @FXML
    private TableColumn<CustomerOrder, Integer> borrow_price;

    @FXML
    private TableColumn<CustomerOrder, String> borr_phone_number;

    @FXML
    private TableView<People> customer_table;
    @FXML
    private TableColumn<People, String> co_username;
    @FXML
    private TableColumn<People, String> col_phonenumber;
    @FXML
    private TableColumn<People, String> col_address;

    @FXML
    private TableView<Book> book_data_table;

    @FXML
    private TableColumn<Book, Integer> col_book_borrw;

    @FXML
    private TableColumn<Book, String> col_book_description;

    @FXML
    private TableColumn<Book, Integer> col_book_id;

    @FXML
    private TableColumn<Book, String> col_book_name;

    @FXML
    private TextField txt_book;

    String query = "";
    DBConnection conn;
    Connection connection;
    PreparedStatement prep;
    double totalPrice = 0;

    ObservableList<CustomerOrder> orderData = FXCollections.observableArrayList();
    ObservableList<People> customer = FXCollections.observableArrayList();
    ObservableList<Book> bookData = FXCollections.observableArrayList();

    ResultSet result = null;

    @FXML
    private TextField txt_phone_number;

    @FXML
    private TextField txt_total_price;

    int index = -1;

    @FXML
    void back_btn(ActionEvent event) {

    }

    @FXML
    void clear_btn(ActionEvent event) {
        orderData.clear();
        txt_total_price.clear();
        txt_phone_number.clear();
        bookData.clear();
        customer.clear();
    }

     static int number ;

     void userHistory()throws SQLException{
           query = "SELECT number FROM number_of_books_borrow WHERE people_phoneNumber = ?";
        prep = connection.prepareStatement(query);
        prep.setString(1, txt_phone_number.getText().trim());
        result = prep.executeQuery();

        if (result.next()) {
            number = result.getInt("number");
        }
      
     }
    @FXML
    void execute_btn(ActionEvent event) throws SQLException {
      
        if(number>=3){
             JOptionPane.showMessageDialog(null, "This client owns three books, which is the maximum allowed.");
             return;
        }

        String querybookData = "SELECT name, borr_prices FROM books WHERE id = ?";
        prep = connection.prepareStatement(querybookData);
        prep.setInt(1, Integer.parseInt(txt_book.getText().trim()));
        ResultSet result2 = prep.executeQuery();

        if (result2.next()) {
            String bookName = result2.getString("name");
            int borrowPrice = result2.getInt("borr_prices");
            if (insertBorrowBook(bookName, borrowPrice) > 0) {
                JOptionPane.showMessageDialog(null, "Book borrowed successfully!");
                updateNumBorrow();
                loadBorrowBooks();
            } else {
                JOptionPane.showMessageDialog(null, "Failed to borrow the book.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Book not found.");
        }
    }

    void updateNumBorrow() throws SQLException {
        query = "UPDATE number_of_books_borrow SET number=? WHERE people_phoneNumber = ?";
        prep = connection.prepareStatement(query);
        prep.setInt(1, number);
        prep.setString(2, txt_phone_number.getText().trim());
        prep.executeUpdate();
    }

    int insertBorrowBook(String bookName, int borrowPrice) throws SQLException {
        String queryInsert = "INSERT INTO borrow_books (people_phoneNumber, books_id, book_name, borrow_price) VALUES (?, ?, ?, ?)";
        prep = connection.prepareStatement(queryInsert);
        prep.setString(1, txt_phone_number.getText().trim());
        prep.setInt(2, Integer.parseInt(txt_book.getText().trim()));
        prep.setString(3, bookName);
        prep.setInt(4, borrowPrice);
        number++;
        totalPrice+=borrowPrice;
        txt_total_price.setText(totalPrice+"");
        int rowsAffected = prep.executeUpdate();
        return rowsAffected;

    }

    private void loadCustomerData() throws SQLException {
        customerData();
        co_username.setCellValueFactory(new PropertyValueFactory<>("name"));
        col_phonenumber.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        col_address.setCellValueFactory(new PropertyValueFactory<>("address"));

    }

    public void customerData() throws SQLException {
        query = "SELECT name, address FROM people WHERE phoneNumber=?";
        prep = connection.prepareStatement(query);
        prep.setString(1, txt_phone_number.getText().toString());
        result = prep.executeQuery();
        if (result.next()) {
            People people = new People();
            people.setName(result.getString("name"));
            people.setAddress(result.getString("address"));
            people.setPhoneNumber(txt_phone_number.getText().toString());
            customer.add(people);
            loadBorrowBooks();
        }
        customer_table.setItems(customer);

    }

    private void loadBookData(int bookID) throws SQLException {
        BookData(bookID);
        col_book_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_book_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        col_book_description.setCellValueFactory(new PropertyValueFactory<>("description"));
        col_book_borrw.setCellValueFactory(new PropertyValueFactory<>("borrPrices"));

    }

    public void BookData(int bookID) throws SQLException {
        query = "SELECT name, borr_prices,description FROM books WHERE id=?";
        prep = connection.prepareStatement(query);
        prep.setInt(1, bookID);
        result = prep.executeQuery();
        if (result.next()) {
            Book book = new Book();
            book.setName(result.getString("name"));
            book.setDescription(result.getString("description"));
            book.setBorrPrices(result.getInt("borr_prices"));
            book.setId(Integer.parseInt(txt_book.getText().toString()));
            bookData.add(book);
        }
        book_data_table.setItems(bookData);

    }

    public void loadBorrowBooks() throws SQLException {
        borrowBooks();
        borr_phone_number.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        book_id.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        book_name.setCellValueFactory(new PropertyValueFactory<>("bookname"));
        borrow_price.setCellValueFactory(new PropertyValueFactory<>("borrowPrice"));
    }

    public void borrowBooks() throws SQLException {
        orderData.clear();
        query = "SELECT books_id, book_name,borrow_price FROM borrow_books WHERE people_phoneNumber=?";
        prep = connection.prepareStatement(query);
        prep.setString(1, txt_phone_number.getText().toString());
        result = prep.executeQuery();
        while (result.next()) {
            CustomerOrder order = new CustomerOrder();
            order.setBookId(result.getInt("books_id"));
            order.setBookname(result.getString("book_name"));
            order.setBorrowPrice(result.getInt("borrow_price"));
            order.setPhoneNumber(txt_phone_number.getText().toString());
            orderData.add(order);
        }
        borrow_book_table.setItems(orderData);
    }

    void lisenTophoneText(){
         txt_phone_number.textProperty().addListener((observable, oldValue, newValue) -> {
                try {
                    searchPeople();
                } catch (SQLException ex) {
                    Logger.getLogger(BuyingOrBorrowingController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
    }
    void LisenToTextBook(){
          txt_book.textProperty().addListener((observablebook, oldbook, newValuebook) -> {
                try {
                    searchBook(Integer.parseInt(txt_book.getText().toString()));
                } catch (SQLException ex) {
                    Logger.getLogger(BuyingOrBorrowingController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            // TODO
            txt_total_price.setText(totalPrice + "");
            connection = conn.connect();
            lisenTophoneText();
            LisenToTextBook();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(BuyingOrBorrowingController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(BuyingOrBorrowingController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void searchPeople() throws SQLException {
        String searchText = txt_phone_number.getText().toString();
        String query = "SELECT name FROM people WHERE phoneNumber LIKE ? ";
        prep = connection.prepareStatement(query);
        prep.setString(1, "%" + searchText + "%");
        result = prep.executeQuery();
        if (result.next()) {
            txt_phone_number.setStyle("-fx-text-inner-color: green;");
            loadCustomerData();
            userHistory();
        } else {
            txt_phone_number.setStyle("-fx-text-inner-color: red;");
        }

    }

    void searchBook(int bookID) throws SQLException {
        query = "SELECT quantity From books WHERE id LIKE ? ";
        prep = connection.prepareStatement(query);
        prep.setString(1, "%" + bookID + "%");

        result = prep.executeQuery();

        if (result.next()) {
            if (result.getInt("quantity") <= 0) {
                txt_book.setStyle("-fx-text-inner-color: blue;");
            } else {
                txt_book.setStyle("-fx-text-inner-color: green;");
                loadBookData(bookID);
            }
        } else {
            txt_book.setStyle("-fx-text-inner-color: red;");
        }

    }

    @FXML
    void back_from_buying(MouseEvent event) throws IOException {
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
    void retrive_btn(ActionEvent event) throws SQLException {
        String queryDelete = "DELETE FROM borrow_books WHERE people_phoneNumber = ? AND books_id = ?";

        prep = connection.prepareStatement(queryDelete);
        prep.setString(1, txt_phone_number.getText().trim());
        prep.setInt(2, Integer.parseInt(txt_book.getText().trim()));

        int rowsAffected = prep.executeUpdate();

        if (rowsAffected > 0) {
            JOptionPane.showMessageDialog(null, "Book record deleted successfully!");
            number-=1;
            updateNumBorrow();
            loadBorrowBooks();
        } else {
            JOptionPane.showMessageDialog(null, "No matching record found to delete.");
        }
    }

}
