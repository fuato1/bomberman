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
import model.properties.view.keys_panel.FormField;
import model.properties.view.keys_panel.KeysPanel;
import model.properties.view.views_listeners.ButtonListener;
import model.properties.view.views_listeners.ScreenStateListener;

public class OptionsPanel extends JPanel implements ActionListener, ButtonListener {
    private static final long serialVersionUID = 1L;

    // listener para estado de pantalla completa
    private ScreenStateListener ssl;

    // componentes del panel de opciones
    private JLabel fullScreenL;
    private JCheckBox fullScreenCB;

    private JLabel soundL;
    private JCheckBox soundCB;

    private JLabel keysL;
    private KeysPanel keysP;

    private JLabel soundTrackL;
    private JTextField soundTrackTF;

    public OptionsPanel(LayoutManager layout) {
        super(layout);

        fullScreenCB = new JCheckBox("(Descativa por defecto)", SettingsController.getFullScreenState());
        fullScreenL = new JLabel("- Pantalla Completa: ", JLabel.CENTER);

        soundCB = new JCheckBox("(Activado por defecto)", SettingsController.getSoundState());
        soundL = new JLabel("- Sonido: ", JLabel.CENTER);

        keysL = new JLabel("- Teclas: ", JLabel.CENTER);
        keysP = new KeysPanel(new GridLayout(7, 1));

        soundTrackTF = new JTextField(SettingsController.getSoundTrack(), JTextField.CENTER);
        soundTrackL = new JLabel("- Pista Musical: ", JLabel.CENTER);

        fullScreenCB.addActionListener(this);
        soundCB.addActionListener(this);

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        this.add(fullScreenL, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        this.add(fullScreenCB, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        this.add(soundL, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        this.add(soundCB, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        this.add(keysL, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        this.add(keysP, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        this.add(soundTrackL, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        this.add(soundTrackTF, gbc);
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
            System.out.println("cambiando a pantalla completa...");
            SettingsController.setFullSreenState(true);
            ssl.stateChanged(true);
        }
        else {
            System.out.println("cambiando a ventana...");
            SettingsController.setFullSreenState(false);
            ssl.stateChanged(false);
        }
    }

    private void changeSoundState() {
        if(soundCB.isSelected()) {
            System.out.println("activando sonido...");
            SettingsController.setSoundState(true);
        }
        else {
            System.out.println("desactivando sonido...");
            SettingsController.setSoundState(false);
        }
    }

    @Override
    public void saveButtonActioned() {
        for (FormField ff : keysP.getKeysPanelFields()) {
            SettingsController.setSingleCustomKey(ff.getFieldName(), ff.getFieldValue());
        }

        SettingsController.saveSettings();
        SettingsController.setSoundTrack(soundTrackTF.getText());
    }

    @Override
    public void resetButtonActioned() {
        SettingsController.setFullSreenState(false);
        fullScreenCB.setSelected(false);
        ssl.stateChanged(false);

        SettingsController.setSoundState(true);
        soundCB.setSelected(true);

        SettingsController.resetCustomKeys();
        keysP.resetKeysPanelFields();

        SettingsController.setSoundTrack("original");
        soundTrackTF.setText("original");
    }
}



