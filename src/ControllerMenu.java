import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.List;

public class ControllerMenu {
    public Button BackButton, PlayButton, NextButton, RepeatButton, OpenButton;
    public Button BackVisualButton, NextVisualButton, ListViewButton;
    public Text PlayedText, DurationText;
    public Label PlayingLabel;
    public Slider PlayedSlider, VolumeSlider;
    public ListView <String> MusicListView;
    public Canvas canvas;
    public AnchorPane GUIAnchor, Menu;

    private Draw draw;

    private MediaPlayer player;
    private Media audio;
    private List<File> fileList;
    private final FileChooser fileChooser = new FileChooser();

    private int currentSong = 0;
    public GraphicsContext gc;
    private boolean isPlaying = false;
    private boolean islistOpen = false;

    public void initialize() {
        draw = new Draw();
        gc = canvas.getGraphicsContext2D();
        Globals.canvasSizeX = (int)canvas.getWidth();
        Globals.canvasSizeY = (int)canvas.getHeight();

        Globals.PlayedSlider = PlayedSlider;
        Globals.PlayedText = PlayedText;
        Globals.DurationText = DurationText;
    }
    public void BackVisualButtonAction(){
        if(Globals.Selected > 0) {
            Globals.Selected--;
        }
    }
    public void NextVisualButtonAction(){
        if(Globals.Selected < Globals.VisualList - 1) {//-1 because the index starts from 0, duh.
            Globals.Selected++;
        }
    }

    public void GUIAnchorEnterAction() {
        if (!islistOpen) {
            GUIAnchor.setOpacity(1);
            PlayingLabel.setTranslateY(GUIAnchor.getTranslateY() - PlayedText.getScaleY());//positioned it so that the label sits on top of the GUI
        }
    }
    public void GUIAnchorExitAction() {
        if (!islistOpen) {
            GUIAnchor.setOpacity(0);
            PlayingLabel.setTranslateY(100);//it goes 100 pixels down from it's initial position
        }
    }

    public void BackButtonAction() {
        if(player != null && currentSong > 0) {
            player.stop();
            currentSong--;
            audio = new Media(fileList.get(currentSong).toURI().toASCIIString());
            player = new MediaPlayer(audio);

            player.play();
            isPlaying = true;
            PlayingLabel.setText(fileList.get(currentSong).getName());
            PlayButton.setText("Stop");

            playVisual();
        }
    }
    public void PlayButtonAction() {
        if(player != null) {//a toggle play button
            if (!isPlaying) {
                player.play();
                isPlaying = true;
                PlayButton.setText("Stop");
            } else {
                player.pause();
                isPlaying = false;
                PlayButton.setText("Play");
            }
        }
        playVisual();
    }
    public void NextButtonAction() {
        if(player != null && currentSong < (fileList.size() - 1)) {
            player.stop();
            currentSong++;
            audio = new Media(fileList.get(currentSong).toURI().toASCIIString());
            player = new MediaPlayer(audio);

            player.play();
            isPlaying = true;
            PlayingLabel.setText(fileList.get(currentSong).getName());
            PlayButton.setText("Stop");

            playVisual();
        }
    }
    public void RepeatButtonAction() {
        if(player != null) {
            player.stop();
            player.play();
            isPlaying = true;
            PlayButton.setText("Stop");
        }
    }
    public void OpenButtonAction() {
        List<File> fileListOld = fileList;
        configureFileChooser(fileChooser);
        fileList = fileChooser.showOpenMultipleDialog(Globals.primaryStage); //Opening file explorer and saving all selected the files in here

        if(fileList != null) {
            if (isPlaying) { //this stops a song that is already playing if opening a new set of songs
                player.stop();
                isPlaying = false;
                PlayButton.setText("Play");

                MusicListView.getItems().clear();
                ListViewButtonAction();
                MusicListView.getSelectionModel().selectFirst();
            }
            for(int i = 0; i < fileList.size(); i++) {
                MusicListView.getItems().add(fileList.get(i).getName());
            }

            Globals.MusicList = fileList.size();
            currentSong = 0;
            if (fileList.get(currentSong).toURI() != null) {
                audio = new Media(fileList.get(currentSong).toURI().toASCIIString());


                player = new MediaPlayer(audio);
                PlayingLabel.setText(fileList.get(currentSong).getName());

                player.setVolume(0.5);//it's set to 1.0 by default which is the max
                VolumeSlider.setValue(player.getVolume());//the range for the slider was set from 0 to 1 just like the volume

                VolumeSlider.valueProperty().addListener(new ChangeListener<Number>() { //A slider listener for the Volume slider
                    public void changed(ObservableValue<? extends Number> ov,
                                        Number old_val, Number new_val) {
                        player.setVolume(VolumeSlider.getValue());
                    }
                });
            }
        } else {
            fileList = fileListOld;
        }
    }
    public void ListViewButtonAction() {
        if(fileList != null) {//a toggle button
            if (!islistOpen) {
                GUIAnchor.setTranslateY(-(GUIAnchor.getHeight() - 100)); // the list view is under the window so I just decrease its Y value
                PlayingLabel.setTranslateY(-200);//hacked values that I can't be bothered with to calculate properly

                islistOpen = true;
            } else {
                GUIAnchor.setTranslateY(-(GUIAnchor.getHeight() - 300)); //returns it in its original place
                PlayingLabel.setTranslateY(-(GUIAnchor.getHeight() - 300) - PlayedText.getScaleY());
                islistOpen = false;
            }
        }
    }
    public void MusicListViewClickAction() {
        if (fileList != null) {
            player.stop();
            currentSong = MusicListView.getSelectionModel().getSelectedIndex(); //gets the index from the cell you clicked on

            audio = new Media(fileList.get(currentSong).toURI().toASCIIString());
            if (isPlaying) { //this stops a song that is already playing if opening a new set of songs
                player.stop();
                isPlaying = false;
                PlayButton.setText("Play");
            }
            player = new MediaPlayer(audio);
            PlayingLabel.setText(fileList.get(currentSong).getName());


            player.play();
            playVisual();
            isPlaying = true;
            PlayButton.setText("Stop");
        }
    }

    private void playVisual() {//currently the slider controls are in Draw class
        if(player != null) {
            draw.Audio(gc, player);
            PlayingLabel.setText(fileList.get(currentSong).getName());

            player.setOnEndOfMedia(new Runnable() {
                @Override
                public void run() {
                    isPlaying = false;
                    PlayButton.setText("Play");
                    NextButtonAction();
                }
            });
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
