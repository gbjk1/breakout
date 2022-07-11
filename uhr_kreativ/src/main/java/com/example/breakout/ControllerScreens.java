package com.example.breakout;

import com.example.breakout.Classes.*;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


public class ControllerScreens {
    public static final int windowHeight = 720;
    private static final int windowWidth = 1280;

    private static double scroll;
    private static double scrollableHeight;


    public void changePictureMainScreen(MouseEvent e) throws FileNotFoundException {
        String pathToPicture = "";

        if (MouseEvent.MOUSE_ENTERED == e.getEventType()) {
            if (((Button) e.getSource()).getId().equals("ButtonStart")) {
                pathToPicture = "BreakoutButtonStart.png";
            } else if (((Button) e.getSource()).getId().equals("ButtonEditor")) {
                pathToPicture = "BreakoutButtonEditor.png";
            } else if (((Button) e.getSource()).getId().equals("ButtonOptions")) {
                pathToPicture = "BreakoutButtonOptions.png";
            } else if (((Button) e.getSource()).getId().equals("ButtonSecret")) {
                pathToPicture = "BreakoutButtonSecret.png";
            }
        } else {
            pathToPicture = "Breakout.png";
        }
        // because File("") ist empty .getAbsolutePath() returns current directory path
        String dirPath = new File("").getAbsolutePath();
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            dirPath += "\\src\\main\\resources\\Background\\";
        } else {
            dirPath += "/src/main/resources/Background/";
        }
        dirPath += pathToPicture;

