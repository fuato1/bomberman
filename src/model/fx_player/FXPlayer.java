package model.fx_player;

import java.io.*;
import java.net.URL;
import javax.sound.sampled.*;

public enum FXPlayer {
   BARRIL("barril3duendes.wav"),
   BARBAROS("barbaros.wav"),
   MONTAPUERCOS("montapuercos.wav");

   public static enum Volume {
      MUTE, LOW, MEDIUM, HIGH
   }

   public static Volume volume = Volume.LOW;

   private Clip clip;

   FXPlayer(String wav) {
      try {

         URL url = this.getClass().getClassLoader().getResource("sonidos/" + wav);

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

   static void init() {
      values();
   }
}