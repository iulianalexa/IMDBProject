import javax.swing.*;
import java.awt.*;

public class GUIRatingPopup extends JFrame {
    public GUIRatingPopup(Production production, Rating rating) {
        initializeUI(production, rating);
    }

    private void initializeUI(Production production, Rating rating) {
        setLayout(new BorderLayout());

        JPanel buttonToolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton removeButton = new JButton("Remove review");
        buttonToolbar.add(removeButton);

        removeButton.addActionListener(e -> {
            if (rating != null) {
                production.removeRating(rating);
                dispose();
            }
        });

        if (rating != null) {
            add(buttonToolbar, BorderLayout.NORTH);
        }

        add(getPanel(production, rating), BorderLayout.CENTER);
        setLocationRelativeTo(null);
        pack();
        setVisible(true);
    }

    private JPanel getPanel(Production production, Rating rating) {
        JPanel panel0 = new JPanel(new BorderLayout());
        JPanel panel1 = new JPanel(new GridLayout(1, 1));

        JLabel scoreLabel = new JLabel("Score:");
        JTextField textField = new JTextField(20);
        panel1.add(scoreLabel);
        panel1.add(textField);

        JTextArea commentTextArea = new JTextArea(10, 20);
        commentTextArea.setLineWrap(true);
        commentTextArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(commentTextArea);

        JButton submitButton = new JButton("Submit");

        JPanel usernamePanel = new JPanel(new GridLayout(1, 1));
        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameTextField = new JTextField();
        usernameTextField.setEditable(false);
        usernamePanel.add(usernameLabel);
        usernamePanel.add(usernameTextField);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(panel0, BorderLayout.CENTER);

        panel0.add(panel1, BorderLayout.NORTH);
        panel0.add(scrollPane, BorderLayout.CENTER);
        if (rating == null) {
            panel0.add(submitButton, BorderLayout.SOUTH);
        } else {
            textField.setEditable(false);
            commentTextArea.setEditable(false);

            textField.setText(Integer.toString(rating.getRating()));
            commentTextArea.setText(rating.getComment());
            usernameTextField.setText(rating.getUsername());
            panel.add(usernamePanel, BorderLayout.NORTH);
        }


        submitButton.addActionListener(e -> {
            String comment = commentTextArea.getText();
            if (comment.isEmpty()) {
                showErrorDialog("Comment cannot be empty.");
                return;
            }

            int score;
            try {
                score = Integer.parseInt(textField.getText());
            } catch (NumberFormatException ex) {
                showErrorDialog("You did not input a number.");
                return;
            }

            if (score < 1 || score > 10) {
                showErrorDialog("Your score must be 1-10");
                return;
            }

            if (IMDB.getInstance().getCurrentUser() instanceof Regular<?> regular) {
                regular.rate(production, score, comment);
                dispose();
            }
        });

        return panel;
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
