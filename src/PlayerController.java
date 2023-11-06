import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.event.ActionEvent;
import javafx.concurrent.Task;
import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

import javafx.collections.ObservableList;


public class PlayerController {
    @FXML
    private Button playButton;
    @FXML
    private Button stopButton;
    @FXML
    private Slider volumeSlider;
    @FXML
    private ListView<String> playlist;
    private ObservableList<String> playlistItems;
    private SourceDataLine sourceDataLine;
    private boolean isPlaying = false;
    private Thread audioThread;

    @FXML
    public void playAction(ActionEvent event) {
        if (!isPlaying) {
            String selectedSong = playlist.getSelectionModel().getSelectedItem();
            if (selectedSong != null) {
                Task<Void> audioTask = new Task<Void>() {
                    @Override
                    protected Void call() {
                        try {
                            isPlaying = true;

                            AudioFormat audioFormat = new AudioFormat(44100, 16, 2, true, false);
                            sourceDataLine = AudioSystem.getSourceDataLine(audioFormat);
                            sourceDataLine.open(audioFormat);
                            sourceDataLine.start();

                            URL audioURL = getClass().getResource("/songs/" + selectedSong + ".wav");
                            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioURL);
                            byte[] audioData = new byte[1024];
                            int bytesRead;

                            while ((bytesRead = audioInputStream.read(audioData, 0, audioData.length)) != -1 && isPlaying) {
                                sourceDataLine.write(audioData, 0, bytesRead);
                            }

                            sourceDataLine.drain();
                            sourceDataLine.close();
                        } catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                };

                audioThread = new Thread(audioTask);
                audioThread.start();
            }
        }
    }

    @FXML
    public void stopAction(ActionEvent event) {
        isPlaying = false; // Set the flag to stop playback
        if (sourceDataLine != null) {
            sourceDataLine.stop(); // Stop the audio playback
            sourceDataLine.close(); // Close the data line
        }
    }

    @FXML
    public void initialize() {
        playlistItems = playlist.getItems();

        // Add your preset songs to the playlistItems
        playlistItems.add("30, 'ore");
        playlistItems.add("Saturday - Saturday");
        playlistItems.add("7_22PM");
        // Add more preset songs here
    }





}





// Did not have enough time to improve song selection.