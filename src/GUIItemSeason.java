import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GUIItemSeason extends GUIItemGeneric<Season> {
    public GUIItemSeason(Season season) {
        super(season);
        if (season == null) {
            setItem(new Season("", new ArrayList<>()));
        }

        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());

        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel nameLabel = new JLabel("Season name:");
        JTextField nameField = new JTextField(25);

        if (getItem() != null) {
            nameField.setText(getItem().getName());
        }

        infoPanel.add(nameLabel);
        infoPanel.add(nameField);

        JPanel episodeListPanel = new JPanel(new BorderLayout());
        JButton episodeListButton = new JButton("Episodes...");
        episodeListPanel.add(episodeListButton);

        JPanel buttonPanel = new JPanel(new BorderLayout());
        JButton submitButton = new JButton("Submit");
        buttonPanel.add(submitButton, BorderLayout.CENTER);

        add(infoPanel, BorderLayout.NORTH);
        add(episodeListPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
        pack();
        setVisible(true);

        episodeListButton.addActionListener(e -> {
            new GUIGenericList<>(
                    Episode.class,
                    getItem().getEpisodes(),
                    new DefaultListCellRenderer(),
                    GUIItemEpisode.class
            );
        });

        submitButton.addActionListener(e -> {
            String name = nameField.getText();
            if (name.isEmpty()) {
                showErrorDialog("Season name cannot be empty!");
                return;
            }

            getItem().setName(name);
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
