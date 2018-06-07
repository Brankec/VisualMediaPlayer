import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Slider;
import javafx.scene.media.AudioSpectrumListener;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.stage.Screen;

import java.text.DecimalFormat;

public class Draw {
    GraphicsContext gc;
    private Double PlayedTime = .0;
    private int bands = 0;
    private double previousBand, smooth;

    public double Audio(GraphicsContext gc, MediaPlayer player) { //this is where we get all the data and send it to other functions
        this.gc = gc;
        player.setAudioSpectrumNumBands(512);
        bands = player.getAudioSpectrumNumBands();

        player.setAudioSpectrumListener(new AudioSpectrumListener() {
            @Override
            public void spectrumDataUpdate(double timestamp, double duration, float[] magnitudes, float[] phases) {
                gc.clearRect(0, 0, 1920 / 2, 1080 / 2);
                PlayedTime += timestamp;

                int width = (int)Screen.getPrimary().getBounds().getWidth()/bands;
                float[] correctedMagnitude = new float[magnitudes.length];

                for (int i = 0; i < bands; i++) {
                    correctedMagnitude[i] = magnitudes[i] - player.getAudioSpectrumThreshold();

                    if(i > bands)
                        smooth = lerp(correctedMagnitude[i],correctedMagnitude[i+1], (float)0.000001);
                    else
                        smooth = correctedMagnitude[i];

                    list(Globals.Selected, i, (i * width+1), 1080 / 2, width, (int)(smooth * 10));
                }
            }
        });
        return PlayedTime;
        //return 0;
    }
    float lerp(float point1, float point2, float alpha)
    {
        return point1 + alpha * (point2 - point1);
    }
    private void list(int select, int i, int x, int y, int w, int h) {
        switch(select) {
            case 0: inHeight(i, x, y, w, h);
                break;
            case 1: inHeightTwice(i, x, y, w, h);
                break;
            case 2: inPosition(i, x, y, w, h);
                break;
            case 3: inRadius(i, x, y, w, h);
                break;
            case 4: inArc(i, x, y, w, h);
                break;
        }
    }














    public void inHeight(int i, int x, int y, int w, double h) {
        double amplitude;

        amplitude = y - h;

        DecimalFormat df = new DecimalFormat("#.0");

        double bandColor = Double.parseDouble(df.format(i * 0.00199));

        gc.setFill(Color.color(1, bandColor, 0));
        gc.fillRect(x, amplitude, w, h);
    }
    public void inHeightTwice(int i, int x, int y, int w, double h) {
        double amplitude;

        amplitude = (((y - 300) - (h/2)) * 0.1) * 10;

        DecimalFormat df = new DecimalFormat("#.0");

        double bandColor = Double.parseDouble(df.format(i * 0.00199));

        gc.setFill(Color.color(1, bandColor, 0));
        gc.fillRect(x, amplitude, w, h);
    }
    public void inPosition(int i, int x, int y, int w, int h) {
        double amplitude;
        int rectHeight = 80;
        amplitude = (y - rectHeight) - h;

        //double bandColor = i / bands;
        DecimalFormat df = new DecimalFormat("#.0");

        double bandColor = Double.parseDouble(df.format(i * 0.00199));

        gc.setFill(Color.color(1, bandColor, 0));

        gc.fillRect(x, amplitude + i*0.1, w, rectHeight);
    }
    public void inRadius(int i, int xPos, int yPos, int w, int h) {
        float r = bands-80;
        float x = (float) (r * Math.cos(i));
        float y = (float) ((r-50) * Math.sin(i)/2);

        double centerX = x + Globals.primaryStage.getWidth()/2;
        double centerY = y + Globals.primaryStage.getHeight()/3;

        DecimalFormat df = new DecimalFormat("#.0");
        double bandColor = Double.parseDouble(df.format(i * 0.00199));

        gc.setFill(Color.color(1, bandColor, 0));

        gc.fillRect(centerX, centerY, w, h);
    }
    public void inArc(int i, int x, int y, int w, int h) {
        double amplitude;

        amplitude = y - h;

        DecimalFormat df = new DecimalFormat("#.00");
        double bandColor = Double.parseDouble(df.format(i * 0.00199));

        gc.setFill(Color.color(1, bandColor, 0));
        //gc.fillArc(x, 300, w, 300, 45, h, ArcType.ROUND);
        gc.strokeArc(320, 10, 50, 50, 40, 80, ArcType.ROUND);
    }
}
