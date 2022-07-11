package com.example.breakout.Classes;

import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.List;

public class Ball {
    //momentum means how fast it moves in a certain direction and if its - or +
    private double xMomentum;
    private double yMomentum;
    private double x;
    private double y;
    private double radius;
    private final Circle circle;
    private int damage = 1;


    public Ball(Circle circle, double dx, double dy, double centerX, double centerY) {
        circle.setCenterX(centerX);
        circle.setCenterY(centerY);
        this.circle = circle;
        this.x = circle.getCenterX();
        this.y = circle.getCenterY();
        this.radius = circle.getRadius();
        xMomentum = dx;
        yMomentum = dy;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public void moveTo(double x, double y) {
        this.x = x;
        this.y = y;
        // System.out.println(x+" this."+this.x);
        circle.setCenterX(x);
        // System.out.println(y+" this."+this.y);
        circle.setCenterY(y);
    }

    public void resize(double radius) {
        this.radius = radius;
        circle.setRadius(radius);
    }

    public void changeMomentum(double xMomentum, double yMomentum) {
        this.xMomentum = xMomentum;
        this.yMomentum = yMomentum;
    }

    public List<Double> getMomentum() {
        List<Double> returnMe = new ArrayList<>();
        returnMe.add(xMomentum);
        returnMe.add(yMomentum);
        return returnMe;
    }

    //gets you x,y and radius in that order in a list
    public List<Double> getPositionalInfo() {
        List<Double> returnMe = new ArrayList<>();
        returnMe.add(x);
        returnMe.add(y);
        returnMe.add(radius);
        return returnMe;
    }
}