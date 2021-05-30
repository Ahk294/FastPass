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

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;


//    Login class includes only one scene.
public class Login{

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

// The .create() method places the login scene onto the primaryStage which it receives as an argument.
    public static Scene create(Stage stage){

        stage.setTitle("Login");

        GridPane gp = new GridPane();
        gp.setHgap(20);
        gp.setVgap(15);
        gp.setPadding(new Insets(0, 50, 50, 0)); // top, right, bottom ,left

//  ======== All the content (nodes) like buttons and text fields are declared here and added to the scene. ============

        Label lblImg = new Label();
//        img object of Image class to load the image we want to display.
        Image img = new Image("images/userLogin.png");
        GridPane.setMargin(lblImg, new Insets(0, 0, 0, 115));
//        view object of ImageView class to 'paint' the loaded image onto the layout
        ImageView view = new ImageView(img);
        view.setFitHeight(100);
//        important for balancing the ratio.
        view.setPreserveRatio(true);
//        using the .setGraphic() method of a label we can place an image on the layout easily.
        lblImg.setGraphic(view);
        gp.add(lblImg, 1, 1);

        TextField txtUserName = new TextField();
        txtUserName.setPromptText("Username");
        gp.add(txtUserName, 1, 2);

        PasswordField txtPass = new PasswordField();
        txtPass.setPromptText("Password");
        gp.add(txtPass, 1, 3);

        Button btnLogin = new Button("Login");
        GridPane.setMargin(btnLogin, new Insets(10, 0, 5, 65));
        btnLogin.setPrefWidth(200);
        gp.add(btnLogin, 1, 4,2, 1);

        Button btnBack = new Button("\uD83E\uDC78");
        btnBack.setId("back");
        gp.add(btnBack, 0, 0);

        Hyperlink forgotPass = new Hyperlink("Forgot Password?");
        GridPane.setMargin(forgotPass, new Insets(0, 0, 0, 105));
        gp.add(forgotPass, 1, 5);

//        the EventHandler functional interface helps in handling events (actions) like button press.
        EventHandler<MouseEvent> mouseHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
//                conditional statements to know which button has been pressed
                if(mouseEvent.getSource() == btnLogin){
//                    conditional statements to make sure the user enters information in all fields.
                    if(txtUserName.getText().isEmpty() && txtPass.getText().isEmpty()){
                        txtUserName.setPromptText("Please Enter Username");
                        txtPass.setPromptText("Please Enter Password");
                    } else if(txtUserName.getText().isEmpty()){
                        txtUserName.setPromptText("Please Enter Username");
                    } else if(txtPass.getText().isEmpty()){
                        txtPass.setPromptText("Please Enter Password");
                    } else {
                        try{
//                            retrieving the user's information.
                            ResultSet rs = stmt.executeQuery("select username, pass from Userinfo where username = '"+txtUserName.getText()+"' and pass = '"+txtPass.getText()+"';");
//                            checking the user's credentials
                            if(rs.next()){
                                stage.setX(25);
                                stage.setY(90);
                                stage.setScene(FastPass.create(stage, txtUserName.getText(), true));
                            } else {
                                Alert.display("ERROR", "The username or password you entered is incorrect.\nPlease try again.");
                            }
                        } catch (Exception e){
                            System.out.println(e.getMessage());
                        }
                    }
//                    when the user clicks on the Forgot Password hyperlink.
                } else if(mouseEvent.getSource() == forgotPass){
                    if(txtUserName.getText().isEmpty()){
                        txtUserName.setPromptText("Please Enter Username");
                    } else {
                        try{
                            ResultSet rs = stmt.executeQuery("select * from Userinfo where username = '"+txtUserName.getText()+"';");
                            if(rs.next()){
//                                sending an e-mail to the registered user's e-mail address with their password.
                                sendMail(rs.getString("Email"), rs.getString("Pass"));
                                Alert.display("IMPORTANT", "An E-mail with your password has been sent at '" + rs.getString("email") + "'.");
                            } else {
                                Alert.display("IMPORTANT", "Entered username is incorrect.");
                            }
                        } catch (Exception e){
                            System.out.println(e.getMessage());
                        }
                    }
//                    when the user clicks on the Back button (arrow).
                } else if(mouseEvent.getSource() == btnBack){
                    stage.setX(25);
                    stage.setY(90);
                    stage.setScene(FastPass.create(stage, "", false));
                }
            }
        };
//        adding events to the required nodes.
        btnLogin.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseHandler);
        forgotPass.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseHandler);
        btnBack.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseHandler);

//        returning the scene to be displayed on the primaryStage.
        Scene scene = new Scene(gp, 485, 470);
        scene.getStylesheets().add("stylesheets/Login.css");
        return scene;
    }


//    the .sendMail() method helps us in sending an e-mail to the user in case they forget their password.
    public static void sendMail(String userMail, String userPass){
//        creating a properties object to connect to the gmail smtp server
        Properties properties = new Properties();
//        mail.smtp.auth attempts to authenticate the user trying to use the auth command if it is set to true.
        properties.put("mail.smtp.auth", "true");
//        enabling the starttls command. This command helps in upgrading to a secure connection from an insecure one.
        properties.put("mail.smtp.starttls.enable", "true");
//        connecting to the gmail smtp server.
        properties.put("mail.smtp.host", "smtp.gmail.com");
//        specifying the smtp port number we want to connect to.
        properties.put("mail.smtp.port", "587");

//        creating a session instance to authenticate the user by using the PasswordAuthentication class
        Session session = Session.getDefaultInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("fastpassbyahk@gmail.com", "FastPass,.12");
            }
        });

        try{
//            creating the message itself using session
            Message message = new MimeMessage(session);
//            adding the necessary info to the message object that are required in an email
//            e-mail address of the sender.
            message.setFrom(new InternetAddress("fastpassbyahk@gmail.com"));
//            e-mail address of the receiver.
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(userMail));
//            subject of the mail.
            message.setSubject("Your Password");
//            content of the mail.
            message.setText("Your password is: " + userPass);
//            sending the e-mail.
            Transport.send(message);
        } catch(MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
