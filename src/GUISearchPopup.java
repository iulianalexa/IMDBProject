import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GUISearchPopup extends JFrame {
    private enum SearchType {
        MOVIE,
        SERIES,
        ACTOR
    }

    private JRadioButton movieRadioButton;
    private JRadioButton seriesRadioButton;
    private JRadioButton actorRadioButton;
    private JTextField textField;
    private JButton okButton;
    private JButton cancelButton;

    public GUISearchPopup(GUIMainFrame mainFrame) {
        initializeUI(mainFrame);
    }

    private void initializeUI(GUIMainFrame mainFrame) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Radio Buttons
        ButtonGroup buttonGroup = new ButtonGroup();

        movieRadioButton = new JRadioButton("Movie");
        seriesRadioButton = new JRadioButton("Series");
        actorRadioButton = new JRadioButton("Actor");

        buttonGroup.add(movieRadioButton);
        buttonGroup.add(seriesRadioButton);
        buttonGroup.add(actorRadioButton);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 0.2;
        gbc.anchor = GridBagConstraints.WEST;
        add(movieRadioButton, gbc);

        gbc.gridy = 1;
        add(seriesRadioButton, gbc);

        gbc.gridy = 2;
        add(actorRadioButton, gbc);

        textField = new JTextField(20);
        okButton = new JButton("OK");
        cancelButton = new JButton("Cancel");

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 0.2;
        gbc.anchor = GridBagConstraints.WEST;
        add(textField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.weightx = 0.2;
        gbc.weighty = 0.2;
        gbc.anchor = GridBagConstraints.EAST;
        add(okButton, gbc);

        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.weightx = 0.2;
        gbc.weighty = 0.2;
        gbc.anchor = GridBagConstraints.EAST;
        add(cancelButton, gbc);

        pack();
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);

        cancelButton.addActionListener(e -> dispose());

        okButton.addActionListener(e -> {
            String query = textField.getText();
            boolean result;

            if (query.isEmpty()) {
                showErrorDialog("Search field is empty!");
                return;
            }

            if (movieRadioButton.isSelected()) {
                result = executeSearch(query, SearchType.MOVIE, mainFrame);
            } else if (seriesRadioButton.isSelected()) {
                result = executeSearch(query, SearchType.SERIES, mainFrame);
            } else if (actorRadioButton.isSelected()) {
                result = executeSearch(query, SearchType.ACTOR, mainFrame);
            } else {
                showErrorDialog("You didn't select anything.");
                return;
            }

            if (result) {
                // Successful, dispose
                dispose();
            } else {
                // Error
                showErrorDialog("No results found!");
            }
        });
    }

    public static<T> String getName(T content, Class<T> tClass) {
        if (Production.class.isAssignableFrom(tClass)) {
            return ((Production) content).getTitle();
        } else if (Actor.class.isAssignableFrom(tClass)) {
            return ((Actor) content).getName();
        }

        return "";
    }

    private static<T> T search(String query, List<T> contentList, Class<T> tClass) {
        query = query.strip();

        // Exact match
        for (T content : contentList) {
            if (getName(content, tClass).equalsIgnoreCase(query)) {
                return content;
            }
        }

        // Partial match
        for (T content : contentList) {
            if (getName(content, tClass).toLowerCase().startsWith(query.toLowerCase())) {
                return content;
            }
        }

        return null;
    }

    private static boolean executeSearch(String query, SearchType searchType, GUIMainFrame mainFrame) {
        switch (searchType) {
            case MOVIE:
                Movie movie = search(query, IMDB.getInstance().getMovieList(), Movie.class);
                if (movie != null) {
                    mainFrame.viewProduction(movie);
                    return true;
                }
                break;
            case SERIES:
                Series series = search(query, IMDB.getInstance().getSeriesList(), Series.class);
                if (series != null) {
                    mainFrame.viewProduction(series);
                    return true;
                }
                break;
            case ACTOR:
                Actor actor = search(query, IMDB.getInstance().getActors(), Actor.class);
                if (actor != null) {
                    mainFrame.viewActor(actor);
                    return true;
                }
                break;
        }

        return false;
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
