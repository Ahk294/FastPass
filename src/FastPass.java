/*
                                            NAME: ABDUL HASEEB KHAN
                                                MISIS: M00792907
*/

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;


/*
    The FastPass class includes 2 interfaces displayed on the same scene: non-user's interface, and user's interface.
    Non-user's interface is shown by default and when no user is logged in.
    User's interface is shown when the user is logged in.
 */
public class FastPass {

//    instance variables to help in generating passwords
    static final String capCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ",         // 26 out of 26
                        lowCase = "abcdefghijklmnopqrstuvwxyz",         // 26 out of 26
                        digits = "1234567890",                          // 10 out 10
                        chs = "`~!@#$%^&*()_-+=|{}[]:;'<>?,./";         // 30 out of 32

    static String allCharacters = "",
                    displayPass;       // displayPass variable to store the randomly generated password and display it on the label

//    creating an object of the Password class in order to generate random and secure passwords using the .generatePassword() method.
    static Password password = new Password();

//    'create' method to display the scene of FastPass class on the primaryStage.
    public static Scene create(Stage stage, String username, boolean isUser){

//        setting the title and icon of the stage (the main window).
        stage.setTitle("FastPass");

//        here we declare the type of layout we want for our scene. There are many layouts to choose from.
        GridPane gp = new GridPane();
        gp.setHgap(50);
        gp.setVgap(15);
        gp.setPadding(new Insets(50, 50, 50, 50)); // top, right, bottom, left

//  ======== All the content (nodes) like buttons and text fields are declared here and added to the scene. ============

/*
*   making all the required labels and placing them in the GridPane.

*   lblPass holds the randomly generated password, therefore I have set its text to the string value of the char[] array
    returned by the .generatePassword() method of the Password class.
*/
        Label lblPass = new Label();
        lblPass.setTooltip(new Tooltip("Click to copy the password!"));
        displayPass = String.valueOf(password.generatePassword(11, "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890`~!@#$%^&*()_-+=|{}[]:;'<>?,./"));

//         char[] array returns 11 characters and 39 empty spaces due to its predefined limit in the Password class, so we substring it.
        lblPass.setText(displayPass.substring(0, 11));
        lblPass.setId("nonUserPass");
        gp.add(lblPass, 0, 0,6, 1);

        Label lblLength = new Label("Length: " + lblPass.getText().length());
        lblLength.setStyle("-fx-text-fill: #00ff00;");
        lblLength.setPrefWidth(200);
        gp.add(lblLength, 0, 5, 2, 1);

        Label lblStrength = new Label("Very Strong");
        lblStrength.setStyle("-fx-text-fill: #00ff00;");
        lblStrength.setPrefWidth(500);
        gp.add(lblStrength, 0, 9, 2, 1);

/*
*   making all the required checkboxes and placing them in the GridPane.

*   all checkboxes have been set to checked by default, this is so that the user can get a strong and randomly generated
    password from all the available characters without interacting much.
*/
        CheckBox upperCase = new CheckBox("Uppercase");
        upperCase.setSelected(true);
        gp.add(upperCase, 2, 5);

        CheckBox lowerCase = new CheckBox("Lowercase");
        lowerCase.setSelected(true);
        gp.add(lowerCase, 3, 5);

        CheckBox numbers = new CheckBox("Numbers");
        numbers.setSelected(true);
        gp.add(numbers, 4, 5);

        CheckBox symbols = new CheckBox("Symbols");
        symbols.setSelected(true);
        gp.add(symbols, 5, 5);

/*
*   making a slider to make it easier for the user to define their password's length.

*   The slider helps in fixing a max and min length for the password, thereby reducing chances of user generated errors.
*/
        Slider slider = new Slider(1, 30, 11);
        slider.setShowTickLabels(true);
        slider.setMajorTickUnit(10);
        gp.add(slider, 0, 2,6, 1);

//        calling the updatePass method to generate and display the random and secure passwords.
        updatePass(slider, upperCase, lowerCase, numbers, symbols, lblPass, lblLength, lblStrength);


//  =========================== if the user has logged in, a new scene is generated. ===================================
        if(isUser){
//            removing the non-user scene's nodes due to difference in their placement on the GridPane.
            gp.getChildren().removeAll(lblPass, slider, lblLength, upperCase, lowerCase, numbers, symbols, lblStrength);

//            adding back the removed nodes in the same scene, but at different coordinates.
            lblPass.setId("userPass");
            lblPass.setTooltip(new Tooltip("Click to edit the password!"));
            gp.add(lblPass, 0, 0,6, 1);

            lblLength.setId("userLbls");
            gp.add(lblLength, 0, 5);

            lblStrength.setId("userLbls");
            gp.add(lblStrength, 0, 6);

            upperCase.setId("userCheckbox");
            gp.add(upperCase, 2, 5);

            lowerCase.setId("userCheckbox");
            gp.add(lowerCase, 3, 5);

            numbers.setId("userCheckbox");
            gp.add(numbers, 4, 5);

            symbols.setId("userCheckbox");
            gp.add(symbols, 5, 5);

//            making a new slider to allow the user to generate passwords of length up-to 50.
            Slider userSlider = new Slider(1, 50, 11);
            userSlider.setShowTickLabels(true);
            userSlider.setMajorTickUnit(10);
            gp.add(userSlider, 0, 2,6, 1);

//            calling the updatePass method to generate and display the random and secure passwords.
            updatePass(userSlider, upperCase, lowerCase, numbers, symbols, lblPass, lblLength, lblStrength);

//            making all the required buttons and placing them in the GridPane.
            Button btnSavePass = new Button("Save It!");
            btnSavePass.setId("savePass");
            btnSavePass.setPrefWidth(150);
            btnSavePass.setPrefHeight(70);
            gp.add(btnSavePass, 4, 9, 2, 1);

            Button btnViewPass = new Button("History");
            btnViewPass.setPrefWidth(150);
            btnViewPass.setPrefHeight(70);
            gp.add(btnViewPass, 5, 9, 2, 1);

            Button btnLogOut = new Button("Log Out");
            btnLogOut.setId("logOut");
            btnLogOut.setPrefWidth(150);
            btnLogOut.setPrefHeight(70);
            gp.add(btnLogOut, 2, 9, 2, 1);

            Button btnDeleteAc = new Button("Delete\nAcc.");
            btnDeleteAc.setId("deleteAc");
            btnDeleteAc.setPrefWidth(250);
            btnDeleteAc.setPrefHeight(140);
            gp.add(btnDeleteAc, 0, 9, 2, 1);

            Button btnCopy = new Button("Copy");
            btnCopy.setPrefWidth(150);
            btnCopy.setPrefHeight(70);
            gp.add(btnCopy, 3, 9, 2, 1);

//            the EventHandler functional interface helps in handling events (actions) like button press.
            EventHandler<MouseEvent> mouseHandler = new EventHandler<MouseEvent>() {
//                overriding the handle method of the EventHandler class.
                @Override
                public void handle(MouseEvent mouseEvent) {
//              conditional statements to know which button has been pressed
/*
            when the user clicks on lblPass (the generated password), it will change into a text field with generated
            password displayed in it, this way the user would be able to copy the generated password and use it. The
            label will appear again and the text field will disappear when the user will hit 'ENTER' in the text field.
*/
                    if(mouseEvent.getSource() == lblPass){
/*
                storing the text of lblPass in a variable called 'pass' and making the lblPass invisible in order to
                display a temporary text field in its place to let the user copy the generated password. I had to
                implement this workaround because copying from a label is not possible in javafx.
*/
                        String pass = lblPass.getText();
                        lblPass.setVisible(false);

                        TextField txtPass = new TextField();
                        txtPass.setTooltip(new Tooltip("To exit the edit mode, hit 'ENTER'."));
                        txtPass.setId("userTxt");
                        txtPass.setText(pass);
                        gp.add(txtPass, 0, 0,6, 2);

/*
                disabling the slider, the save password button, and the copy button so the user can't generate and
                save/copy passwords unknowingly while in the process of editing the currently generated password.
*/
                        userSlider.setDisable(true);
                        btnSavePass.setDisable(true);
                        btnCopy.setDisable(true);

//                    interface listening for a key press on the TextField
                        txtPass.setOnKeyPressed(keyEvent -> {
                            if(keyEvent.getCode().toString().equals("ENTER")){
                                if(txtPass.getText().length() > 50){
/*
*                           trimming out access characters if the password length exceeds 50.

*                           This can happen when the user is allowed to modify the generated password. The user might
                            include extra characters to an already long password thereby making the password length
                            greater than 50.
*/
                                    lblPass.setText(txtPass.getText().substring(0, 50));

//                                    removing the text field.
                                    gp.getChildren().remove(txtPass);
                                    lblLength.setText("Length: " + lblPass.getText().length());
//                                    enabling the slider, the save password button, and the copy button to let the
//                                    user generate more passwords.
                                    userSlider.setDisable(false);
                                    btnSavePass.setDisable(false);
                                    btnCopy.setDisable(false);
//                                    making the lblPass visible again to let the user see their generated passwords.
                                    lblPass.setVisible(true);
                                } else if(txtPass.getText().isEmpty()){
                                    Alert.display("ERROR", "Please enter/generate at least 1 character.");
                                } else {
//                                    if the password length is in limit, then we do not trim the password
                                    lblPass.setText(txtPass.getText());

                                    gp.getChildren().remove(txtPass);
                                    lblLength.setText("Length: " + lblPass.getText().length());
                                    userSlider.setDisable(false);
                                    btnSavePass.setDisable(false);
                                    btnCopy.setDisable(false);
                                    lblPass.setVisible(true);
                                }
                            }
                        });
//                      when the user clicks on the Save It! button, the SavePass stage will pop up.
                    } else if(mouseEvent.getSource() == btnSavePass){
                        SavePass.display(username, lblPass.getText()); // used lblPass.getText() instead of password so that user edited pass can be stored.

//                        resetting the generated password in order to avoid saving the same password more than once.
                        displayPass = String.valueOf(password.generatePassword(11, "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890`~!@#$%^&*()_-+=|{}[]:;'<>?,./"));
                        lblPass.setText(displayPass.substring(0, 11));
                        upperCase.setSelected(true);
                        lowerCase.setSelected(true);
                        numbers.setSelected(true);
                        symbols.setSelected(true);

//                      when the user clicks on the History button, the scene will change to the History scene.
                    } else if(mouseEvent.getSource() == btnViewPass){
                        stage.setX(215);
                        stage.setY(90);
                        stage.setScene(History.create(stage, username));
//                      when the user clicks on the Log Out button, the scene will change to the FastPass non-user scene.
                    } else if(mouseEvent.getSource() == btnLogOut){
                        stage.setX(25);
                        stage.setY(90);
                        stage.setScene(FastPass.create(stage, "", false));
//                      when the user clicks on the Delete Acc. button, the DeleteUser stage will pop up for confirmation.
                    } else if(mouseEvent.getSource() == btnDeleteAc){
                        DeleteUser.delete(username);
                        stage.close();
                    } else if(mouseEvent.getSource() == btnCopy){
//                        copying the content of the label since the user is allowed to edit their password,
                        Clipboard clip = Clipboard.getSystemClipboard();
                        ClipboardContent content = new ClipboardContent();
                        content.putString(lblPass.getText());
                        clip.setContent(content);
                        Alert.display("SUCCESS", "Password Copied!");
                    }
                }
            };
//            adding events to the required nodes.
            lblPass.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseHandler);
            btnSavePass.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseHandler);
            btnViewPass.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseHandler);
            btnLogOut.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseHandler);
            btnDeleteAc.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseHandler);
            btnCopy.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseHandler);

