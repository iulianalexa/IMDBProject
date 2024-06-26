import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GUIProductionPopup extends JFrame {
    JTextField releaseYearField = new JTextField(25);
    JTextField durationField;
    JButton seasonsButton = new JButton("Seasons...");

    Staff<?> staff;

    public GUIProductionPopup(Production production) {
        if (IMDB.getInstance().getCurrentUser() instanceof Staff<?> staff1) {
            this.staff = staff1;
        } else {
            return;
        }

        initializeUI(production);
    }

    private void initializeUI(Production production) {
        setLayout(new BorderLayout());

        /*
        Title: field
        Description: field
        Type: ComboBox
        Director list button, Actor list button, Genre list button
         */

        /* MOVIE
        Release Year: field
        Duration: field
         */

        /* SERIES
        Release Year: field
        Season list button
         */

        add(getMainProductionUI(production), BorderLayout.CENTER);

        setLocationRelativeTo(null);
        pack();
        setVisible(true);
    }

    private JPanel getMainProductionUI(Production production) {
        final List<String> directors = new ArrayList<>();
        final List<Actor> actors = new ArrayList<>();
        final List<Genre> genres = new ArrayList<>();
        final List<Season> seasons = new ArrayList<>();

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JPanel titlePanel = new JPanel(new GridLayout(1, 2));
        JLabel titleLabel = new JLabel("Title:");
        JTextField titleField = new JTextField(25);
        titlePanel.add(titleLabel);
        titlePanel.add(titleField);

        JPanel descriptionPanel = new JPanel(new GridLayout(1, 2));
        JLabel descriptionLabel = new JLabel("Description:");
        JTextArea descriptionTextArea = new JTextArea(10, 25);
        descriptionTextArea.setLineWrap(true);
        descriptionTextArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(descriptionTextArea);
        descriptionPanel.add(descriptionLabel);
        descriptionPanel.add(scrollPane);

        JPanel trailerPanel = new JPanel(new GridLayout(1, 2));
        JLabel trailerLabel = new JLabel("Trailer link:");
        JTextField trailerField = new JTextField(25);
        trailerPanel.add(trailerLabel);
        trailerPanel.add(trailerField);

        JPanel typePanel = new JPanel(new GridLayout(1, 2));
        JLabel typeLabel = new JLabel("Type:");
        JComboBox<ProductionType> typeComboBox = new JComboBox<>(ProductionType.values());
        typePanel.add(typeLabel);
        typePanel.add(typeComboBox);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 3));
        JButton directorListButton = new JButton("Directors...");
        JButton actorListButton = new JButton("Actors...");
        JButton genreListButton = new JButton("Genres...");
        buttonPanel.add(directorListButton);
        buttonPanel.add(actorListButton);
        buttonPanel.add(genreListButton);

        JPanel augmentedPanel = new JPanel(new BorderLayout());

        JPanel submitButtonPanel = new JPanel(new BorderLayout());
        JButton submitButton = new JButton("Submit");
        submitButtonPanel.add(submitButton, BorderLayout.CENTER);

        panel.add(titlePanel);
        panel.add(descriptionPanel);
        panel.add(trailerPanel);
        panel.add(typePanel);
        panel.add(augmentedPanel);
        panel.add(buttonPanel);
        panel.add(submitButtonPanel);

        typeComboBox.addActionListener(e -> {
            if (typeComboBox.getSelectedItem() instanceof ProductionType productionType) {
                if (productionType == ProductionType.MOVIE) {
                    setMoviePanel(augmentedPanel);
                } else if (productionType == ProductionType.SERIES) {
                    setSeriesPanel(augmentedPanel);
                }
            }
        });

        typeComboBox.setSelectedItem(ProductionType.MOVIE);

        submitButton.addActionListener(e -> {
            // Sanity checks
            String title = titleField.getText();
            String description = descriptionTextArea.getText();
            String trailerLink = trailerField.getText();
            int releaseYear;

            if (title.isEmpty()) {
                showErrorDialog("Title cannot be empty!");
                return;
            }

            if (description.isEmpty()) {
                description = null;
            }

            if (trailerLink.isEmpty()) {
                trailerLink = null;
            }

            try {
                releaseYear = Integer.parseInt(releaseYearField.getText());
            } catch (NumberFormatException ex) {
                showErrorDialog("Release Year is not a valid number!");
                return;
            }

            Production newProduction;

            if (typeComboBox.getSelectedItem() instanceof ProductionType productionType) {
                if (productionType == ProductionType.MOVIE) {
                    String duration = durationField.getText();
                    if (duration.isEmpty()) {
                        duration = null;
                    }
                    newProduction = new Movie(title, description, duration, releaseYear);

                } else if (productionType == ProductionType.SERIES) {
                    newProduction = new Series(title, description, releaseYear, 0);

                    // Create episodes map
                    Map<String, List<Episode>> episodes = new HashMap<>();
                    for (Season season : seasons) {
                        episodes.put(season.getName(), season.getEpisodes());
                    }

                    ((Series) newProduction).setNumSeasons(seasons.size());
                    ((Series) newProduction).setEpisodes(episodes);
                } else {
                    // Impossible case
                    return;
                }
            } else {
                // Impossible case
                return;
            }

            newProduction.setDirectors(directors);
            newProduction.setActors(actors);
            newProduction.setGenres(genres);
            newProduction.setTrailerLink(trailerLink);

            if (production == null) {
                staff.addProductionSystem(newProduction);
            } else {
                production.copyNonUpdatableInformationOver(newProduction);
                staff.removeProductionSystem(production);
                staff.addProductionSystem(newProduction);
            }
            dispose();
        });

        directorListButton.addActionListener(e -> {
            new GUIGenericList<>(
                    String.class,
                    directors,
                    new DefaultListCellRenderer(),
                    GUIItemDirector.class
            );
        });

        actorListButton.addActionListener(e -> {
            new GUIGenericList<>(
                    Actor.class,
                    actors,
                    new ActorListCellRenderer(),
                    GUIItemActor.class
            );
        });

        genreListButton.addActionListener(e -> {
            new GUIGenericList<>(
                    Genre.class,
                    genres,
                    new DefaultListCellRenderer(),
                    GUIItemGenre.class
            );
        });

        seasonsButton.addActionListener(e -> {
            new GUIGenericList<>(
                    Season.class,
                    seasons,
                    new DefaultListCellRenderer(),
                    GUIItemSeason.class
            );
        });

        if (production != null) {
            // Set appropriate fields
            titleField.setText(production.getTitle());
            descriptionTextArea.setText(production.getPlot());
            trailerField.setText(production.getTrailerLink());
            directors.addAll(production.getDirectors());
            genres.addAll(production.getGenres());
            actors.addAll(production.getActors());
            if (production instanceof Movie movie) {
                typeComboBox.setSelectedItem(ProductionType.MOVIE);
                releaseYearField.setText(Integer.toString(movie.getReleaseYear()));
                durationField.setText(movie.getDuration());
            } else if (production instanceof Series series) {
                typeComboBox.setSelectedItem(ProductionType.SERIES);
                releaseYearField.setText(Integer.toString(series.getReleaseYear()));
                Map<String, List<Episode>> episodeMap = series.getSeasons();
                for (String key : episodeMap.keySet()) {
                    List<Episode> episodeList = episodeMap.get(key);
                    seasons.add(new Season(key, episodeList));
                }
            }
        }

        return panel;
    }

    private JPanel getMoviePanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2));
        JLabel releaseYearLabel = new JLabel("Release Year:");
        releaseYearField = new JTextField(25);
        JLabel durationLabel = new JLabel("Duration:");
        durationField = new JTextField(25);
        panel.add(releaseYearLabel);
        panel.add(releaseYearField);
        panel.add(durationLabel);
        panel.add(durationField);

        return panel;
    }

    private JPanel getSeriesPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JPanel releaseYearPanel = new JPanel(new GridLayout(1, 2));
        JLabel releaseYearLabel = new JLabel("Release Year:");
        releaseYearPanel.add(releaseYearLabel);
        releaseYearPanel.add(releaseYearField);

        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.add(seasonsButton, BorderLayout.CENTER);

        panel.add(releaseYearPanel);
        panel.add(buttonPanel);

        return panel;
    }

    private void setMoviePanel(JPanel moviePanel) {
        moviePanel.removeAll();
        JPanel insidePanel = getMoviePanel();
        moviePanel.add(insidePanel, BorderLayout.CENTER);
        moviePanel.updateUI();
    }

    private void setSeriesPanel(JPanel seriesPanel) {
        seriesPanel.removeAll();
        JPanel insidePanel = getSeriesPanel();
        seriesPanel.add(insidePanel, BorderLayout.CENTER);
        seriesPanel.updateUI();
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
