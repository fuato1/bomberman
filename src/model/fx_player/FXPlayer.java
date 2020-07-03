package model.fx_player;

import java.io.*;
import java.net.URL;
import javax.sound.sampled.*;

public enum FXPlayer {
   STAGE_THEME("stage_theme.wav"), STAGE_START("stage_start.wav"), TITLE_SCREEN("title_screen.wav"),
   BOMB_SOUND("bomb_sound.wav"), BONUS_SOUND("bonus_sound.wav"), GAME_OVER("game_over.wav");

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
         if (!clip.isRunning()) {
            clip.setFramePosition(0);
            clip.start();
         }
      }
   }

   public void stop() {
      if (clip.isRunning()) {
         clip.stop();
      }
   }
}