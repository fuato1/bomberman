package model.properties.view;

import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import model.properties.controller.SettingsController;
import model.properties.view.views_listeners.ButtonListener;

public class ButtonsPanel extends JPanel implements ActionListener {
    private static final long serialVersionUID = 1L;

    // listener para cambiar las config en OptionsPanel
    private ButtonListener bl;

    // botones para guardar y reiniciar las config
    private JButton save, reset;

    // constructor 
    public ButtonsPanel(LayoutManager layout) {
        super(layout);
        save = new JButton("Guardar Controles");
        reset = new JButton("Reiniciar Controles");

        save.addActionListener(this);
        reset.addActionListener(this);

        this.add(save);
        this.add(reset);
    }
    
    // agregando OptionsPanel para poder comunicar cambios de estado
    public void addButtonListener(ButtonListener bl) {
        this.bl = bl;
    }

    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand() == save.getActionCommand()) {
            saveConfig();
        }
        if(e.getActionCommand() == reset.getActionCommand()) {
            resetConfig();
        }
    }

    // guardar config
    private void saveConfig() {
        bl.saveButtonActioned();
        SettingsController.saveSettings();
    }

    // reiniciar config
    private void resetConfig() {
        bl.resetButtonActioned();
        SettingsController.saveSettings();
    }
}