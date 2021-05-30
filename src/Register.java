/*
                                            NAME: ABDUL HASEEB KHAN
                                                MISIS: M00792907
*/

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.sql.*;


//    Register class includes only one scene.
public class Register{

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

// .create() method to place the Register scene on the PrimaryStage.
    public static Scene create(Stage stage){

        stage.setTitle("Register");
//        here we declare the type of layout we want for our scene. There are many layouts to choose from.
        GridPane gp = new GridPane();
        gp.setHgap(20);
        gp.setVgap(15);
        gp.setPadding(new Insets(0, 50, 50, 0)); // top, right, bottom, left

//  ======== All the content (nodes) like buttons and text fields are declared here and added to the scene. ============

//        lblImg is the user registration icon.
        Label lblImg = new Label();
//        img object of Image class to load the image we want to display.
        Image img = new Image("images/userRegister.png");
        GridPane.setMargin(lblImg, new Insets(0, 0, 0, 95));
//        view object of ImageView class to 'paint' the loaded image onto the layout
        ImageView view = new ImageView(img);
        view.setFitHeight(100);
//        important for balancing the ratio.
        view.setPreserveRatio(true);
//        using the .setGraphic() method of a label we can place an image on the layout easily.
        lblImg.setGraphic(view);
        gp.add(lblImg, 1, 1);

//        imgRight shows a tick mark to tell the user that they have confirmed their password.
        Image imgRight = new Image("images/success.png");
        ImageView viewRight = new ImageView(imgRight);
        viewRight.setFitHeight(30);
        viewRight.setFitWidth(30);
        viewRight.setPreserveRatio(true);

//        imgWrong shows a cross mark to tell the user that they have not confirmed the password.
        Image imgWrong = new Image("images/error.png");
        ImageView viewWrong = new ImageView(imgWrong);
        viewWrong.setFitHeight(30);
        viewWrong.setFitWidth(30);
        viewWrong.setPreserveRatio(true);

//        lblConfirm is the confirm password icon, it switches between imgRight and imgWrong based on what the user inputs.
        Label lblConfirm = new Label();
        gp.add(lblConfirm, 2, 5);

        TextField txtName = new TextField();
        txtName.setPromptText("Full Name");
        gp.add(txtName, 1, 2);

        TextField txtUserName = new TextField();
        txtUserName.setPromptText("Username");
        gp.add(txtUserName, 1, 3);

        PasswordField txtPass = new PasswordField();
        txtPass.setPromptText("Password");
        gp.add(txtPass, 1, 4);

        PasswordField txtConfPass = new PasswordField();
        txtConfPass.setPromptText("Confirm Password");
        gp.add(txtConfPass, 1, 5);

        TextField txtEmail = new TextField();
        txtEmail.setPromptText("Email");
        gp.add(txtEmail, 1, 6);

        Button btnRegister = new Button("Register");
        GridPane.setMargin(btnRegister, new Insets(0, 0, 0, 80));
        gp.add(btnRegister, 1, 7);

//        UNICODE of a back arrow is used here.
        Button btnBack = new Button("\uD83E\uDC78");
        btnBack.setId("back");
        gp.add(btnBack, 0, 0);

        Hyperlink isCurUser = new Hyperlink("Already have an account? Click here");
        GridPane.setMargin(isCurUser, new Insets(0, 0, 0, 11));
        gp.add(isCurUser, 1, 8);

//        the EventHandler functional interface helps in handling events (actions) like button press.
        EventHandler<MouseEvent> mouseHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
//                conditional statements to know which button has been pressed
                if(mouseEvent.getSource() == btnRegister){
//                    conditional statements to make sure the user enters information in all fields.
                    if(txtName.getText().isEmpty()){
                        txtName.setPromptText("Please Enter Full Name");
                    } else {
                        if(txtUserName.getText().isEmpty()){
                            txtUserName.setPromptText("Please Enter Username");
                        } else {
                            try{
//                                checking if the username exists or not.
                                ResultSet rs = stmt.executeQuery("select * from Userinfo where username = '"+txtUserName.getText()+"';");
                                while (rs.next()){
                                    if(txtUserName.getText().equals(rs.getString("username"))){
                                        Alert.display("ERROR", "Username '" + txtUserName.getText() + "' already exists.");
                                    }
                                }
                            } catch (Exception e){
                                System.out.println();
                            }
                            if(txtPass.getText().isEmpty()){
                                txtPass.setPromptText("Please Enter Password");
                                lblConfirm.setVisible(false);
                            } else {
                                if(txtConfPass.getText().isEmpty()){
                                    txtConfPass.setPromptText("Please Confirm Password");
                                    lblConfirm.setVisible(false);
                                } else {
                                    if(!txtConfPass.getText().equals(txtPass.getText())){
                                        lblConfirm.setVisible(true);
                                        lblConfirm.setGraphic(viewWrong);
                                    } else {
                                        lblConfirm.setGraphic(viewRight);
                                        if(txtEmail.getText().isEmpty()){
                                            txtEmail.setPromptText("Please Enter Email");
                                        } else {
                                            try {
                                                Class.forName("com.mysql.cj.jdbc.Driver");
//                                                inserting the new registered user's information in the database.
                                                PreparedStatement pStmt = con.prepareStatement("insert into Userinfo values (?, ?, ?, ?)");
                                                pStmt.setString(1, txtName.getText());
                                                pStmt.setString(2, txtUserName.getText());
                                                pStmt.setString(3, txtPass.getText());
                                                pStmt.setString(4, txtEmail.getText());
                                                pStmt.executeUpdate();
//                                                success message upon successfully registering the user.
                                                Alert.display("SUCCESS", "Your account has been successfully created.\nWelcome " + txtUserName.getText() + "!");
                                                con.close();
                                                stage.setX(425);
                                                stage.setY(90);
                                                stage.setScene(Login.create(stage));
                                            } catch (Exception e) {
                                                System.out.println();
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
//                    when the user clicks on the forgot password hyperlink.
                } else if(mouseEvent.getSource() == isCurUser){
                    stage.setX(445);
                    stage.setY(90);
                    stage.setScene(Login.create(stage));
//                    when the user click on the back button.
                } else if(mouseEvent.getSource() == btnBack){
                    stage.setX(25);
                    stage.setY(90);
                    stage.setScene(FastPass.create(stage, "", false));
                }
            }
        };
//        adding events to the required nodes.
        btnRegister.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseHandler);
        isCurUser.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseHandler);
        btnBack.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseHandler);

//        returning the Register scene to be displayed on the Primary Stage.
        Scene scene = new Scene(gp, 440, 570);
        scene.getStylesheets().add("stylesheets/Register.css");
        return scene;
    }
}