        ImageView imageView = (ImageView) scene.lookup("#imageMain");
        Image image = new Image(new FileInputStream(dirPath));
        imageView.setImage(image);

    }

    @FXML
    AnchorPane mainPaneLevelScreen;

    public void showClockScreen() {
        AnalogClock clock = new AnalogClock();
        clock.start(Application.getStage());
    }

    public static void switchToMain() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("MainScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), windowWidth, windowHeight);

        Application.stage.setTitle("Hauptfenster");
        Application.stage.setScene(scene);
        Application.stage.setResizable(false);
        Application.stage.show();
        StaticClass.playSong("titlescreen.mp3");
    }

    public void switchToMain2() throws IOException {
        // Called by a button to go back to the main, as Static methods can't be used by on-action in fxml
        switchToMain();
    }

    public void switchToLevels() throws IOException { // called by button "Start"
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("LevelsScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), windowWidth, windowHeight);

        Application.stage.setTitle("Levelauswahl");
        Application.stage.setScene(scene);
        Application.stage.setResizable(false);
        Application.stage.show();

        mainPaneLevelScreen = (AnchorPane) scene.lookup("#mainPaneLevelScreen");

        //  set needed instance variables and on click action of back to main window-Button
        Button backToMain = new Button("Zurück zum Hauptfenster");

        backToMain.setLayoutX(65);
        backToMain.setLayoutY(38);

        backToMain.setMinHeight(30);
        backToMain.setMaxHeight(30);

        backToMain.addEventHandler(MouseEvent.MOUSE_CLICKED, (e -> {
            try {
                ControllerScreens.switchToMain();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }));

        //  set needed instance variables of search-TextField
        TextField searchField = new TextField();

        searchField.setLayoutX(1005);
        searchField.setLayoutY(38);

        searchField.setMinHeight(30);
        searchField.setMaxHeight(30);

        searchField.setMinWidth(100);
        searchField.setMaxWidth(100);

        //  set needed instance variables and on click action of search-Button
        Button searchBtn = new Button("Suchen");

        searchBtn.setLayoutX(1115);
        searchBtn.setLayoutY(38);

        searchBtn.setMinHeight(30);
        searchBtn.setMaxHeight(30);

        searchBtn.setMinWidth(100);
        searchBtn.setMaxWidth(100);

        //  reload Levels and filter by content of search-TextField on search-Button-click
        searchBtn.setOnMouseClicked(e -> {
            for (int i = mainPaneLevelScreen.getChildren().size() - 1; i >= 0; i--) {
                Node n = mainPaneLevelScreen.getChildren().get(i);
                if (n.getClass().getSimpleName().equals("AnchorPane")) {
                    mainPaneLevelScreen.getChildren().remove(n);
                }
            }

            showLevels(searchField.getText());
        });

        //  add defined Buttons and TextField to AnchorPane
        mainPaneLevelScreen.getChildren().addAll(backToMain, searchField, searchBtn);

        //  load all Levels without filtering by search-TextField
        showLevels("");

        //  set needed variable and add properties to all objects on main-AnchorPane
        scroll = 0;

        for (int i = mainPaneLevelScreen.getChildren().size() - 1; i >= 0; i--) {
            Node n = mainPaneLevelScreen.getChildren().get(i);
            n.getProperties().put("originalY", n.getLayoutY());
        }

        //  add action for scrolling
        mainPaneLevelScreen.setOnScroll(e -> {
            //  stop scrolling if on top of the page and on bottom, keep scrolling if not
            if (scroll - e.getDeltaY() <= 0) {
                scroll = 0;
            } else if (scroll - e.getDeltaY() >= scrollableHeight) {
                scroll = scrollableHeight;
            } else {
                scroll += e.getDeltaY() * -1;
            }

            //  reset coordinates of all objects after scroll
            for (int i = mainPaneLevelScreen.getChildren().size() - 1; i >= 0; i--) {
                Node n = mainPaneLevelScreen.getChildren().get(i);
                if (!n.getClass().getSimpleName().equals("ImageView")) {
                    n.setLayoutY((double) n.getProperties().get("originalY") - scroll);
                }
            }
        });
    }

    //  function to add all saved Levels (depending on search-TextField) as new AnchorPane to main-AnchorPane
    public void showLevels(String filterBy) {
        //  load all saved Levels from file
        Level[] levels = Level.loadLevelList();
        if (levels != null) {
            //  place all Levels as new AnchorPane and set coordinates for 4 Levels each row
            int x = 65;
            int y = 90;

            int levelCount = 0;

            for (int i = 0; i < levels.length; i++) {
                Level level = levels[i];

                if (level != null && level.getName().toLowerCase().contains(filterBy.toLowerCase())) {
                    levelCount++;

                    AnchorPane pane = new AnchorPane();

                    pane.setMinWidth(250);
                    pane.setMaxWidth(250);

                    pane.setMinHeight(200);
                    pane.setMaxHeight(200);

                    pane.setLayoutX(x);
                    pane.setLayoutY(y);

                    if ((i + 1) % 4 == 0) {
                        x = 65;
                        y += 200 + 50;
                    } else {
                        x += 250 + 50;
                    }

                    game = new Game(level);
                    loadBlocks(0.25, pane);

                    Label label = new Label(level.getName());
                    AnchorPane.setLeftAnchor(label, 0.0);
                    AnchorPane.setRightAnchor(label, 0.0);
                    AnchorPane.setBottomAnchor(label, 0.0);
                    AnchorPane.setTopAnchor(label, 0.0);
                    label.setAlignment(Pos.BOTTOM_CENTER);
                    pane.getChildren().add(label);

                    //  add action to start game on selection a level
                    pane.setOnMouseClicked(e -> {
                        if (e.getButton() == MouseButton.PRIMARY) {
                            try {
                                game = new Game(level);
                                switchToGame();
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }
                    });

                    //  add context-menu (opens on right-click) to allow user to play, delete and edit each level
                    pane.setOnContextMenuRequested(e -> {
                        ContextMenu contextMenu = new ContextMenu();
                        MenuItem playItem = new MenuItem("Spielen");
                        playItem.setOnAction(ev -> {
                            try {
                                game = new Game(level);
                                switchToGame();
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        });

                        MenuItem editItem = new MenuItem("Bearbeiten");
                        editItem.setOnAction(ev -> {
                            try {
                                LevelEditorController.level = level;
                                switchToLevelEditor();
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        });

                        MenuItem deleteItem = new MenuItem("Löschen");
                        deleteItem.setOnAction(ev -> {
                            Level.deleteLevel(level.getName());
                            for (int j = mainPaneLevelScreen.getChildren().size() - 1; j >= 0; j--) {
                                Node n = mainPaneLevelScreen.getChildren().get(j);
                                if (n.getClass().getSimpleName().equals("AnchorPane")) {
                                    mainPaneLevelScreen.getChildren().remove(n);
                                }
                            }

                            showLevels(filterBy);
                        });

                        contextMenu.getItems().addAll(playItem, editItem, deleteItem);
                        contextMenu.show(pane, e.getScreenX(), e.getScreenY());
                    });

                    String backGroundPath = new File("").getAbsolutePath();
                    backGroundPath = backGroundPath.replace('\\', '/');
                    backGroundPath += "/src/main/resources/Background/LeftSide.png";

                    String basicStyle = "-fx-border-color: black;" +
                            "-fx-background-image: url('file:" + backGroundPath + "');" +
                            "-fx-background-size: 250 200;";

                    //  restyle border for hover-action
                    pane.setOnMouseEntered(e -> pane.setStyle(basicStyle + "-fx-border-width: 5px;"));

                    pane.setOnMouseExited(e -> pane.setStyle(basicStyle + "-fx-border-width: 2px;"));

                    pane.setStyle(basicStyle + "-fx-border-width: 2px;");

                    mainPaneLevelScreen.getChildren().add(pane);
                }
            }

            scrollableHeight = y + (double) (((levelCount - 1) / 4) + 1) * 250 - 720;

            if (scrollableHeight < 0) {
                scrollableHeight = 0;
            }
        }
    }

    @FXML
    Button musicButton;

    @FXML
    Button soundButton;

    public void switchToSettings() throws IOException { // called by button "Settings"
        StaticClass.playSong("settings.mp3");
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("SettingsScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), windowWidth, windowHeight);

        Application.stage.setTitle("Einstellungen");
        Application.stage.setScene(scene);
        Application.stage.setResizable(false);
        Application.stage.show();

        this.musicButton = (Button) scene.lookup("#musicButton");   // -> the Music button
        if (!StaticClass.isMusicSetting()) {
            musicButton.setText("Musik einschalten");
        }
        this.soundButton = (Button) scene.lookup("#soundButton");   // -> the Sound button
        if (!StaticClass.isSoundSetting()) {
            soundButton.setText("Ton einschalten");
        }
    }

    public void changeMusicSetting() throws IOException { // Called by button "Music button" to turn music on or off
        StaticClass.setMusicSetting(!StaticClass.isMusicSetting());
        if (StaticClass.isMusicSetting()) {
            musicButton.setText("Musik ausschalten");
        } else {
            musicButton.setText("Musik einschalten");
        }
    }

    public void changeSoundSetting() throws IOException { // Called by button "Sound button" to turn Sound on or off
        StaticClass.setSoundSetting(!StaticClass.isSoundSetting());
        if (StaticClass.isSoundSetting()) {
            soundButton.setText("Ton ausschalten");
        } else {
            soundButton.setText("Ton einschalten");
        }
    }

    public void switchToLevelEditor() throws IOException { // called by button "Level Editor"
        StaticClass.playSong("Leveleditor.mp3");
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("LevelEditorScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), windowWidth, windowHeight);
        Application.stage.setTitle("Level-Editor");
        Application.stage.setScene(scene);
        Application.stage.setResizable(false);
        Application.stage.show();
    }

    private Game game;// = new Game();

    @FXML
    Button againButton;
    @FXML
    Button backButton;

    public void openEnd() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("EndScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 500, 300);
        Stage stageEnd = new Stage();

        stageEnd.setTitle("Ende");
        stageEnd.setScene(scene);
        stageEnd.setResizable(false);

        double x = Application.stage.getX();
        double y = Application.stage.getY();

        stageEnd.setX(x + 250);
        stageEnd.setY(y + 180);

        stageEnd.initOwner(Application.stage);
        stageEnd.initModality(Modality.APPLICATION_MODAL);

        stageEnd.show();

        Button againButton = (Button) scene.lookup("#againButton");
        Button backButton = (Button) scene.lookup("#backButton");

        stageEnd.setOnCloseRequest(ev -> {
            try {
                openEnd();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        backButton.setOnAction(e -> {
            try {
                switchToMain2();
                stageEnd.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        });

        againButton.setOnAction(e -> {
            try {
                stageEnd.close();
                gameStart = false;
                gameStartLock = false;
                game = new Game(Level.loadLevel(game.getLevel().getName()));
                switchToGame();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }

    public void switchToGame() throws IOException { // called by button "level [id]"

        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("GameScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), windowWidth, windowHeight);

        Application.stage.setTitle("Spielfenster");
        Application.stage.setScene(scene);
        Application.stage.setResizable(false);
        Application.stage.show();

        Button button = (Button) scene.lookup("#back");
        button.setOnMousePressed(e -> timeline.stop());

        // with scene.lookup linking to fx:id
        this.scene = (AnchorPane) scene.lookup("#scene");          // -> scene in which the gameplay is done
        this.circle = (Circle) scene.lookup("#circle");            // --> the ball
        this.rectangle = (Rectangle) scene.lookup("#rectangle");   // -> the bar
        this.highScore = (Label) scene.lookup("#highScore");       // -> score

        game.setBall(new Ball(circle,
                0,
                0,
                rectangle.getLayoutX() + rectangle.getWidth() / 2,
                rectangle.getLayoutY() - rectangle.getHeight()));
        // dx: dy: --> momentum
        // centerX: centerY: --> initial positional info
        // no initial momentum for the ball -> after pressing "B" method changeMomentum is called

        game.setBar(new Bar(rectangle));
        loadBlocks(1, this.scene);

        this.imageViewRightStrength = (ImageView) scene.lookup("#imageViewRightStrength");
        this.imageViewRightSpeed = (ImageView) scene.lookup("#imageViewRightSpeed");
        this.imageViewRightBar = (ImageView) scene.lookup("#imageViewRightBar");
        this.imageViewRightBall = (ImageView) scene.lookup("#imageViewRightBall");
        this.imageViewRightBombs = (ImageView) scene.lookup("#imageViewRightBombs");

        EventHandler<KeyEvent> handler = (key) -> {
            // listening to KeyEvent's
            //BarDirectX can be moved or changed depending on how it feels
            if (key.getCode() == KeyCode.B && !gameStartLock) {
                timeline.play();
                gameStart = true;
                gameStartLock = true;
                // locking the ability to press B multiple times
                game.getBall().changeMomentum(2, -2);
                // momentum is given
                // why x, -y? cause top left corner is 0, 0
                createdMillis = System.currentTimeMillis();
                // used in getAgeInSeconds() for the score (time needed)
                // -> starts only if B is pressed, so players have the "freedom" to
                // position the bar wherever they want before the timer starts
            }

            if (key.getCode() == KeyCode.A) {
                // keycodes == keyboard input
                //System.out.println("Bar Left in Controller"); // console output for testing
                if (this.rectangle.getLayoutX() <= 0) {
                    // looks if the bar "rectangle" is out of bounds and
                    // adjusts it's x-value
                    this.rectangle.setLayoutX(0);
                } else {
                    game.moveBar(-BarDirectX);
                    if (!gameStart) {
                        // boolean value -> looks if game has started
                        // it is changed if the key B is pressed
                        game.getBall().moveTo((game.getBall().getPositionalInfo().get(0) - BarDirectX),
                                game.getBall().getPositionalInfo().get(1));
                        // if the key B hasn't been pressed yet
                        // changes the ball's x-value corresponding to the bar's x-value
                        // --> "ball stays on top of bar"
                    }
                }
            }

            if (key.getCode() == KeyCode.D) {
                // keycodes == keyboard input
                //System.out.println("Bar Right in Controller"); // console output for testing
                if (this.rectangle.getLayoutX() + this.rectangle.getWidth() >= 1000) {
                    // looks if the bar "rectangle" is out of bounds and
                    // adjusts it's x-value
                    this.rectangle.setLayoutX(1000 - rectangle.getWidth());
                } else {
                    game.moveBar(+BarDirectX);
                    if (!gameStart) {
                        // boolean value -> looks if game has started
                        // it is changed if the key B is pressed
                        game.getBall().moveTo((game.getBall().getPositionalInfo().get(0) + BarDirectX),
                                game.getBall().getPositionalInfo().get(1));
                        // if the key B hasn't been pressed yet
                        // changes the ball's x-value corresponding to the bar's x-value
                        // --> "ball stays on top of bar"
                    }
                }
            }

            if (key.getCode() == KeyCode.H) {
                if (bombCounter > 0 && gameStart) {
                    bombExplosion();
                }
            }
        };
        scene.addEventHandler(KeyEvent.KEY_PRESSED, handler);
        timeline.setCycleCount(Animation.INDEFINITE);
    }

    public void bombExplosion() {
        game.decreaseBombCounter();

        double width;

        width = 128 * (1 + 0.2 * ((game.getBall().getPositionalInfo().get(2) - 10)/ 5));

        double ballX = game.getBall().getPositionalInfo().get(0) - (width / 2);
        double ballY = game.getBall().getPositionalInfo().get(1) - (width / 2);

        String dirPath = new File("").getAbsolutePath();
        dirPath += "\\src\\main\\resources\\Explosion\\Explosion";

        Timeline timeline1;
        Timeline timeline2 = null;

        String imagePath;
        for (int i = 1; i <= 8; i++) {
            imagePath = i + ".png";
            ImageView imageView = new ImageView();

            try {
                Image image = new Image(new FileInputStream(dirPath + imagePath));

                imageView.setImage(image);
                imageView.setLayoutX(ballX);
                imageView.setLayoutY(ballY);

                imageView.setFitHeight(width);
                imageView.setFitWidth(width);

                timeline1 = (new Timeline(new KeyFrame(Duration.seconds(0.07), ev -> scene.getChildren().add(imageView))));
                if (timeline2 != null) {
                    Timeline finalTimeline1 = timeline1;
                    timeline2.setOnFinished(e ->
                            finalTimeline1.play()
                    );
                } else {
                    timeline1.play();
                }
                timeline2 = new Timeline(new KeyFrame(Duration.seconds(0.07), ev -> scene.getChildren().remove(imageView)));
                Timeline finalTimeline = timeline2;
                timeline1.setOnFinished(e ->
                        finalTimeline.play()
                );

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        for (int i = scene.getChildren().size() - 1; i >= 0; i--) {
            // iterates to the number of children the scene has
            Node n = scene.getChildren().get(i);
            if (n.getClass().getSimpleName().equals("Rectangle") && n != rectangle) {
                Block b = game.getLevel().findBlock(((Rectangle) n).getX(), ((Rectangle) n).getY());

                if (((b.getX() >= ballX && b.getX() + b.getWidth() <= ballX + width) ||
                        (b.getX() <= ballX && b.getX() + b.getWidth() >= ballX) ||
                        (b.getX() <= ballX + width && b.getX() + b.getWidth() >= ballX + width))
                        && ((b.getY() >= ballY && b.getY() + b.getHeight() <= ballY + width) ||
                        (b.getY() <= ballY && b.getY() + b.getWidth() >= ballY) ||
                        (b.getY() <= ballY + width && b.getY() + b.getWidth() >= ballY + width))) {
                    scene.getChildren().remove(n);
                    game.getLevel().removeBlock(b);
                }
            }
        }


    }

    @FXML
    ImageView imageViewRightStrength;
    @FXML
    ImageView imageViewRightSpeed;
    @FXML
    ImageView imageViewRightBar;
    @FXML
    ImageView imageViewRightBall;
    @FXML
    ImageView imageViewRightBombs;

    public void changePictureNumbers() throws FileNotFoundException {
        String dirPath0 = new File("").getAbsolutePath();
        String dirPath1 = "", dirPath2 = "", dirPath3 = "", dirPath4 = "", dirPath5 = "";

        dirPath1 += dirPath0 + "\\src\\main\\resources\\Number\\";
        dirPath2 += dirPath0 + "\\src\\main\\resources\\Number\\";
        dirPath3 += dirPath0 + "\\src\\main\\resources\\Number\\";
        dirPath4 += dirPath0 + "\\src\\main\\resources\\Number\\";
        dirPath5 += dirPath0 + "\\src\\main\\resources\\Number\\";

        switch (game.getBall().getDamage()) {
            case (1) -> dirPath1 += "Times1.png";
            case (2) -> dirPath1 += "Times2.png";
        }

        Double aDouble = game.getBall().getMomentum().get(0);
        if (Math.abs(aDouble) == 1) {
            dirPath2 += "Times1.png";
        } else if (Math.abs(aDouble) == 2) {
            dirPath2 += "Times2.png";
        } else if (Math.abs(aDouble) == 3) {
            dirPath2 += "Times3.png";
        }

        switch ((int) rectangle.getWidth()) {
            case (50) -> dirPath3 += "Number50.png";
            case (100) -> dirPath3 += "Number100.png";
            case (150) -> dirPath3 += "Number150.png";
            case (170) -> dirPath3 += "Number170.png";
            case (200) -> dirPath3 += "Number200.png";
        }

        Double i = game.getBall().getPositionalInfo().get(2);
        if (i == 10) {
            dirPath4 += "Number10.png";
        } else if (i == 15) {
            dirPath4 += "Number15.png";
        } else if (i == 20) {
            dirPath4 += "Number20.png";
        } else if (i == 25) {
            dirPath4 += "Number25.png";
        } else if (i == 30) {
            dirPath4 += "Number30.png";
        } else if (i == 35) {
            dirPath4 += "Number35.png";
        }

        switch ((int) game.getBombCounter()) {
            case (0) -> dirPath5 += "Times0.png";
            case (1) -> dirPath5 += "Times1.png";
            case (2) -> dirPath5 += "Times2.png";
            case (3) ->  dirPath5 += "Times3.png";
        }

        Image imageStrength = new Image(new FileInputStream(dirPath1));
        imageViewRightStrength.setImage(imageStrength);

        Image imageSpeed = new Image(new FileInputStream(dirPath2));
        imageViewRightSpeed.setImage(imageSpeed);

        Image imageBar = new Image(new FileInputStream(dirPath3));
        imageViewRightBar.setImage(imageBar);

        Image imageBall = new Image(new FileInputStream(dirPath4));
        imageViewRightBall.setImage(imageBall);

        Image imageBombs = new Image(new FileInputStream(dirPath5));
        imageViewRightBombs.setImage(imageBombs);
    }


    public boolean checkBall() throws FileNotFoundException {
        boolean b = game.checkBall();

        for (PowerUp powerUp : game.getPowerUp()) {
            if (!scene.getChildren().contains(powerUp.getImageView())) {
                scene.getChildren().add(powerUp.getImageView());
            }
        }

        for (int i = scene.getChildren().size() - 1; i >= 0; i--) {
            // iterates to the number of children the scene has
            Node n = scene.getChildren().get(i);
            if (n.getClass().getSimpleName().equals("Rectangle")) {
                // filtering -> we are only interested in rectangles
                Rectangle r = (Rectangle) n;
                if (//game.getLevel().findBlock(r.getX(), r.getY()) == null &&
                        r != rectangle) {
                    // && r != rectangle -> the bar is also a rectangle, so we need to take it out
                    scene.getChildren().remove(r);
                }
            }
        }

        loadBlocks(1, this.scene);
        return b;
    }

    private double bombCounter;
    private Boolean gameStart = false;
    private Boolean gameStartLock = false;
    private long createdMillis;
    private final double BarDirectX = 15;

    @FXML
    private AnchorPane scene; // scene in which the gameplay is done
    @FXML
    private Circle circle; // circle == ball
    @FXML
    private Rectangle rectangle; // rectangle == bar
    @FXML
    private Label highScore = new Label();

    // 1 Frame evey 10 millis, which means 100 FPS
    Timeline timeline = new Timeline(new KeyFrame(Duration.millis(10), new EventHandler<>() {
        @Override
        public void handle(ActionEvent actionEvent) {
            // methods used while timeline is ongoing
            // is started by start button "B" after
            // moving the Bar to the spot the user would like to begin
            try {
                changePictureNumbers();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            bombCounter = game.getBombCounter();
            int counter = 0;
            for (Node n : scene.getChildren()) {
                if (n.getClass().getSimpleName().equals("Rectangle") && n != rectangle) {
                    counter++;
                }
            }
            if (counter == 0) {
                // checks if all blocks are gone
                timeline.stop();
                StaticClass.playSound("win.wav");
                try {
                    openEnd();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            game.moveBall();
            PowerUp p = null;
            try {
                p = game.moveDown();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            if (p != null) {
                scene.getChildren().remove(p.getImageView());
            }

            try {
                if (checkBall()) {
                    // checkBall returns boolean -> looks if ball hit the bottom border of the scene
                    getAgeInSeconds();
                    // score/ timer counts up
                } else {
                    timeline.stop();
                    // if lost, timeline is stopped
                    StaticClass.playSound("lose.wav");
                    openEnd();
                    // losing sound
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }));

    private void loadBlocks(double factor, AnchorPane pane) {
        Block block;
        for (int i = 0; i < game.getLevel().getBlocks().size(); i++) {
            block = game.getLevel().getBlocks().get(i);
            placeBlock(block.getX() * factor,
                    block.getY() * factor,
                    block.getStrength(), factor, pane);
        }
    }

    private void placeBlock(double x, double y, int strength, double factor, AnchorPane pane) {
        Rectangle rect = new Rectangle(x, y, 100 * factor, 30 * factor);

        if (strength == 1) {
            rect.setFill(Color.RED);
        } else if (strength == 2) {
            rect.setFill(Color.BLUE);
        } else {
            rect.setFill(Color.GREEN);
        }

        pane.getChildren().add(rect);
    }

    public void getAgeInSeconds() {
        // is needed for score, looks if start button is pressed "B" and then
        // it takes the current time in milliseconds
        // and every 10 milliseconds (timeline is responsible for that)
        // it looks at the current time and calculates how much time is between
        long nowMillis = System.currentTimeMillis();
        int zw = (int) ((nowMillis - this.createdMillis) / 1000);
        highScore.textProperty().bind(new SimpleIntegerProperty(zw).asString());
    }
}