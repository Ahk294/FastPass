/*
                                            NAME: ABDUL HASEEB KHAN
                                                MISIS: M00792907
*/

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.sql.*;
import java.util.ArrayList;


//    History class includes only one scene.
public class History{

//    forming connection with MySQL.
    static Connection con;
    static Statement stmt;
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/users", "root", "1234");
            stmt = con.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    creating table to store passwords
    static TableView<Password> table = new TableView<>();

// .create() method to place the History scene on the PrimaryStage.
    public static Scene create(Stage stage, String username){

        stage.setTitle("History");
        GridPane gpHistory = new GridPane();
        gpHistory.setPadding(new Insets(0, 60, 50, 0)); // top, right, bottom, left

//  ======== All the content (nodes) like buttons and text fields are declared here and added to the scene. ============

//        table columns created below

        TableColumn<Password, Date> dateColumn = new TableColumn<>("Date Generated");
        dateColumn.setMinWidth(100);
//        gets all the values that are in the 'gen_date' column and places them in the 'dateColumn' column
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("gen_date"));

        TableColumn<Password, String> userNameColumn = new TableColumn<>("Username");
        userNameColumn.setMinWidth(100);
//        gets all the values that are in the 'username' column and places them in the 'usernameColumn' column
        userNameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<Password, String> webAppColumn = new TableColumn<>("Password For");
        webAppColumn.setMinWidth(200);
//        gets all the values that are in the 'web_app' column and places them in the 'webAppColumn' column
        webAppColumn.setCellValueFactory(new PropertyValueFactory<>("web_app"));

        TableColumn<Password, String> passColumn = new TableColumn<>("Passwords");
        passColumn.setMinWidth(400);
//        gets all the values that are in the 'pass' column and places them in the 'userPass' column
        passColumn.setCellValueFactory(new PropertyValueFactory<>("userPass"));

//        setting up the table

//        table = new TableView<>();
//        loading all of the data in the table. The .setItems() method takes an ObservableList as its argument.
        table.setItems(getPassword(username));
//        adding the columns we created above.
        table.getColumns().addAll(dateColumn, userNameColumn, webAppColumn, passColumn);
        gpHistory.add(table, 1, 1, 3, 1);

//        used UNICODE to display a back arrow in the button.
        Button btnBack = new Button("\uD83E\uDC78");
        btnBack.setId("back");
        gpHistory.add(btnBack, 0, 0);

        Button btnDelete = new Button();
        GridPane.setMargin(btnDelete, new Insets(20, 0, 0, 275));
        btnDelete.setText("Remove Password");
        btnDelete.setId("deletePass");
        gpHistory.add(btnDelete, 3, 2);

        Button btnRefresh = new Button();
        GridPane.setMargin(btnRefresh, new Insets(0, 0, 0, 750));
        btnRefresh.setId("refresh");
        btnRefresh.setText("↻");
        gpHistory.add(btnRefresh, 3, 0);

//        getting the cell selected by the user.
        table.getSelectionModel().setCellSelectionEnabled(true);

//        the EventHandler functional interface helps in handling events (actions) like button press.
        EventHandler<MouseEvent> mouseHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
//                conditional statements to know which button has been pressed
                if(mouseEvent.getSource() == btnBack){
                    stage.setX(25);
                    stage.setY(90);
                    stage.setScene(FastPass.create(stage, username, true));
                } else if(mouseEvent.getSource() == btnDelete){
/*
                try/catch block to handle the error of casting java.sql.Date to String. Since the conversion of these
                two data types into each other is not possible, I was getting a ClassCastException. I have handled it
                by displaying a simple message to the user to select a password instead of a date from the table.
 */
                    try {
//                        pos is an object of the TablePosition class, it holds info about the selected cell's position in the table.
                        TablePosition pos = table.getSelectionModel().getSelectedCells().get(0);
//                        using the .getRow method of the TablePosition class we get the row number of the selected cell
                        int row = pos.getRow();

/*
                    The TableView is of type Password so I am storing all the content of the selected cell's row inside a
                    Password class object called passItem.
 */
                        Password passItem = table.getItems().get(row);

//                        gets the table column of the selected cell using the .getTableColumn() method of TablePosition class.
                        TableColumn col = pos.getTableColumn();

//                        casts the content inside the selected cell to String and stores it in the string variable 'tableData'.
                        String tableData = (String) col.getCellObservableValue(passItem).getValue();

//                        separate condition if the user selects a cell from any column other than password or date.
                        if(tableData.equals(passItem.getUsername()) || tableData.equals(passItem.getWeb_app())){
                            Alert.display("ERROR", "Please select a password.");
                        } else if(tableData.equals(passItem.getUserPass())){
                            try{
//                                deleting the selected password from the database.
                                PreparedStatement pStmt = con.prepareStatement("delete from passhistory where pass = ?");
                                pStmt.setString(1, tableData);
                                pStmt.execute();

//                                success message displayed upon successful deletion of the password.
                                Alert.display("SUCCESS", " The password '" + tableData + "' has been deleted.");
                            }
                            catch (Exception e){
                                Alert.display("ERROR", "An error has occurred.");
                            }
                        }
                    } catch(Exception e){
                        Alert.display("ERROR", "Please select a password.");
                    }
                } else if(mouseEvent.getSource() == btnRefresh){
//                    setting all the Password objects of a user in the table.
                    table.setItems(getPassword(username));
                    table.refresh();
                }
            }
        };
//        adding events to the required nodes.
        btnBack.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseHandler);
        btnDelete.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseHandler);
        btnRefresh.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseHandler);

//        returning the scene to be displayed at the primaryStage.
        Scene scene = new Scene(gpHistory, 925, 450);
        scene.getStylesheets().add("stylesheets/History.css");
        return scene;
    }

//    ObservableList to store Password objects and get all the passwords
    public static ObservableList<Password> getPassword(String username){
        ObservableList<Password> passwordObservableList = FXCollections.observableArrayList();

//        creating an ArrayList to hold the user's passwords
        ArrayList<Password> passwords = new ArrayList<Password>();

        try{
//            retrieving the user's data from the database.
            ResultSet rs = stmt.executeQuery("select * from PassHistory where username = '"+username+"';");
            while(rs.next()){
//                creating one object for each password generated by the user.
                Password pass = new Password();
                pass.setGen_date(rs.getDate("Gen_Date"));
                pass.setUsername(rs.getString("Username"));
                pass.setWeb_app(rs.getString("Web_app"));
                pass.setUserPass(rs.getString("Pass"));

//                adding newly created object to the ObservableList 'passwordObservableList'.
                passwordObservableList.add(pass);

//                adding the passwords to the ArrayList.
                passwords.add(pass);
            }
//                adding the generated password history of the user to a file for quick and easy reference.
            Password.fileIO(passwords, username);

            Alert.display("SUCCESS", "A copy of your password history is saved at: C:\\Users\\hasee\\Desktop\\FP Users\\"+ username +" History.txt");
        }
        catch (Exception e){
            Alert.display("ERROR", "An error has occurred.");
        }
//        returning the ObservableList to be passed as an argument in the .setItems method of the TableView class.
        return passwordObservableList;
    }
}
