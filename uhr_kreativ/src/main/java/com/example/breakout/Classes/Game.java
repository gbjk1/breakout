package com.example.breakout.Classes;

import com.example.breakout.Application;
import com.example.breakout.ControllerScreens;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class Game {
    private Ball ball;
    private Bar bar;
    private Level level;
    private List<PowerUp> powerUp;
    private double bombCounter;

    public Game() {
        ball = null;
        bar = null;
        level = null;
        powerUp = new ArrayList<>();
    }

    public Game(Level level) {
        this();
        this.level = level;
    }

    public double getBombCounter() {
        return bombCounter;
    }

    public void increaseBombCounter() {
        if(bombCounter < 3) {
            bombCounter++;
        }
    }

    public void decreaseBombCounter() {
        bombCounter--;
    }

    public void setPowerUp(List<PowerUp> powerUp) {
        this.powerUp = powerUp;
    }

    public void setBall(Ball ball) {
        this.ball = ball;
    }

    public void setBar(Bar bar) {
        this.bar = bar;
    }

    public Level getLevel() {
        return level;
    }

    public Ball getBall() {
        return ball;
    }

    public List<PowerUp> getPowerUp() {
        return powerUp;
    }

    public PowerUp moveDown() throws FileNotFoundException {
        for (PowerUp powerUp : getPowerUp()) {
            powerUp.moveTo(3);
            if (bar.checkItem(powerUp.getPositionalInfo())) {
                this.powerUp.remove(powerUp);
                Scene scene = Application.stage.getScene();

                if (powerUp.getId() == 3 || powerUp.getId() == 4) {
                    increaseBombCounter();

                } else {
                    switch (powerUp.randomID()) {
                        case (0):
                            if (ball.getPositionalInfo().get(2) != 10) {
                                ball.resize(10);
                            } else {
                                ball.resize(20);
                            }
                            break;
                        case (1):
                            if (ball.getPositionalInfo().get(2) != 10) {
                                ball.resize(10);
                            } else {
                                ball.resize(25);
                            }
                            break;
                        case (2):
                            if (ball.getPositionalInfo().get(2) != 10) {
                                ball.resize(10);
                            } else {
                                ball.resize(15);
                            }
                            break;
                        case (3):
                            if (ball.getPositionalInfo().get(2) != 10) {
                                ball.resize(10);
                            } else {
                                ball.resize(35);
                            }
                            break;
                        case (4):
                            if (ball.getPositionalInfo().get(2) != 10) {
                                ball.resize(10);
                            } else {
                                ball.resize(30);
                            }
                            break;
                        case (5):
                            if (bar.getWidth() != 100) {
                                bar.resize(100);
                            } else {
                                bar.resize(150);
                            }
                            break;
                        case (6):
                            if (bar.getWidth() != 100) {
                                bar.resize(100);
                            } else {
                                bar.resize(50);
                            }
                            break;
                        case (7):
                            if (bar.getWidth() != 100) {
                                bar.resize(100);
                            } else {
                                bar.resize(200);
                            }
                            break;
                        case (8):
                            if (bar.getWidth() != 100) {
                                bar.resize(100);
                            } else {
                                bar.resize(170);
                            }
                            break;
                        case (9):
                            if ((ball.getMomentum().get(0) != 2 || ball.getMomentum().get(0) != -2) &&
                                    (ball.getMomentum().get(1) != 2 || ball.getMomentum().get(1) != -2)) {

                                if (ball.getMomentum().get(0) < 0) {
                                    if (ball.getMomentum().get(1) < 0) {
                                        ball.changeMomentum(-2, -2);
                                    } else {
                                        ball.changeMomentum(-2, 2);
                                    }
                                } else {
                                    if (ball.getMomentum().get(1) < 0) {
                                        ball.changeMomentum(2, -2);
                                    } else {
                                        ball.changeMomentum(2, 2);
                                    }
                                }
                            } else {
                                ball.changeMomentum(ball.getMomentum().get(0) * 3, ball.getMomentum().get(1) * 3);
                            }
                            break;
                        case (10):
                            if ((ball.getMomentum().get(0) != 2 || ball.getMomentum().get(0) != -2) &&
                                    (ball.getMomentum().get(1) != 2 || ball.getMomentum().get(1) != -2)) {

                                if (ball.getMomentum().get(0) < 0) {
                                    if (ball.getMomentum().get(1) < 0) {
                                        ball.changeMomentum(-2, -2);
                                    } else {
                                        ball.changeMomentum(-2, 2);
                                    }
                                } else {
                                    if (ball.getMomentum().get(1) < 0) {
                                        ball.changeMomentum(2, -2);
                                    } else {
                                        ball.changeMomentum(2, 2);
                                    }
                                }
                            } else {
                                ball.changeMomentum(ball.getMomentum().get(0) * 2, ball.getMomentum().get(1) * 2);
                            }
                            break;
                        case (11):
                            if (ball.getDamage() != 1) {
                                ball.setDamage(1);
                            } else {
                                ball.setDamage(2);
                            }
                            break;
                        case (12):
                            StaticClass.playSound("HappyYeah.wav");
                            Application.stage.setScene(new Scene(new Group(new ImageView(new Image(new FileInputStream((new File("").getAbsolutePath()) + "\\src\\main\\resources\\Item\\Apfel.png")))), 1280, 720));
                            new Timeline(new KeyFrame(Duration.seconds(0.5), e -> Application.stage.setScene(scene))).play();
                            break;
                    }
                }
                return powerUp;
            } else if (powerUp.getPositionalInfo().get(1) >= ControllerScreens.windowHeight) {
                this.powerUp.remove(powerUp);
                return powerUp;
            }
        }
        return null;
    }

    public void moveBall() {
        List<Double> momentum = ball.getMomentum();
        List<Double> position = ball.getPositionalInfo();

        ball.moveTo( position.get(0) + momentum.get(0),
                     position.get(1) + momentum.get(1));

        if (ball.getPositionalInfo().get(0) <= 0) {
            ball.moveTo(ball.getPositionalInfo().get(2), ball.getPositionalInfo().get(1));
        }
        if (ball.getPositionalInfo().get(0) >= 1000) {
            ball.moveTo(1000 - ball.getPositionalInfo().get(2), ball.getPositionalInfo().get(1));
        }
        if (ball.getPositionalInfo().get(1) <= 0) {
            ball.moveTo(ball.getPositionalInfo().get(0), ball.getPositionalInfo().get(2));
        }
    }

    public void moveBar(double xchange) {
        bar.move(xchange);
    }

    // return true: not lost
    // return false: player lost game (ball touched bottom side)
    public boolean checkBall() throws FileNotFoundException {
        List<Double> position = ball.getPositionalInfo();
        List<Double> momentum = ball.getMomentum();

        for (Block block : level.getBlocks()) {
            int touches = block.checkBlock(position);
            // ball touches block on left or right side
            if (touches == 1 || touches == 2) {
                ball.changeMomentum((momentum.get(0) * -1), momentum.get(1));
                block.lowerHP(ball.getDamage());
                if (block.getStrength() <= 0) {
                    level.removeBlock(block);
                    powerUp.add(new PowerUp(block.getX() + block.getWidth() / 2 - 32, block.getY() + block.getHeight() / 2 - 32));
                }
                return true;
            } else if (touches == 3 || touches == 4) { // ball touches block on top or bottom side
                ball.changeMomentum(momentum.get(0), (momentum.get(1) * -1));
                block.lowerHP(ball.getDamage());
                if (block.getStrength() <= 0) {
                    level.removeBlock(block);
                    powerUp.add(new PowerUp(block.getX() + block.getWidth() / 2 - 32, block.getY() + block.getHeight() / 2 - 32));
                }
                return true;
            }
        }

        // ball touches left or right side of the window
        if (position.get(0) - position.get(2) <= 0 && momentum.get(0) < 0) {
            ball.changeMomentum((momentum.get(0) * -1), momentum.get(1));
            return true;
        } else if (position.get(0) + position.get(2) >= 1000 && momentum.get(0) > 0) {
            ball.changeMomentum((momentum.get(0) * -1), momentum.get(1));
            return true;
        }

        // ball touches top or bottom side of the window
        if (position.get(1) - position.get(2) == 0 && momentum.get(1) < 0) {
            ball.changeMomentum(momentum.get(0), (momentum.get(1) * -1));
            return true; // left or right
            //moveBall();
        } else if (position.get(1) - position.get(2) >= 720) {
            bar.stop();
            return false; // lost
        }
        // ball touches bar
        else if (bar.checkBar(position)) {
            ball.changeMomentum(momentum.get(0), (momentum.get(1) * -1));
            return true; // touch bar
        }
        return true;
    }
}
