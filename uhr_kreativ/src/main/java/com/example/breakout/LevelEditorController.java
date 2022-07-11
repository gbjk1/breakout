package com.example.breakout;

import com.example.breakout.Classes.Block;
import com.example.breakout.Classes.Level;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

public class LevelEditorController {
    //  static variables to maintain more flexibility
    private static final int rectWidth = 100;
    private static final int rectHeight = 30;
    private static final int mainPaneWidth = 1000;
    private static final int blockX = 1090;

    //  rectangles in the right AnchorPane -> needed for drag and drop-actions
    @FXML
    private Rectangle redRect;
    @FXML
    private Rectangle blueRect;
    @FXML
    private Rectangle greenRect;

    /*
     *  mainPane: outer AnchorPane; whole scene (1280 x 720)
     *  placePane: left AnchorPane; allowed area for drag and drop (1000 x 720)
     */
    @FXML
    private AnchorPane mainPane;
    @FXML
    private AnchorPane placePane;

    //  Buttons and TextField in left AnchorPane (needed for file actions)
    @FXML
    private Button resetScreenBtn;
    @FXML
    private Button saveLevelBtn;
    @FXML
    private Button exitLevelEditorBtn;
    @FXML
    private TextField name;

    //  needed variable for saving created Level
    protected static Level level = new Level();

    //  needed variables for selecting multiple blocks
    private int blockSelected = 0;
    private final Point p = new Point();
    private final Region selectRect = new Region();
    private double x = 0, y = 0;

    //  method initialize gets executed while opening Level-Editor
    @FXML
    private void initialize() {
        //  color Rectangles in left AnchorPane
        redRect.setFill(Color.RED);
        blueRect.setFill(Color.BLUE);
        greenRect.setFill(Color.GREEN);

        //  sett onMouseEntered-Event to create new block for drag and drop actions
        redRect.setOnMouseEntered(e -> placeBlock(new Block(level.getCount(), blockX, 50, rectWidth, rectHeight, 1)));
        blueRect.setOnMouseEntered(e -> placeBlock(new Block(level.getCount(), blockX, 130, rectWidth, rectHeight, 2)));
        greenRect.setOnMouseEntered(e -> placeBlock(new Block(level.getCount(), blockX, 210, rectWidth, rectHeight, 3)));

        //  load blocks from file if level from file will be edited
        loadBlocks();
        name.setText(level.getName());

        //  set actions for reset-Button
        resetScreenBtn.setFocusTraversable(false);
        resetScreenBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, (e -> {
            Alert resetAlert = new Alert(Alert.AlertType.CONFIRMATION);
            resetAlert.setTitle("Löschen");
            resetAlert.setHeaderText("Alle platzierten Blöcke werden gelöscht.");
            resetAlert.setContentText("Möchten Sie fortfahren?");

            Optional<ButtonType> result = resetAlert.showAndWait();
            if (result.isPresent()) {
                if (result.get() == ButtonType.OK) {
                    for (Rectangle r : getRectangles()) {
                        level.removeBlock(level.findBlock(r.getX(), r.getY()));
                        mainPane.getChildren().remove(r);
                    }

                    reloadScreen();
                }
            }
        }));

