package com.example.breakout.Classes;

import com.example.breakout.ControllerScreens;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.Scene;

import java.util.*;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;


public class AnalogClock extends Application {

    final Circle circle1 = new Circle();

    final int xCENTER = 640;
    final int yCENTER = 310;
    final int radius = 200;

    //initialize clock components
    final Line hourLine = new Line(xCENTER, yCENTER, 0, 0);
    final Line minuteLine = new Line(xCENTER, yCENTER, 0, 0);
    final Line secondLine = new Line(xCENTER, yCENTER, 0, 0);

    final Line mark5 = new Line(
            (int) (Math.cos(5 * 3.14f / 30 - 3.14f / 2) * 180 + xCENTER),
            (int) (Math.sin(5 * 3.14f / 30 - 3.14f / 2) * 180 + yCENTER),
            (int) (Math.cos(5 * 3.14f / 30 - 3.14f / 2) * 200 + xCENTER),
            (int) (Math.sin(5 * 3.14f / 30 - 3.14f / 2) * 200 + yCENTER));

    final Line mark10 = new Line(
            (int) (Math.cos(10 * 3.14f / 30 - 3.14f / 2) * 180 + xCENTER),
            (int) (Math.sin(10 * 3.14f / 30 - 3.14f / 2) * 180 + yCENTER),
            (int) (Math.cos(10 * 3.14f / 30 - 3.14f / 2) * 200 + xCENTER),
            (int) (Math.sin(10 * 3.14f / 30 - 3.14f / 2) * 200 + yCENTER));

    final Line mark20 = new Line(
            (int) (Math.cos(20 * 3.14f / 30 - 3.14f / 2) * 180 + xCENTER),
            (int) (Math.sin(20 * 3.14f / 30 - 3.14f / 2) * 180 + yCENTER),
            (int) (Math.cos(20 * 3.14f / 30 - 3.14f / 2) * 200 + xCENTER),
            (int) (Math.sin(20 * 3.14f / 30 - 3.14f / 2) * 200 + yCENTER));

    final Line mark25 = new Line(
            (int) (Math.cos(25 * 3.14f / 30 - 3.14f / 2) * 180 + xCENTER),
            (int) (Math.sin(25 * 3.14f / 30 - 3.14f / 2) * 180 + yCENTER),
            (int) (Math.cos(25 * 3.14f / 30 - 3.14f / 2) * 200 + xCENTER),
            (int) (Math.sin(25 * 3.14f / 30 - 3.14f / 2) * 200 + yCENTER));

    final Line mark35 = new Line(
            (int) (Math.cos(35 * 3.14f / 30 - 3.14f / 2) * 180 + xCENTER),
            (int) (Math.sin(35 * 3.14f / 30 - 3.14f / 2) * 180 + yCENTER),
            (int) (Math.cos(35 * 3.14f / 30 - 3.14f / 2) * 200 + xCENTER),
            (int) (Math.sin(35 * 3.14f / 30 - 3.14f / 2) * 200 + yCENTER));

    final Line mark40 = new Line(
            (int) (Math.cos(40 * 3.14f / 30 - 3.14f / 2) * 180 + xCENTER),
            (int) (Math.sin(40 * 3.14f / 30 - 3.14f / 2) * 180 + yCENTER),
            (int) (Math.cos(40 * 3.14f / 30 - 3.14f / 2) * 200 + xCENTER),
            (int) (Math.sin(40 * 3.14f / 30 - 3.14f / 2) * 200 + yCENTER));

    final Line mark50 = new Line(
            (int) (Math.cos(50 * 3.14f / 30 - 3.14f / 2) * 180 + xCENTER),
            (int) (Math.sin(50 * 3.14f / 30 - 3.14f / 2) * 180 + yCENTER),
            (int) (Math.cos(50 * 3.14f / 30 - 3.14f / 2) * 200 + xCENTER),
            (int) (Math.sin(50 * 3.14f / 30 - 3.14f / 2) * 200 + yCENTER));

    final Line mark55 = new Line(
            (int) (Math.cos(55 * 3.14f / 30 - 3.14f / 2) * 180 + xCENTER),
            (int) (Math.sin(55 * 3.14f / 30 - 3.14f / 2) * 180 + yCENTER),
            (int) (Math.cos(55 * 3.14f / 30 - 3.14f / 2) * 200 + xCENTER),
            (int) (Math.sin(55 * 3.14f / 30 - 3.14f / 2) * 200 + yCENTER));

    final Label label = new Label();


    //declare dynamic end-coordinates for hourLine, minuteLine
    int xMinute, yMinute, xHour, yHour, xSecond, ySecond;

    //initialize pane and scene
    final Pane pane = new Pane();
    final Scene scene = new Scene(pane, 1280, 720);

    //create labels for analog clock
    final Label twelve = new Label();
    final Label three = new Label();
    final Label six = new Label();
    final Label nine = new Label();

    final Button button = new Button();

    public Scene getScene() {
        return scene;
    }

