package model.properties.view;

import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.properties.controller.SettingsController;
import model.properties.view.keys_panel.FormField;
import model.properties.view.keys_panel.KeysPanel;
import model.properties.view.views_listeners.ButtonListener;

public class OptionsPanel extends JPanel implements ActionListener, ButtonListener {
    private static final long serialVersionUID = 1L;

    // componentes del panel de opciones
    private JLabel soundL;
    private JCheckBox soundCB;

    private JLabel keysL;
    private KeysPanel keysP;

    public OptionsPanel(LayoutManager layout) {
        super(layout);

        soundCB = new JCheckBox("(Activado por defecto)", SettingsController.getSoundState());
        soundL = new JLabel("- Sonido: ", JLabel.CENTER);

        keysL = new JLabel("- Teclas: ", JLabel.CENTER);
        keysP = new KeysPanel(new GridLayout(7, 1));

        soundCB.addActionListener(this);

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        this.add(soundL, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        this.add(soundCB, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        this.add(keysL, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        this.add(keysP, gbc);
    }

    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand() == soundCB.getActionCommand()) {
            changeSoundState();
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
        SettingsController.saveSettings();
    }

    @Override
    public void resetButtonActioned() {
        SettingsController.setSoundState(true);
        soundCB.setSelected(true);

        SettingsController.resetCustomKeys();
        keysP.resetKeysPanelFields();
    }
}



