import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.io.File;

public class ControllerMenu {
    public Button BackButton, PlayButton, NextButton, RepeatButton, OpenButton;
    public Button BackVisualButton, NextVisualButton;
    public Text PlayedText, DurationText;
    public Label PlayingLabel;
    public Slider PlayedSlider, VolumeSlider;
    public Canvas canvas;
    public AnchorPane GUIAnchor;

    private Draw draw;

    private MediaPlayer player;
    private Media audio;
    private File file;
    private final FileChooser fileChooser = new FileChooser();

    public GraphicsContext gc;
    private boolean playing = true;

    public void initialize() {
        draw = new Draw();
        gc = canvas.getGraphicsContext2D();
        Globals.canvasSizeX = (int)canvas.getWidth();
        Globals.canvasSizeY = (int)canvas.getHeight();

        Globals.PlayedSlider = PlayedSlider;
        Globals.PlayedText = PlayedText;
        Globals.DurationText = DurationText;
        PlayingLabel.setTranslateY(100);//it goes 100 pixels down from it's initial position
    }
    public void BackVisualButtonAction(){
        if(Globals.Selected > 0) {
            Globals.Selected--;
        }
    }
    public void NextVisualButtonAction(){
        if(Globals.Selected < Globals.NumberOfOptions) {
            Globals.Selected++;
        }
    }

    public void GUIAnchorEnterAction() {
        GUIAnchor.setOpacity(1);
        PlayingLabel.setTranslateY(GUIAnchor.getTranslateY() - PlayedText.getScaleY());//positioned it so that the label sits on top of the GUI
    }
    public void GUIAnchorExitAction() {
        GUIAnchor.setOpacity(0);
        PlayingLabel.setTranslateY(100);//it goes 100 pixels down from it's initial position
    }
    public void VolumeSliderDragAction() {
        System.out.println("y");

        if(player != null) {
            player.setVolume(VolumeSlider.getValue());
        }
    }

    public void BackButtonAction() {
        //Currently unused
    }
    public void PlayButtonAction() {
        if(player != null) {//creating a toggle play button
            if (playing) {
                player.play();
                playing = false;
                PlayButton.setText("Stop");
            } else {
                player.pause();
                playing = true;
                PlayButton.setText("Play");
            }
        }
        if(player != null) {
            draw.Audio(gc, player);
            PlayingLabel.setText(file.getName());

            player.setOnEndOfMedia(new Runnable() {
                @Override
                public void run() {
                    player.stop();
                    playing = true;
                    PlayButton.setText("Play");
                }
            });
        }
    }
    public void NextButtonAction() {
        //Currently unused
    }
    public void RepeatButtonAction() {
        /*if(player != null) { not working properly yet. Reason: unknown.
            player.stop();
            player.play();
            playing = false;
            System.out.println(playing);
            PlayButton.setText("Stop");
        }*/
    }
    public void OpenButtonAction() {
        MediaPlayer oldPlayer = player;
        configureFileChooser(fileChooser);
        file = fileChooser.showOpenDialog(Globals.primaryStage);

        if (file.toURI() != null) {
            audio = new Media(file.toURI().toASCIIString());
            player = new MediaPlayer(audio);
            player.setVolume(0.5);//it's set to 1.0 by default which is the max
            VolumeSlider.setValue(player.getVolume());//the range for the slider was set from 0 to 1 just like the volume

            VolumeSlider.valueProperty().addListener(new ChangeListener<Number>() { //A slider listener for the Volume slider
                public void changed(ObservableValue<? extends Number> ov,
                                    Number old_val, Number new_val) {
                    player.setVolume(VolumeSlider.getValue());
                }
            });
        }
        if(player != oldPlayer && oldPlayer != null) {
            player.stop();
        }
    }


    private static void configureFileChooser(
            final FileChooser fileChooser) {
        fileChooser.setTitle("View songs");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("MP3", "*.mp3"),
                new FileChooser.ExtensionFilter("WAV", "*.wav") //add formats you'd like to play
        );
    }
}
