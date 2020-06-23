package model.properties.model;

import java.util.HashMap;

public class Settings {
    private boolean sound;
    private HashMap<String, String> defaultKeys, customKeys;

    private static Settings settings;

    private Settings(boolean sound, HashMap<String, String> defaultKeys, HashMap<String, String> customKeys) {
        this.sound = sound;
        this.defaultKeys = defaultKeys;
        this.customKeys = customKeys;
    }

    public static Settings getSettings() {
        if(settings == null) {
            HashMap<String, String> defaultKeys = new HashMap<String, String>(7);
            HashMap<String, String> customKeys = new HashMap<String, String>(7);

            defaultKeys.put("Sonido", "83");
            defaultKeys.put("Pausa", "80");
            defaultKeys.put("Arriba", "38");
            defaultKeys.put("Abajo", "40");
            defaultKeys.put("Izquierda", "37");
            defaultKeys.put("Derecha", "39");
            defaultKeys.put("Acción", "32");
            defaultKeys.put("Detonador", "68");

            settings = new Settings(true, defaultKeys, customKeys);
        }

        return settings;
    }

    public boolean isSound() {
        return sound;
    }

    public void setSound(boolean sound) {
        this.sound = sound;
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
        return "Settings [customKeys=" + customKeys + ", defaultKeys=" + defaultKeys +
            ", sound=" + sound + "]";
    }
}