//            returning the scene to be displayed at the primaryStage.
            Scene scene = new Scene(gp, 1300, 520);
            scene.getStylesheets().add("stylesheets/FastPass.css");
            return scene;
        } else {
//            making all the required buttons and placing them in the GridPane for non-user scene.
            Button btnLogin = new Button("Login");
            btnLogin.setPrefWidth(150);
            btnLogin.setPrefHeight(250);
            gp.add(btnLogin, 3, 9);

            Button btnRegister = new Button("Sign Up");
            btnRegister.setPrefWidth(150);
            btnRegister.setPrefHeight(250);
            gp.add(btnRegister, 5, 9);

            Button btnCopy = new Button("Copy");
            btnCopy.setPrefWidth(150);
            btnCopy.setPrefHeight(250);
            gp.add(btnCopy, 4, 9);

//            the EventHandler functional interface helps in handling events (actions) like button press.
            EventHandler<MouseEvent> mouseHandler = new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
//                    conditional statements to know which button has been pressed.
//                    when the user clicks on the Login button, the scene will change to the Login scene.
                    if(mouseEvent.getSource() == btnLogin){
                        stage.setX(425);
                        stage.setY(90);
                        stage.setScene(Login.create(stage));
//                      when the user clicks on the Register button, the scene will change to the Register scene.
                    } else if(mouseEvent.getSource() == btnRegister){
                        stage.setX(465);
                        stage.setY(60);
                        stage.setScene(Register.create(stage));
                    } else if(mouseEvent.getSource() == btnCopy){
//                        copying the generated password itself since non-users are not allowed to edit their passwords.
                        Clipboard clip = Clipboard.getSystemClipboard();
                        ClipboardContent content = new ClipboardContent();
                        content.putString(displayPass);
                        clip.setContent(content);
                        Alert.display("SUCCESS", "Password Copied!");
                    }
                }
            };
