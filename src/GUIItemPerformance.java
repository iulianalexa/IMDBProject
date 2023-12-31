import javax.swing.*;
import java.awt.*;

public class GUIItemPerformance extends GUIItemGeneric<Performance> {
    public GUIItemPerformance(Performance performance) {
        super(performance);

        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());

        JPanel infoPanel = new JPanel(new GridLayout(2, 2));
        JLabel titleLabel = new JLabel("Production title:");
        JTextField titleField = new JTextField(25);

        if (getItem() != null) {
            titleField.setText(getItem().getTitle());
        }

        infoPanel.add(titleLabel);
        infoPanel.add(titleField);

        JLabel typeLabel = new JLabel("Production type:");
        JComboBox<ProductionType> typeJComboBox = new JComboBox<>(ProductionType.values());

        if (getItem() != null) {
            typeJComboBox.setSelectedItem(getItem().getType());
        }

        infoPanel.add(typeLabel);
        infoPanel.add(typeJComboBox);

        JPanel buttonPanel = new JPanel(new BorderLayout());
        JButton submitButton = new JButton("Submit");
        buttonPanel.add(submitButton, BorderLayout.CENTER);

        add(infoPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
        pack();
        setVisible(true);

        submitButton.addActionListener(e -> {
            String title = titleField.getText();
            if (title.isEmpty()) {
                showErrorDialog("Production title cannot be empty!");
                return;
            }

            ProductionType type = (ProductionType) typeJComboBox.getSelectedItem();

            setItem(new Performance(title, type));
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
