package model.properties.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.WindowEvent;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

import model.properties.controller.SettingsController;
import model.properties.view.views_listeners.ScreenStateListener;

public class SettingsView extends JPanel {
    private static final long serialVersionUID = 1L;

    // instancia de la interfaz
    private static SettingsView mainView;

    // componentes de la interfaz
    private JFrame mainFrame;
    private OptionsPanel optionsPanel;
    private ButtonsPanel buttonsPanel;

    private SettingsView() {
        mainFrame = new JFrame("Configuración del Jugador");

        // leyendo la config de properties.json
        SettingsController.readSettings();

        optionsPanel = new OptionsPanel(new GridBagLayout());

        buttonsPanel = new ButtonsPanel(new GridBagLayout());
        buttonsPanel.addButtonListener(optionsPanel);
    }

    public static SettingsView getMainView() {
        if (mainView == null) {
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

        try {
            URL iconURL = getClass().getResource("/imagenes/config.png");
            ImageIcon icon = new ImageIcon(iconURL);
            mainFrame.setIconImage(icon.getImage());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        mainFrame.setSize(500, 400);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setResizable(false);
        mainFrame.add(this);
        mainFrame.setVisible(true);
    }

    public void addOptionsPanelSSL(ScreenStateListener ssl) {
        optionsPanel.addScreenStateListener(ssl);
    }

    public void closeConfig() {
        mainFrame.dispatchEvent(new WindowEvent(mainFrame, WindowEvent.WINDOW_CLOSING));
    }
}