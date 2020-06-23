package model.properties.view.keys_panel;

import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.KeyEvent;
import java.util.Vector;

import javax.swing.JPanel;

import model.properties.controller.SettingsController;

public class KeysPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private Vector<FormField> keysPanelFields;

    public KeysPanel(LayoutManager layout) {
        super(layout);

        keysPanelFields = new Vector<FormField>(SettingsController.getCustomKeys().size());

        for (String key : SettingsController.getCustomKeys().keySet()) {
            keysPanelFields.add(new FormField(
                new GridLayout(1, 2), 
                key, 
                KeyEvent.getKeyText(Integer.parseInt(SettingsController.getCustomKeys().get(key)))
            ));
            this.add(keysPanelFields.lastElement());
        }
    }

    public void resetKeysPanelFields() {
        for (FormField ff : keysPanelFields) {
            ff.setFieldValue(SettingsController.getCustomKeys().get(ff.getFieldName()));
        }
    }
}