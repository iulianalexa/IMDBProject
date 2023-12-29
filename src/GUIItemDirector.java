import javax.swing.*;
import java.awt.*;

public class GUIItemDirector extends GUIItemGeneric<String> {
    public GUIItemDirector() {
        super();
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());

        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel nameLabel = new JLabel("Director name:");
        JTextField nameField = new JTextField(25);
        infoPanel.add(nameLabel);
        infoPanel.add(nameField);

        JPanel buttonPanel = new JPanel(new BorderLayout());
        JButton submitButton = new JButton("Submit");
        buttonPanel.add(submitButton, BorderLayout.CENTER);

        add(infoPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
        pack();
        setVisible(true);

        submitButton.addActionListener(e -> {
            String name = nameField.getText();
            if (name.isEmpty()) {
                showErrorDialog("Director name cannot be empty!");
                return;
            }

            super.setItem(name);
            dispose();
        });
    }

    private static void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(
                null,
                message,
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}
