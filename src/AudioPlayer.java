import javax.sound.sampled.*;

public class AudioPlayer {
    public static void main(String[] args) {
        try {
            AudioFormat audioFormat = new AudioFormat(44100, 16, 2, true, false);
            SourceDataLine sourceDataLine = AudioSystem.getSourceDataLine(audioFormat);
            sourceDataLine.open(audioFormat);
            sourceDataLine.start();

            // Read audio data from a file (replace "sample.wav" with your audio file)
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(AudioPlayer.class.getResourceAsStream("30, 'ore.wav"));
            byte[] audioData = new byte[1024];
            int bytesRead;

            while ((bytesRead = audioInputStream.read(audioData, 0, audioData.length)) != -1) {
                sourceDataLine.write(audioData, 0, bytesRead);
            }

            sourceDataLine.drain();
            sourceDataLine.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}