    @Override
    public void start(Stage primaryStage) {
        StaticClass.playSong("clock.mp3");
        scene.setRoot(pane);

        twelve.setText("12");
        twelve.setLayoutX(xCENTER - 13);
        twelve.setLayoutY(yCENTER - radius + 5);
        twelve.setFont(new Font("Arial", 22));

        three.setText("3");
        three.setLayoutX(xCENTER + radius - 20);
        three.setLayoutY(yCENTER - 20);
        three.setFont(new Font("Arial", 22));

        six.setText("6");
        six.setLayoutX(xCENTER - 7);
        six.setLayoutY(yCENTER + radius - 30);
        six.setFont(new Font("Arial", 22));

        nine.setText("9");
        nine.setLayoutX(xCENTER - radius + 8);
        nine.setLayoutY(yCENTER - 20);
        nine.setFont(new Font("Arial", 22));

        circle1.setCenterX(xCENTER);
        circle1.setCenterY(yCENTER);
        circle1.setRadius(radius);
        circle1.setFill(Color.FLORALWHITE);

        hourLine.setStroke(Color.DARKCYAN);
        minuteLine.setStroke(Color.BLACK);
        secondLine.setStroke(Color.CORAL);

        minuteLine.setStrokeWidth(2);
        hourLine.setStrokeWidth(3);

        button.setText("Zurück zum Hauptfenster");

        button.setMinWidth(200);
        button.setMaxWidth(200);

        button.setLayoutX(scene.getWidth() / 2 - 100);
        button.setLayoutY(680);

        button.setOnAction(e -> {

            try {
                ControllerScreens.switchToMain();
            } catch (Exception a) {
                System.out.println("Hauptfenster konnte nicht geladen werden.");
            }
        });

        label.setLayoutX(xCENTER - 35);
        label.setLayoutY(yCENTER);
        label.setText("Uhr-Kreativ™");
        label.setFont(Font.font("Arial"));

        pane.getChildren().addAll(circle1, hourLine, minuteLine, secondLine, twelve, three, six, nine, button,
                mark5, mark10, mark20, mark25, mark35, mark40, mark50, mark55, label);
        primaryStage.setScene(scene);

        primaryStage.setTitle("Uhr-Kreative Uhr");
        primaryStage.show();


        MyTimer timer = new MyTimer();
        timer.start();
    }

    private class MyTimer extends AnimationTimer {

        private long prevTime = 0;

        @Override
        public void handle(long now) { //runs every nano-second/only needed every second

            long deltaT = now - prevTime;
            int currentMinute;
            int currentHour;
            int currentSecond;

            if (deltaT >= 1e9) { //update clock-components every second

                prevTime = now;

                currentMinute = Calendar.getInstance().get(Calendar.MINUTE);
                currentHour = Calendar.getInstance().get(Calendar.HOUR);
                currentSecond = Calendar.getInstance().get(Calendar.SECOND);


                //xSecond = (int) (Math.cos(currentSecond * 3.14f / 30 - 3.14f / 2) * 160 + xCENTER);
                xSecond = (int) (Math.cos(3.14f / 2 - (currentSecond * 3.14f / 30)) * 160 + xCENTER);
                //ySecond = (int) (Math.sin(currentSecond * 3.14f / 30 - 3.14f / 2) * 160 + yCENTER);
                ySecond = (int) (Math.sin(3.14f / 2 - (currentSecond * 3.14f / 30)) * (-160) + yCENTER);
                //xMinute = (int) (Math.cos(currentMinute * 3.14f / 30 - 3.14f / 2) * 140 + xCENTER);
                xMinute = (int) (Math.cos(3.14f / 2 - (currentMinute * 3.14f / 30)) * 140 + xCENTER);
                //yMinute = (int) (Math.sin(currentMinute * 3.14f / 30 - 3.14f / 2) * 140 + yCENTER);
                yMinute = (int) (Math.sin(3.14f / 2 - (currentMinute * 3.14f / 30)) * (-140) + yCENTER);
                //xHour = (int) (Math.cos((currentHour * 30 + (double) (currentMinute / 2)) * 3.14f / 180 - 3.14f / 2) * 90 + xCENTER);
                xHour = (int) (Math.cos(3.14f / 2 - (currentHour*3.14f/6 + currentMinute*3.14f / 360)) * 90 + xCENTER);
                //yHour = (int) (Math.sin((currentHour * 30 + (double) (currentMinute / 2)) * 3.14f / 180 - 3.14f / 2) * 90 + yCENTER);
                yHour = (int) (Math.sin(3.14f / 2 - (currentHour*3.14f/6 + currentMinute*3.14f / 360)) * (-90) + yCENTER);
                secondLine.setEndX(xSecond);
                secondLine.setEndY(ySecond);
                minuteLine.setEndX(xMinute);
                minuteLine.setEndY(yMinute);
                hourLine.setEndX(xHour);
                hourLine.setEndY(yHour);
            }
        }
    }
}
