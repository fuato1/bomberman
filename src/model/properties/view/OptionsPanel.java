package model.properties.view;

import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import model.properties.controller.SettingsController;
import model.properties.view.keys_panel.KeysPanel;
import model.properties.view.views_listeners.ButtonListener;
import model.properties.view.views_listeners.ScreenStateListener;

public class OptionsPanel extends JPanel implements ActionListener, ButtonListener {
    private static final long serialVersionUID = 1L;

    // listener para estado de pantalla completa
    private ScreenStateListener ssl;

    // componentes del panel de opciones
    private JLabel playerNameL;
    private JTextField playerNameTF;

    private JLabel fullScreenL;
    private JCheckBox fullScreenCB;

    private JLabel soundL;
    private JCheckBox soundCB;

    private JLabel keysL;
    private KeysPanel keysP;

    public OptionsPanel(LayoutManager layout) {
        super(layout);

        playerNameTF = new JTextField(10);
        playerNameTF.setText(SettingsController.getPlayerName());
        playerNameL = new JLabel("- Nombre del Jugador: ", JLabel.CENTER);

        fullScreenCB = new JCheckBox("(Descativa por defecto)", SettingsController.getFullScreenState());
        fullScreenL = new JLabel("- Pantalla Completa: ", JLabel.CENTER);

        soundCB = new JCheckBox("(Activado por defecto)", SettingsController.getSoundState());
        soundL = new JLabel("- Sonido: ", JLabel.CENTER);

        keysL = new JLabel("- Teclas: ", JLabel.CENTER);
        keysP = new KeysPanel(new GridLayout(7, 1));

        fullScreenCB.addActionListener(this);
        soundCB.addActionListener(this);

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        this.add(playerNameL, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        this.add(playerNameTF, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        this.add(fullScreenL, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        this.add(fullScreenCB, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        this.add(soundL, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        this.add(soundCB, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        this.add(keysL, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        this.add(keysP, gbc);
    }

    public void addScreenStateListener(ScreenStateListener ssl) {
        this.ssl = ssl;
    }

    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand() == fullScreenCB.getActionCommand()) {
            changeScreenState();
        }
        if(e.getActionCommand() == soundCB.getActionCommand()) {
            changeSoundState();
        }
    }

    private void changeScreenState() {
        if(fullScreenCB.isSelected()) {
            SettingsController.setFullScreenState(true);
            ssl.stateChanged(true);
        }
        else {
            SettingsController.setFullScreenState(false);
            ssl.stateChanged(false);
        }
    }

    private void changeSoundState() {
        if(soundCB.isSelected()) {
            SettingsController.setSoundState(true);
        }
        else {
            SettingsController.setSoundState(false);
        }
    }

    @Override
    public void saveButtonActioned() {
        SettingsController.setPlayerName(playerNameTF.getText());
        SettingsController.saveSettings();
    }

    @Override
    public void resetButtonActioned() {
        SettingsController.setPlayerName("Player");
        playerNameTF.setText(SettingsController.getPlayerName());

        SettingsController.setFullScreenState(false);
        fullScreenCB.setSelected(false);
        ssl.stateChanged(false);

        SettingsController.setSoundState(true);
        soundCB.setSelected(true);

        SettingsController.resetCustomKeys();
        keysP.resetKeysPanelFields();
    }
}



