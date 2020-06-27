package model.tests;

import model.SistemaJuegos;
import model.properties.controller.SettingsController;

public class TestSistemaJuegos {
    public static void main(String[] args) {
        SettingsController.initView();
        
        SistemaJuegos sj = new SistemaJuegos();
        sj.launch();
    }
}