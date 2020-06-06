package model.properties.view.keys_panel;

import java.awt.LayoutManager;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

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

        this.add(fieldName);
        this.add(fieldValue);
    }

    // get fieldName
    public String getFieldName() {
        return fieldName.getText();
    }

    // get fieldValue
    public String getFieldValue() {
        return fieldValue.getText();
    }

    // set fieldValue
    public void setFieldValue(String fieldValue) {
        this.fieldValue.setText(fieldValue);
    }
}