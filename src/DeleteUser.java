/*
                                            NAME: ABDUL HASEEB KHAN
                                                MISIS: M00792907
*/

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;


//    DeleteUser class is a separate stage, it acts like a pop up.
public class DeleteUser{

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

//    .delete() method shows the pop up for confirming user account deletion
    public static void delete(String username){

//        we create a new stage for confirming the deletion of the account. This gives the pop up effect to it.
        Stage popUp = new Stage();
        popUp.getIcons().add(new Image("images/warning.png"));

        GridPane gp = new GridPane();
        gp.setPadding(new Insets(20, 20, 20, 20));
        gp.setHgap(15);
        gp.setVgap(15);

//        does not let the user ignore the message.
        popUp.initModality(Modality.APPLICATION_MODAL);
        popUp.setTitle("WARNING");
        popUp.setMinWidth(300);

//  ======== All the content (nodes) like buttons and text fields are declared here and added to the scene. ============

//        lblImp is the warning image in the pop up window.
        Label lblImp = new Label();
//        img object of Image class to load the image we want to display.
        Image imgImp = new Image("images/warning.png");
//        view object of ImageView class to 'paint' the loaded image onto the layout
        ImageView viewImp = new ImageView(imgImp);
        viewImp.setFitHeight(75);
//        important for balancing the ratio.
        viewImp.setPreserveRatio(true);
//        using the .setGraphic() method of a label we can place an image on the layout easily.
        lblImp.setGraphic(viewImp);
        gp.add(lblImp, 0, 0);

        Label lblAsk = new Label("Are You Sure You Want To Delete The Account '"  + username + "' ?");
        gp.add(lblAsk, 1, 0);

        Button btnYes = new Button("Yes");
        btnYes.setId("btnYes");
        gp.add(btnYes, 1, 1);
        GridPane.setMargin(btnYes, new Insets(0, 0, 0, 140));

//        using a lambda expression to add a button click event to btnYes.
        btnYes.setOnAction(yesEvent -> {
            try {
//                deleting all the passwords of the user (who is deleting the account) from the password database.
                PreparedStatement pStmt1 = con.prepareStatement("delete from passhistory where username = ?");
                pStmt1.setString(1, username);
                pStmt1.execute();

//                deleting the user from the user database.
                PreparedStatement pStmt2 = con.prepareStatement("delete from userinfo where username = ?");
                pStmt2.setString(1, username);
                pStmt2.execute();

//                success message displayed upon successful deletion of the password and user from the databases.
                Alert.display("SUCCESS", username + " account has been deleted.");
                popUp.setX(25);
                popUp.setY(90);
                popUp.setScene(FastPass.create(popUp, "", false));
                popUp.getIcons().add(new Image("images/logo.png"));
            } catch (Exception e) {
                Alert.display("ERROR", "An error has occurred.");
            }
        });

        Button btnNo = new Button("No");
        btnNo.setId("btnNo");
        gp.add(btnNo, 1, 1);
        GridPane.setMargin(btnNo, new Insets(0, 0, 0, 260));

//        using a lambda expression to add a button click event to btnNo.
        btnNo.setOnAction(noEvent -> {
            popUp.setX(25);
            popUp.setY(90);
            popUp.setScene(FastPass.create(popUp, username, true));
            popUp.getIcons().add(new Image("images/logo.png"));
        });

//        setting the scene on the pop up stage for confirming deletion of the user's account.
        Scene scene = new Scene(gp);
        scene.getStylesheets().add("stylesheets/PopUp.css");
        popUp.setScene(scene);
//        this will show the window to the user.
        popUp.show();
    }
}

