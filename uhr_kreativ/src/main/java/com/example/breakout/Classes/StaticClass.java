package com.example.breakout.Classes;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class StaticClass {
    public static File settings = new File(String.valueOf(Paths.get(System.getProperty("user.dir"), "src", "resources", "settings.txt")));
    public static String theme = "basic";

    private static MediaPlayer mediaPlayer = null;

    private static boolean soundSetting = true;
    private static boolean musicSetting = true;
    private static double mediaPlayerVolume = 0.15;
    private static String currentSong = "no song";


    public static void playSong(String songName) {
        //these lines need to be here and not in the if due to settings being able to turn on or off music with a button there
        if (!currentSong.equals(songName)) {
            currentSong = songName;
            Media hit = new Media(new File(String.valueOf(Paths.get(System.getProperty("user.dir"), "src", "resources", theme, songName
            ))).toURI().toString());
            mediaPlayer = new MediaPlayer(hit);
            mediaPlayer.setVolume(mediaPlayerVolume);
            mediaPlayer.setOnEndOfMedia(new Runnable() {
                @Override
                public void run() {
                    mediaPlayer.seek(Duration.ZERO);
                    mediaPlayer.play();
                }
            });
            if (musicSetting) {
                mediaPlayer.play();
            }
        }
    }

    public static void playSound(String soundName) {
        if (soundSetting) {
            File path = new File(String.valueOf(Paths.get(System.getProperty("user.dir"), "src", "resources", theme, soundName)));
            System.out.println(path);
            try {
                AudioInputStream audioInput2 = AudioSystem.getAudioInputStream(path);
                Clip clip = AudioSystem.getClip();
                clip.open(audioInput2);
                clip.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void readLocalSettings() throws IOException {
        if (settings.exists()) {
            Scanner myReader = new Scanner(settings);
            String data = myReader.nextLine();
            musicSetting = Boolean.parseBoolean(data.split("=")[1]);
            data = myReader.nextLine();
            soundSetting = Boolean.parseBoolean(data.split("=")[1]);
            myReader.close();
        } else {
            settings.createNewFile();
            FileWriter myWriter = new FileWriter(settings);
            myWriter.write("Music=true\n");
            myWriter.write("Sound=true");
            myWriter.close();
        }
    }

    public static boolean isSoundSetting() {
        return soundSetting;
    }

    public static boolean isMusicSetting() {
        return musicSetting;
    }

    public static void setSoundSetting(boolean soundSetting) throws IOException {
        StaticClass.soundSetting = soundSetting;
        changeOneLineInSettings(1, "Sound=" + soundSetting);
    }

    public static void setMusicSetting(boolean musicSetting) throws IOException {
        if (musicSetting) {
            mediaPlayer.seek(Duration.ZERO);
            mediaPlayer.play();
        } else {
            mediaPlayer.stop();
        }
        StaticClass.musicSetting = musicSetting;
        changeOneLineInSettings(0, "Music=" + musicSetting);
    }

    public static void changeOneLineInSettings(int linenumber, String line) throws IOException {
        List<String> fileContent = new ArrayList<>(Files.readAllLines(settings.toPath()));

        for (int i = 0; i < fileContent.size(); i++) {
            if (i == linenumber) {
                fileContent.set(i, line);
                break;
            }
        }

        Files.write(settings.toPath(), fileContent);
    }
}
