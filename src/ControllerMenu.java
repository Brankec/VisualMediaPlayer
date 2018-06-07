import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.io.File;

public class ControllerMenu {
    public Button BackButton, PlayButton, NextButton, RepeatButton, OpenButton;
    public Button BackVisualButton, NextVisualButton;
    public Text PlayedText, DurationText;
    public Slider PlayedSlider, VolumeSlider;
    public Canvas canvas;

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
        Globals.windowSizeX = (int)canvas.getWidth();
        Globals.windowSizeY = (int)canvas.getHeight();
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

    public void BackButtonAction() {
        //Currently unused
    }
    public void PlayButtonAction() {
        if(player != null) {
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
            //PlayedSlider.setValue(draw.Audio(gc, player));
            draw.Audio(gc, player);
            System.out.println("l");
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
        }
        if(player != oldPlayer && oldPlayer != null) {
            player.stop();
        }

        player = new MediaPlayer(audio);
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
