package model.properties.controller;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import model.properties.model.Settings;
import model.properties.view.SettingsView;

public class SettingsController {
    private static Settings model = Settings.getSettings();
    private static SettingsView view = SettingsView.getMainView();

    // metodo para cargar las config
    public static void readSettings() {
        JSONParser parser = new JSONParser();
        
        try(FileReader reader = new FileReader("src/properties.json")) {
            HashMap<Object, Object> config = (HashMap<Object, Object>) parser.parse(reader);

            // seteando controles desde properties.json
            setPlayerName((String) config.get("playerName"));
            setFullScreenState((boolean) config.get("fullScreen"));
            setSoundState((boolean) config.get("sound"));

            // seteando teclas desde properties.json
            JSONObject keys = (JSONObject) config.get("keys");

            for(String key : model.getDefaultKeys().keySet()) {
                model.getCustomKeys().put(key, (String) keys.get(key));
            }
        } catch(IOException e) {
            e.printStackTrace();
        } catch(ParseException p) {
            p.printStackTrace();
        }
    }

    // metodo para guardar en el archivo JSON la config
    public static void saveSettings() {
        JSONParser parser = new JSONParser();
        
        try(FileReader reader = new FileReader("src/properties.json")) {
            HashMap<Object, Object> config = (HashMap<Object, Object>) parser.parse(reader);

            // seteando controles desde properties.json
            config.put("playerName", model.getPlayerName());
            config.put("fullScreen", model.isFullScreen());
            config.put("sound", model.isSound());

            // seteando teclas desde properties.json
            JSONObject keys = (JSONObject) config.get("keys");

            for (String key : model.getCustomKeys().keySet()) {
                keys.put(key, model.getCustomKeys().get(key));
            }

            // escribiendo los nuevos datos en properties.json
            JSONObject json = (JSONObject) config;

            try (FileWriter file = new FileWriter("src/properties.json")) {
                file.write(json.toJSONString());
                file.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch(IOException e) {
            e.printStackTrace();
        } catch(ParseException p) {
            p.printStackTrace();
        }
    }

    // inicia la vista
    public static void initView() {
        view.initUx();
    }
    
    // para test
    public static String show() {
        return model.toString();
    }

    /*
        Getters  
    */
    public static String getPlayerName() {
        return model.getPlayerName();
    }

    public static boolean getFullScreenState() {
        return model.isFullScreen();
    }

    public static boolean getSoundState() {
        return model.isSound();
    }

    public static HashMap<String, String> getDefaultKeys() {
        return model.getDefaultKeys();
    }

    public static HashMap<String, String> getCustomKeys() {
        return model.getCustomKeys();
    }

    /*
        Setters 
    */
    public static void setPlayerName(String playerName) {
        model.setPlayerName(playerName);
    }

    public static void setFullScreenState(boolean state) {
        model.setFullScreen(state);
    }

    public static void setSoundState(boolean state) {
        model.setSound(state);
    }

    /*
        Setters para customKeys
        No hay setter para defaultKeys, ya que deberia ser inmutable
    */

    public static void setCustomKeys(HashMap<String, String> customKeys) {
        model.setCustomKeys(customKeys);
    }

    public static void setSingleCustomKey(String key, String value) {
        model.getCustomKeys().put(key, value);
    }

    public static void resetCustomKeys() {
        model.setCustomKeys(model.getDefaultKeys());
    }
}