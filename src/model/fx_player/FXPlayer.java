package model.fx_player;

import java.io.*;
import java.net.URL;
import javax.sound.sampled.*;

/*
   Esta clase solo funciona para archivos .wav
*/
public enum FXPlayer {
   STAGE_THEME("stage_theme.wav"),
   STAGE_START("stage_start.wav"),
   TITLE_SCREEN("title_screen.wav");

   public static enum Volume {
      MUTE, LOW, MEDIUM, HIGH
   }

   public static Volume volume = Volume.LOW;

   private Clip clip;

   private FXPlayer(String fileName) {
      try {
         URL url = this.getClass().getClassLoader().getResource("sonidos/" + fileName);

         AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);

         clip = AudioSystem.getClip();

         clip.open(audioInputStream);
      } catch (UnsupportedAudioFileException e) {
         e.printStackTrace();
      } catch (IOException e) {
         e.printStackTrace();
      } catch (LineUnavailableException e) {
         e.printStackTrace();
      }
   }

   public void play() {
      if (volume != Volume.MUTE) {
         if (!clip.isRunning()){
            clip.setFramePosition(0);
            clip.start();
         }
      }
   }

   public void stop() {
      if(clip.isRunning()) {
         clip.stop();
         clip.close();
      }
   }
}