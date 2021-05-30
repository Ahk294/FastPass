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
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;


/*
    Alert class is a separate stage, it acts like a pop up. I have used this class to give errors, warnings, and success
    messages to the users.
 */
public class Alert{
    public static void display(String title, String msg){

//        we create a new stage with a general layout that suits all types of situations.
        Stage popUp = new Stage();

        GridPane gpAlert = new GridPane();
        gpAlert.setHgap(15);
        gpAlert.setVgap(15);
        gpAlert.setPadding(new Insets(20, 20, 20, 20));

//        does not let the user ignore the message.
        popUp.initModality(Modality.APPLICATION_MODAL);
        popUp.setTitle(title);
        popUp.setMinWidth(300);

//        conditional statements for checking the type of alert we need to show, these change the icon of the alert.
        if(title.equals("SUCCESS")){
            popUp.getIcons().add(new Image("images/success.png"));
            Label lblSuccess = new Label();
            Image imgSuc = new Image("images/success.png");
            ImageView viewSuc = new ImageView(imgSuc);
            viewSuc.setFitHeight(75);
            viewSuc.setPreserveRatio(true);
            lblSuccess.setGraphic(viewSuc);
            gpAlert.add(lblSuccess, 0, 0);
        } else if(title.equals("ERROR")){
            popUp.getIcons().add(new Image("images/error.png"));
            Label lblError = new Label();
            Image imgErr = new Image("images/error.png");
            ImageView viewErr = new ImageView(imgErr);
            viewErr.setFitHeight(75);
            viewErr.setPreserveRatio(true);
            lblError.setGraphic(viewErr);
            gpAlert.add(lblError, 0, 0);
        } else if(title.equals("IMPORTANT")){
            popUp.getIcons().add(new Image("images/warning.png"));
            Label lblWarn = new Label();
            Image imgWarn = new Image("images/warning.png");
            ImageView viewWarn = new ImageView(imgWarn);
            viewWarn.setFitHeight(75);
            viewWarn.setPreserveRatio(true);
            lblWarn.setGraphic(viewWarn);
            gpAlert.add(lblWarn, 0, 0);
        }

//        a general label that displays whatever message is passed to the Alert class as an argument.
        Label lblMsg = new Label();
        lblMsg.setText(msg);
        gpAlert.add(lblMsg, 1, 0);

        Button btnOk = new Button("OK");
        GridPane.setHalignment(btnOk, HPos.CENTER);
        gpAlert.add(btnOk, 0, 1, 2, 1);
        btnOk.setOnAction(e -> popUp.close());

        TextField txtMsg = new TextField();
        txtMsg.setVisible(false);

//        the EventHandler functional interface helps in handling events (actions) like button press.
        EventHandler<MouseEvent> mouseHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
//                conditional statements to know which button has been pressed
//                optional feature for letting the user copy the displayed message. Useful for errors.
                if(mouseEvent.getSource() == lblMsg){
                    lblMsg.setVisible(false);
                    txtMsg.setVisible(true);
                    txtMsg.setText(msg);
                    gpAlert.add(txtMsg, 1, 0);

//                    interface listening for a key press on the TextField
                    txtMsg.setOnKeyPressed(keyEvent -> {
                        if(keyEvent.getCode().toString().equals("ENTER")){
//                                removing the text field.
                            gpAlert.getChildren().remove(txtMsg);

                            lblMsg.setVisible(true);
                        }
                    });
                }
            }
        };
//        adding the event to the label.
        lblMsg.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseHandler);

//        setting the scene on the Alert stage.
        Scene scene = new Scene(gpAlert);
        scene.getStylesheets().add("stylesheets/PopUp.css");
        popUp.setScene(scene);
//        this will keep showing the window until the user closes the window by pressing the button.
        popUp.showAndWait();
    }
}