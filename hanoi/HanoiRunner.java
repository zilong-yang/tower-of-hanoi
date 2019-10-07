package hanoi;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Created by Z on 7/27/2018
 *
 * InsertionSortRunner class of the Tower of Hanoi animation.
 */
public class HanoiRunner extends Application {

    @Override
    public void start(Stage primaryStage) {
        TowerOfHanoi pane = new TowerOfHanoi();

        TextField tfLevel = new TextField("" + pane.getLevel());
        tfLevel.setPrefColumnCount(2);

        Button btSolve = new Button("Solve");
        btSolve.setPrefWidth(75);
        Button btReset = new Button("Reset");
        btReset.setPrefWidth(75);
        Button btPlayPause = new Button("Pause");
        btPlayPause.setDisable(true);
        btPlayPause.setPrefWidth(75);

        btSolve.setOnAction(event -> {
            if (pane.isStopped())
                pane.solve();
            btSolve.setDisable(true);
            btPlayPause.setDisable(false);
        });

        btReset.setOnAction(event -> {
            pane.stop();
            pane.setLevel(Integer.parseInt(tfLevel.getText()));
            btSolve.setDisable(false);
            btPlayPause.setDisable(true);
            btPlayPause.setText("Pause");
        });

        btPlayPause.setOnAction(event -> {
            if (pane.isRunning() && btPlayPause.getText().equals("Pause")) {
                pane.pause();
                btPlayPause.setText("Play");
            } else if (pane.isPaused()) {
                pane.play();
                btPlayPause.setText("Pause");
            }
        });

        HBox buttons = new HBox(5,
                new Label("Level: "), tfLevel,
                btReset, btSolve, btPlayPause);
        buttons.setAlignment(Pos.CENTER);
        buttons.setPadding(new Insets(5));

        Slider slSpeed = new Slider(0, 5, 1);
        slSpeed.setMajorTickUnit(0.5);
        slSpeed.setMinWidth(200);
        slSpeed.setMaxWidth(400);
        slSpeed.valueProperty().addListener(ov -> pane.setRate(slSpeed.getValue()));

        Label lblSpeed = new Label("Speed", slSpeed);
        lblSpeed.setContentDisplay(ContentDisplay.RIGHT);
        lblSpeed.setGraphicTextGap(10);

        VBox controls = new VBox(5, lblSpeed, buttons);
        controls.setAlignment(Pos.CENTER);
        controls.setPadding(new Insets(5));

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(5));
        root.setCenter(pane);
        root.setBottom(controls);

        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Tower of Hanoi");
        primaryStage.show();
    }
}
