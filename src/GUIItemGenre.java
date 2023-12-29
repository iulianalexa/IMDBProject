import javax.swing.*;
import java.awt.*;

public class GUIItemGenre extends GUIItemGeneric<Genre> {
    public GUIItemGenre(Genre genre) {
        super(genre);
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());

        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel nameLabel = new JLabel("Genre:");
        JComboBox<Genre> genreJComboBox = new JComboBox<>(Genre.values());

        if (getItem() != null) {
            genreJComboBox.setSelectedItem(getItem());
        }

        infoPanel.add(nameLabel);
        infoPanel.add(genreJComboBox);

        JPanel buttonPanel = new JPanel(new BorderLayout());
        JButton submitButton = new JButton("Submit");
        buttonPanel.add(submitButton, BorderLayout.CENTER);

        add(infoPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
        pack();
        setVisible(true);

        submitButton.addActionListener(e -> {
            if (genreJComboBox.getSelectedItem() instanceof Genre genre) {
                super.setItem(genre);
                dispose();
            }
        });
    }
}
