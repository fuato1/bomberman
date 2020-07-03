package model.properties.view.keys_panel;

import java.awt.LayoutManager;
import java.awt.event.KeyEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import model.properties.controller.SettingsController;

import java.awt.event.KeyListener;

public class FormField extends JPanel {
    private static final long serialVersionUID = 1L;

    // label y campo de texto de cada campo de formulario
    private JLabel fieldName;
    private JTextField fieldValue;

    // constructor
    public FormField(LayoutManager layout, String name, String value) {
        super(layout);

        fieldName = new JLabel(name, JLabel.CENTER);
        fieldValue = new JTextField(value);

        fieldValue.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
                fieldValue.setText(KeyEvent.getKeyText(e.getKeyCode()));
                SettingsController.setSingleCustomKey(fieldName.getText(), Integer.toString(e.getKeyCode()));
            }
        });

        this.add(fieldName);
        this.add(fieldValue);
    }

    /*
     * Getters.
     */
    public String getFieldName() {
        return fieldName.getText();
    }

    public String getFieldValue() {
        return fieldValue.getText();
    }

    /*
     * Setters.
     */
    public void setFieldValue(String fieldValue) {
        this.fieldValue.setText(KeyEvent.getKeyText(Integer.parseInt(fieldValue)));
    }
}