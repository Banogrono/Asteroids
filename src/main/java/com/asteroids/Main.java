package com.asteroids;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.ImageCursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Main extends Application {

    public static ArrayList<String> keysPressed = new ArrayList<>();
    public static ArrayList<String> keysPressedDiscrete = new ArrayList<>();
    public static Vector mousePosition = new Vector();
    Scene mainScene;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/menu.fxml"));
        mainScene = new Scene(root, 1280, 720);
        primaryStage.setTitle("Asteroids");
        primaryStage.setScene(mainScene);

        try {
            Image crosshair = new Image(getClass().getResource("/Other_Sprites/crosshair1.png").toURI().toString());
            mainScene.setCursor(new ImageCursor(crosshair,
                    crosshair.getWidth() / 2,
                    crosshair.getHeight() /2));
        } catch (Exception e) {
            e.printStackTrace();
        }

        mainScene.setOnKeyPressed((KeyEvent e )-> {
            String keyName = e.getCode().toString();
            if (!keysPressed.contains(keyName)) {
                keysPressed.add(keyName);
                keysPressedDiscrete.add(keyName);
            }
        });

        mainScene.setOnKeyReleased((KeyEvent e )-> {
            String keyName = e.getCode().toString();
            if (keysPressed.contains(keyName)) {
                keysPressed.remove(keyName);
            }
        });

        mainScene.setOnMouseMoved((MouseEvent e) -> {
            mousePosition.set(e.getX(),e.getY());
        });

        mainScene.setOnMouseClicked((MouseEvent e) -> {
            keysPressedDiscrete.add("MOUSE_1");
        });

        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
