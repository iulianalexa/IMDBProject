import javax.swing.*;
import java.awt.*;

public class GUIItemEpisode extends GUIItemGeneric<Episode> {
    public GUIItemEpisode(Episode episode) {
        super(episode);
        if (episode == null) {
            setItem(new Episode());
        }

        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());

        JPanel infoPanel = new JPanel(new GridLayout(2, 2));
        JLabel nameLabel = new JLabel("Episode name:");
        JTextField nameField = new JTextField(25);

        if (getItem() != null) {
            nameField.setText(getItem().getEpisodeName());
        }

        infoPanel.add(nameLabel);
        infoPanel.add(nameField);

        JLabel durationLabel = new JLabel("Episode duration:");
        JTextField durationField = new JTextField(25);

        if (getItem() != null) {
            durationField.setText(getItem().getDuration());
        }

        infoPanel.add(durationLabel);
        infoPanel.add(durationField);

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
                showErrorDialog("Episode name cannot be empty!");
                return;
            }

            String duration = durationField.getText();
            if (duration.isEmpty()) {
                duration = null;
            }

            getItem().setEpisodeName(name);
            getItem().setDuration(duration);
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
