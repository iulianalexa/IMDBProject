import javax.swing.*;
import java.awt.*;

public class GUIItemActor extends GUIItemGeneric<Actor> {
    public GUIItemActor(Actor actor) {
        super(actor);
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());

        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel nameLabel = new JLabel("Actor name:");
        JTextField nameField = new JTextField(25);

        if (getItem() != null) {
            nameField.setText(getItem().getName());
        }

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
            Actor actor = IMDB.getInstance().searchForActor(name);
            if (actor == null) {
                showErrorDialog("Could not find actor!");
                return;
            }

            super.setItem(actor);
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
