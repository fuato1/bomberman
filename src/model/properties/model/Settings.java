package model.properties.model;

import java.util.HashMap;

public class Settings {
    private boolean fullScreen;
    private boolean sound;
    private HashMap<String, String> defaultKeys, customKeys;
    private String soundTrack;

    private static Settings settings;

    private Settings(boolean fullScreen, boolean sound, HashMap<String, String> defaultKeys, HashMap<String, String> customKeys, String soundTrack) {
        this.fullScreen = fullScreen;
        this.sound = sound;
        this.defaultKeys = defaultKeys;
        this.customKeys = customKeys;
        this.soundTrack = soundTrack;
    }

    public static Settings getSettings() {
        if(settings == null) {
            HashMap<String, String> defaultKeys = new HashMap<String, String>(7);
            HashMap<String, String> customKeys = new HashMap<String, String>(7);

            defaultKeys.put("Sonido", "q");
            defaultKeys.put("Música", "w");
            defaultKeys.put("Pausa", "p");
            defaultKeys.put("Ayuda", "h");
            defaultKeys.put("Izquierda", "left arrow");
            defaultKeys.put("Derecha", "right arrow");
            defaultKeys.put("Acción", "space");

            settings = new Settings(false, true, defaultKeys, customKeys, "original");
        }

        return settings;
    }

    public boolean isFullScreen() {
        return fullScreen;
    }

    public void setFullScreen(boolean fullScreen) {
        this.fullScreen = fullScreen;
    }

    public boolean isSound() {
        return sound;
    }

    public void setSound(boolean sound) {
        this.sound = sound;
    }

    public String getSoundTrack() {
        return soundTrack;
    }

    public void setSoundTrack(String soundTrack) {
        this.soundTrack = soundTrack;
    }

    public static void setSettings(Settings settings) {
        Settings.settings = settings;
    }

    public HashMap<String, String> getDefaultKeys() {
        return defaultKeys;
    }

    public void setDefaultKeys(HashMap<String, String> defaultKeys) {
        this.defaultKeys = defaultKeys;
    }

    public HashMap<String, String> getCustomKeys() {
        return customKeys;
    }

    public void setCustomKeys(HashMap<String, String> customKeys) {
        this.customKeys = customKeys;
    }

    @Override
    public String toString() {
        return "Settings [customKeys=" + customKeys + ", defaultKeys=" + defaultKeys + ", fullScreen=" + fullScreen
                + ", sound=" + sound + ", soundTrack=" + soundTrack + "]";
    }
}