//            adding events to the required nodes.
            btnLogin.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseHandler);
            btnRegister.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseHandler);
            btnCopy.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseHandler);

//            returning the scene to be displayed at the primaryStage.
            Scene scene = new Scene(gp, 1300, 450);
            scene.getStylesheets().add("stylesheets/FastPass.css");
            return scene;
        }
    }

// updatePass method helps in generating random passwords and displaying it on the label.
    public static void updatePass(Slider slider, CheckBox uc, CheckBox lc, CheckBox num, CheckBox sym, Label lp, Label ll, Label ls) {
//        an event listener to keep a track of the length selected by the user from the slider. Listening to the value being generated on sliding the slider.
        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
/*
*       conditional statements to check which character types does the user want in their password.

*       when a character type is selected by the user through the checkboxes, it is included in one big String called
        allCharacters. If a character type is not selected by the user, then it is not included in allCharacters.
        allCharacters is then passed as an argument to the generatePassword method of the Password class along with the
        user's choice of password length.
*/
            if(uc.isSelected()){
                allCharacters += capCase;
            }
            if(lc.isSelected()){
                allCharacters += lowCase;
            }
            if(num.isSelected()){
                allCharacters += digits;
            }
            if(sym.isSelected()){
                allCharacters += FastPass.chs;
            }

//            handling the error if the user does not select any character types and decides to generate a password.
            try{
/*
        the generatePassword method returns a char[] array of randomly selected characters from the allCharacters string.
        A label's text can't be set to a char[] array, so I convert it to a String using 'String.valueOf()' and store
        its value in a string variable 'password'. lblPass's text is then set to the 'password' variable.
 */
                displayPass = String.valueOf(password.generatePassword((int) slider.getValue(), allCharacters)).substring(0, (int) slider.getValue());
                lp.setText(displayPass);
                ll.setText("Length: " + displayPass.length());
            } catch (IllegalArgumentException e){
                Alert.display("ERROR", "Please select at least one type.");
            }

//            emptying the allCharacters string so that it only includes those character types that the user selects each time.
            allCharacters = "";

//            conditional statements to inform the user about the strength of their password.
            if((int)slider.getValue() == 1){
                ll.setStyle("-fx-text-fill: #cf0000;");
                ls.setText("Very Weak");
                ls.setStyle("-fx-text-fill: #cf0000;");
            } else if((int)slider.getValue() > 1 && (int)slider.getValue() < 4){
                ll.setStyle("-fx-text-fill: #cf6100;");
                ls.setText("Weak");
                ls.setStyle("-fx-text-fill: #cf6100;");
            } else if((int)slider.getValue() >= 4 && (int)slider.getValue() < 7){
                ll.setStyle("-fx-text-fill: #dcf000;");
                ls.setText("Okay");
                ls.setStyle("-fx-text-fill: #dcf000;");
            } else if((int)slider.getValue() >= 7 && (int)slider.getValue() < 9){
                ll.setStyle("-fx-text-fill: #76c900;");
                ls.setText("Strong");
                ls.setStyle("-fx-text-fill: #76c900;");
            } else if((int)slider.getValue() >= 9){
                ll.setStyle("-fx-text-fill: #00ff00;");
                ls.setText("Very Strong");
                ls.setStyle("-fx-text-fill: #00ff00;");
            }
        });
    }
}
