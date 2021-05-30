/*
                                            NAME: ABDUL HASEEB KHAN
                                                MISIS: M00792907
*/

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;


// App class inheriting from Application class which provides a framework for managing a JavaFX application.
public class App extends Application {

    public static void main(String[] args) {
//        since the main method is always called first, the launch(args) method helps in launching the JavaFX app.
        launch(args);
    }

//    start method is the main entry point for all the JavaFx apps.
    @Override
    public void start(Stage primaryStage) throws Exception{

//        stage -> scene -> layout -> nodes

//        stage is the main window with the min, max, and exit button.
//        scene is what holds all the content, buttons, etc (nodes). on it.
        primaryStage.setScene(FastPass.create(primaryStage, "", false));
        primaryStage.getIcons().add(new Image("images/logo.png"));
        primaryStage.setX(25);
        primaryStage.setY(90);
        primaryStage.show();
    }
}
