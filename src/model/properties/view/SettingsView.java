package model.properties.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import model.properties.controller.SettingsController;

public class SettingsView extends JPanel {
    private static final long serialVersionUID = 1L;

    // instancia de la interfaz
    private static SettingsView mainView;

    // componentes de la interfaz
    private JFrame mainFrame;
    private OptionsPanel optionsPanel;
    private ButtonsPanel buttonsPanel;

    private SettingsView() {
        mainFrame = new JFrame("Settings");

        // leyendo la config de properties.json
        SettingsController.readSettings();

        optionsPanel = new OptionsPanel(new GridBagLayout());

        buttonsPanel = new ButtonsPanel(new GridBagLayout());
        buttonsPanel.addButtonListener(optionsPanel);
    }

    public static SettingsView getMainView() {
        if(mainView == null) {
            mainView = new SettingsView();
        }

        return mainView;
    }

    public void initUx() {
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        this.add(optionsPanel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        this.add(buttonsPanel, gbc);

        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(400, 300);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setResizable(false);
        mainFrame.add(this);
        mainFrame.setVisible(true);
    }

    public void stateChanged(boolean newState) {
        if(newState) {
            mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        }
        else {
            mainFrame.setSize(400, 300);
            mainFrame.setLocationRelativeTo(null);
        }
    }
}