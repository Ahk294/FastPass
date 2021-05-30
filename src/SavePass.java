/*
                                            NAME: ABDUL HASEEB KHAN
                                                MISIS: M00792907
*/

import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.*;


//    SavePass class is a separate stage, it acts like a pop up.
public class SavePass{

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

    public static void display(String username, String pass){

//        we create a new stage for asking the user which website or application is this password for. This also gives it a pop up effect.
        Stage popUp = new Stage();
        popUp.getIcons().add(new Image("images/success.png"));

        GridPane gpSave = new GridPane();
        gpSave.setPadding(new Insets(20, 20, 20, 20));
        gpSave.setHgap(15);
        gpSave.setVgap(15);

//        does not let the user ignore the message.
        popUp.initModality(Modality.APPLICATION_MODAL);
        popUp.setTitle("Save Password");
        popUp.setMinWidth(300);

//  ======== All the content (nodes) like buttons and text fields are declared here and added to the scene. ============

        Label lblWebAppName = new Label("Password for:");
        gpSave.add(lblWebAppName, 0, 0);

        Label lblPass = new Label("Password:");
        gpSave.add(lblPass, 0, 1);

        TextField txtWebAppName = new TextField();
        txtWebAppName.setId("savePassFields");
        txtWebAppName.setPromptText("ex: Facebook username");
        gpSave.add(txtWebAppName, 1, 0);

        TextField txtPass = new TextField();
        txtPass.setId("savePassFields");
        txtPass.setText(pass);
        txtPass.setEditable(false);
        gpSave.add(txtPass, 1, 1);

        Button btnSave = new Button("Save");
        GridPane.setHalignment(btnSave, HPos.CENTER);
        gpSave.add(btnSave, 0, 2, 2, 1);

//        the EventHandler functional interface helps in handling events (actions) like button press.
        EventHandler<MouseEvent> mouseHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
//                conditional statements to know which button has been pressed
                if(mouseEvent.getSource() == btnSave){
//                    conditional statements to make sure each password is linked to some website or application account.
                    if(txtWebAppName.getText().isEmpty()){
                        txtWebAppName.setPromptText("*REQUIRED*");
                    } else {
//                        passing the current time in milliseconds in the Date construct to get the current date.
                        long millis = System.currentTimeMillis();
                        Date date = new Date(millis);

                        try{
//                            inserting the user's password into the password database.
                            PreparedStatement pStmt = con.prepareStatement("insert into PassHistory values (?, ?, ?, ?)");
                            pStmt.setDate(1, date);
                            pStmt.setString(2, username);
                            pStmt.setString(3, txtWebAppName.getText());
                            pStmt.setString(4, txtPass.getText());
                            pStmt.executeUpdate();

                            popUp.close();
                        } catch (Exception e){
                            Alert.display("ERROR", e.getMessage());
                        }
                    }
                }
            }
        };
//        adding the event to btnSave.
        btnSave.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseHandler);

//        setting the scene on the SavePass pop up stage.
        Scene scene = new Scene(gpSave);
        scene.getStylesheets().add("stylesheets/PopUp.css");
        popUp.setScene(scene);
//        this will keep showing the window until the user closes the window by pressing the button.
        popUp.showAndWait();
    }
}
