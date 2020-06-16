package mediatest;

import javafx.fxml.FXML;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class Controller {
    @FXML
    private MediaView mediaView;

    public void initialize() throws IOException {
        //Fjern &hd=1 i url'en for at downloade den ikke hd version.
        URL url = new URL("https://media.exorlive.com/?id=7302&filetype=mp4&env=production&hd=1");
        File file = new File("test.mp4");
        long start = System.currentTimeMillis();

        // Vælg kun en.
//        downloadWithJavaIO(url.toString(), file.getName());
//        downloadWithJava7IO(url.toString(), file.getName());
        downloadWithJavaNIO(url.toString(), file.getName());

        Media media = new Media(file.toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaView.setMediaPlayer(mediaPlayer);
        mediaPlayer.play();
        long stop = System.currentTimeMillis();

        System.out.println((stop - start) / 1000);
    }


    // Tre formater fået fra https://github.com/eugenp/tutorials/blob/master/core-java-modules/core-java-networking-2/src/main/java/com/baeldung/download/FileDownload.java
    // Kommer fra denne artikel: https://www.baeldung.com/java-download-file
    public static void downloadWithJavaIO(String url, String localFilename) {

        try (BufferedInputStream in = new BufferedInputStream(new URL(url).openStream()); FileOutputStream fileOutputStream = new FileOutputStream(localFilename)) {

            byte dataBuffer[] = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void downloadWithJava7IO(String url, String localFilename) {

        try (InputStream in = new URL(url).openStream()) {
            Files.copy(in, Paths.get(localFilename), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void downloadWithJavaNIO(String fileURL, String localFilename) throws IOException {

        URL url = new URL(fileURL);
        try (ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(localFilename); FileChannel fileChannel = fileOutputStream.getChannel()) {

            fileChannel.transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
        }
    }
}