        //  set actions for save-level-Button
        saveLevelBtn.setDisable(true);
        saveLevelBtn.setFocusTraversable(false);
        saveLevelBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, (e -> {
            reloadScreen();
            level.setName(name.getText());
            System.out.println();

            Alert saveAlert = new Alert(Alert.AlertType.INFORMATION);
            saveAlert.setTitle("Speichern");
            saveAlert.setHeaderText("Level gespeichert.");
            saveAlert.setContentText("Level wurde unter dem Namen \"" + name.getText() + "\" abgespeichert.");
            if (level.saveLevel(name.getText())) {
                saveAlert.show();
            } else {
                Alert overwriteAlert = new Alert(Alert.AlertType.CONFIRMATION);
                overwriteAlert.setTitle("Speichern");
                overwriteAlert.setHeaderText("Level mit dem Namen \"" + name.getText() + "\" existiert bereits.");
                overwriteAlert.setContentText("Möchten Sie trotzdem fortfahren?");

                Optional<ButtonType> result = overwriteAlert.showAndWait();
                if (result.isPresent()) {
                    if (result.get() == ButtonType.OK) {
                        level.overwriteLevel(name.getText());
                        saveAlert.show();
                    }
                }
            }
        }));

        //  set actions for exit-level-Button
        exitLevelEditorBtn.setFocusTraversable(false);
        exitLevelEditorBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, (e -> {
            Alert resetAlert = new Alert(Alert.AlertType.CONFIRMATION);
            resetAlert.setTitle("Beenden");
            resetAlert.setHeaderText("Alle platzierten Blöcke werden gelöscht.");
            resetAlert.setContentText("Möchten Sie trotzdem fortfahren?");

            Optional<ButtonType> result = resetAlert.showAndWait();
            if (result.isPresent()) {
                if (result.get() == ButtonType.OK) {
                    try {
                        level = new Level();
                        ControllerScreens.switchToMain();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }

        }));

        //  add actions for name-Text-Field (gets executed after key released)
        name.setFocusTraversable(false);
        name.setPromptText("Level-Name");
        name.addEventHandler(KeyEvent.KEY_RELEASED, (e -> {
            if (getRectangles().size() == 0) {
                saveLevelBtn.setDisable(true);
            } else {
                saveLevelBtn.setDisable(name.getText().equals(""));
            }
        }));

        //  set border for multiple-selection
        selectRect.setStyle("-fx-border-width: 2px; -fx-border-color: black; -fx-border-style: dashed;");

        //  add event to left AnchorPane to reset multiple selection and move selected blocks
        placePane.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            blockSelected = 0;
            reloadScreen();

            selectRect.setMinHeight(0);
            selectRect.setMinHeight(0);
            selectRect.setMinWidth(0);
            selectRect.setMaxWidth(0);
            selectRect.setLayoutX(0);
            selectRect.setLayoutY(0);

            p.setLocation(e.getSceneX(), e.getSceneY());

            placePane.getChildren().add(selectRect);
        });
        placePane.addEventHandler(MouseEvent.MOUSE_DRAGGED, e -> {
            if (p.getX() > e.getSceneX() && e.getSceneX() <= 0) {
                selectRect.setMinWidth(p.getX());
                selectRect.setMaxWidth(p.getX());
                selectRect.setLayoutX(2);
            } else if (p.getX() > e.getSceneX() && e.getSceneX() > 0 && e.getSceneX() < mainPaneWidth) {
                selectRect.setMinWidth(p.getX() - e.getSceneX());
                selectRect.setMaxWidth(p.getX() - e.getSceneX());
                selectRect.setLayoutX(e.getSceneX());
            } else if (e.getSceneX() >= mainPaneWidth) {
                selectRect.setMinWidth(mainPaneWidth - 6 - p.getX());
                selectRect.setMaxWidth(mainPaneWidth - 6 - p.getX());
                selectRect.setLayoutX(p.getX());
            } else {
                selectRect.setMinWidth(e.getSceneX() - p.getX());
                selectRect.setMaxWidth(e.getSceneX() - p.getX());
                selectRect.setLayoutX(p.getX());
            }

            if (p.getY() > e.getSceneY() && e.getSceneY() <= 0) {
                selectRect.setMinHeight(p.getY());
                selectRect.setMaxHeight(p.getY());
                selectRect.setLayoutY(0);
            } else if (p.getY() > e.getSceneY() && e.getSceneX() > 0) {
                selectRect.setMinHeight(p.getY() - e.getSceneY());
                selectRect.setMaxHeight(p.getY() - e.getSceneY());
                selectRect.setLayoutY(e.getSceneY());
            } else if (e.getSceneY() >= ControllerScreens.windowHeight - 20) {
                selectRect.setMinHeight(ControllerScreens.windowHeight - 20 - p.getY());
                selectRect.setMaxHeight(ControllerScreens.windowHeight - 20 - p.getY());
                selectRect.setLayoutY(p.getY());
            } else {
                selectRect.setMinHeight(e.getSceneY() - p.getY());
                selectRect.setMaxHeight(e.getSceneY() - p.getY());
                selectRect.setLayoutY(p.getY());
            }
        });
        placePane.addEventHandler(MouseEvent.MOUSE_RELEASED, e -> {
            Block b;
            for (Rectangle r : getRectangles()) {
                if (((r.getX() + rectWidth > selectRect.getLayoutX()) && (r.getX()
                        < selectRect.getLayoutX() + selectRect.getWidth()))
                        && ((r.getY() + rectHeight > selectRect.getLayoutY()) &&
                        (r.getY() < selectRect.getLayoutY() +
                                selectRect.getHeight()))) {
                    b = level.findBlock(r.getX(), r.getY());

                    if (b.getStrength() == 1) {
                        r.setFill(Color.DARKRED);
                    } else if (b.getStrength() == 2) {
                        r.setFill(Color.DARKBLUE);
                    } else {
                        r.setFill(Color.DARKGREEN);
                    }

                    blockSelected++;
                }
            }

            selectRect.setMinHeight(0);
            selectRect.setMinHeight(0);
            selectRect.setMinWidth(0);
            selectRect.setMaxWidth(0);

            placePane.getChildren().remove(selectRect);
        });

        mainPane.requestFocus();
    }

    //  function to load blocks from Level and place on main AnchorPane as Rectangle
    private void loadBlocks() {
        try {
            Block b;
            for (int i = 0; i < level.getBlocks().size(); i++) {
                b = level.getBlocks().get(i);
                placeBlock(b);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //  function to reload main AnchorPane
    private void reloadScreen() {
        Block b;
        for (Rectangle r : getRectangles()) {
            b = level.findBlock(r.getX(), r.getY());
            if (r.getX() <= (mainPaneWidth - rectWidth)) {
                if (b.getStrength() == 1) {
                    r.setFill(Color.RED);
                } else if (b.getStrength() == 2) {
                    r.setFill(Color.BLUE);
                } else {
                    r.setFill(Color.GREEN);
                }
            } else {
                mainPane.getChildren().remove(r);
                level.removeBlock(b);
            }
        }

        if (getRectangles().size() == 0) {
            saveLevelBtn.setDisable(true);
        } else {
            saveLevelBtn.setDisable(name.getText() != null && name.getText().equals(""));
        }
    }

    //  function to get list of placed Rectangles
    private ArrayList<Rectangle> getRectangles() {
        ArrayList<Rectangle> rectList = new ArrayList<>();
        for (int i = 0; i < mainPane.getChildren().size(); i++) {
            Node n = mainPane.getChildren().get(i);
            if (n.getClass().getSimpleName().equals("Rectangle")) {
                rectList.add((Rectangle) n);
            }
        }
        return rectList;
    }

    //  function to place new Rectangle (gets executed after MouseEntered on Rectangles in right AnchorPane)
    private void placeBlock(Block block) {
        Rectangle rect;

        //  set color for new rectangle depending on "Mother"-Rectangle (detected by Y-coordinate)
        if (block.getStrength() == 1) {
            rect = new Rectangle(block.getX(), block.getY(), rectWidth, rectHeight);
            if (block.getX() == blockX) {
                rect.setFill(Color.DARKRED);
            } else {
                rect.setFill(Color.RED);
            }
        } else if (block.getStrength() == 2) {
            rect = new Rectangle(block.getX(), block.getY(), rectWidth, rectHeight);
            if (block.getX() == blockX) {
                rect.setFill(Color.DARKBLUE);
            } else {
                rect.setFill(Color.BLUE);
            }
        } else {
            rect = new Rectangle(block.getX(), block.getY(), rectWidth, rectHeight);

            if (block.getX() == blockX) {
                rect.setFill(Color.DARKGREEN);
            } else {
                rect.setFill(Color.GREEN);
            }
        }

        //  removing earlier created Rectangles in right AnchorPane
        for (Rectangle r : getRectangles()) {
            if (r.getX() == blockX && r.getY() != rect.getY()) {
                mainPane.getChildren().remove(r);
            }
        }

        //  removing Rectangle if position is in right AnchorPane
        EventHandler<MouseEvent> handler = e -> {
            if (rect.getX() + (double) rectWidth / 2 > mainPaneWidth - rectWidth) {
                mainPane.getChildren().remove(rect);
                level.removeBlock(block);
            }
            mainPane.setCursor(Cursor.DEFAULT);
        };

        //  add handler to on mouse exited target event listener
        rect.addEventHandler(MouseEvent.MOUSE_EXITED_TARGET, handler);

        //  add handler for on mouse pressed event (for coloring block)
        rect.addEventHandler(MouseEvent.MOUSE_PRESSED, (e -> {
            if (blockSelected > 1) {
                for (Rectangle r : getRectangles()) {
                    if (!(r.getX() < mainPaneWidth - (double) rectWidth / 2)) {
                        blockSelected--;
                        if (r.getFill() == Color.DARKGREEN) {
                            r.setFill(Color.GREEN);
                        } else if (r.getFill() == Color.DARKRED) {
                            r.setFill(Color.RED);
                        } else if (r.getFill() == Color.DARKBLUE) {
                            r.setFill(Color.BLUE);
                        }
                    }
                }
                x = e.getSceneX();
                y = e.getSceneY();
            } else if (blockSelected == 1) {
                reloadScreen();
                blockSelected = 1;
                rect.removeEventHandler(MouseEvent.MOUSE_EXITED_TARGET,
                        handler);
                rect.addEventHandler(MouseEvent.MOUSE_EXITED_TARGET, ev ->
                        mainPane.setCursor(Cursor.DEFAULT));
            } else {
                if (blockSelected != 0) {
                    reloadScreen();
                }
                blockSelected = 1;
                rect.removeEventHandler(MouseEvent.MOUSE_EXITED_TARGET,
                        handler);
                rect.addEventHandler(MouseEvent.MOUSE_EXITED_TARGET, ev ->
                        mainPane.setCursor(Cursor.DEFAULT));
            }

            mainPane.setCursor(Cursor.MOVE);
        }));

        //  add event handler needed for drag-action (replacing Rectangle on drag)
        rect.addEventHandler(MouseEvent.MOUSE_DRAGGED, (e -> {
            //  if one block is selected
            //  -> color gets dark if position is not a correct position for
            //      placing a block
            if (blockSelected == 1) {
                rect.setX(e.getSceneX() - (double) rectWidth / 2);
                rect.setY(e.getSceneY() - (double) rectHeight / 2);

                block.setX(e.getSceneX() - (double) rectWidth / 2);
                block.setY(e.getSceneY() - (double) rectHeight / 2);

                if (block.getStrength() == 1) {
                    rect.setFill(Color.DARKRED);
                } else if (block.getStrength() == 2) {
                    rect.setFill(Color.DARKBLUE);
                } else {
                    rect.setFill(Color.DARKGREEN);
                }
            } else if (blockSelected > 1) {
                Block b;
                for (Rectangle r : getRectangles()) {
                    if (r.getFill() == Color.DARKGREEN || r.getFill() == Color.DARKRED
                            || r.getFill() == Color.DARKBLUE) {
                        b = level.findBlock(r.getX(), r.getY());

                        b.setX(r.getX() - (x - e.getSceneX()));
                        b.setY(r.getY() - (y - e.getSceneY()));

                        r.setX(r.getX() - (x - e.getSceneX()));
                        r.setY(r.getY() - (y - e.getSceneY()));
                    }
                }
                x = e.getSceneX();
                y = e.getSceneY();
            }
        }));

        //  add event handler to check if position of blocks is correct on
        //      drop action
        //  -> recolor if position is correct, remove block if not
        rect.addEventHandler(MouseEvent.MOUSE_RELEASED, e -> {
            if (blockSelected == 1) {
                if ((e.getSceneX() + (double) rectWidth / 2 > mainPaneWidth || e.getSceneX() - (double) rectWidth / 2 < 0) ||
                        (e.getSceneY() - (double) rectHeight / 2 < 0 || e.getSceneY() + (double) rectHeight / 2 > ControllerScreens.windowHeight) ||
                        !level.replaceBlock(block)) {
                    mainPane.getChildren().remove(rect);
                    level.removeBlock(block);
                }
                reloadScreen();
                blockSelected = 0;
            } else if (blockSelected > 1) {
                Block b;
                for (Rectangle r : getRectangles()) {
                    if (r.getFill() == Color.DARKGREEN || r.getFill() ==
                            Color.DARKRED || r.getFill() == Color.DARKBLUE) {
                        b = level.findBlock(r.getX(), r.getY());
                        if (((r.getX() >= mainPaneWidth - rectWidth || r.getX() <= 0) || (r.getY()
                                <= 0 || r.getY() >= ControllerScreens.windowHeight - 20 - rectHeight)) || !level.replaceBlock(b)) {
                            mainPane.getChildren().remove(r);
                            level.removeBlock(b);
                            blockSelected--;
                        }
                    }
                }
            }

            mainPane.setCursor(Cursor.MOVE);
        });

        rect.addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET, (e ->
                mainPane.setCursor(Cursor.MOVE)));

        if (!level.getBlocks().contains(block)) {
            level.addBlock(block);
        }
        mainPane.getChildren().add(rect);
    }
}
