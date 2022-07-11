package com.example.breakout;

import com.example.breakout.Classes.StaticClass;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class Application extends javafx.application.Application {

    private static final int windowHeight = 720;
    private static final int windowWidth = 1280;

    public static Stage stage;

    public static Stage getStage() {
        return stage;
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("MainScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), windowWidth, windowHeight);

        Application.stage = stage;
        stage.setTitle("Hauptfenster");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        StaticClass.readLocalSettings();
        StaticClass.playSong("titlescreen.mp3");

        // das braucht nur klausberger, bitte nicht löschen
        //((Button)scene.lookup("#lvlEditBtn")).fire();
    }

    public static void main(String[] args) {
        launch();
    }
}