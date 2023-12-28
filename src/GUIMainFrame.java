import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class GUIMainFrame extends JFrame {
    private JPanel header = new JPanel();
    private JPanel mainPage = new JPanel(new BorderLayout());

    private List<Genre> filteredGenres = new ArrayList<>();
    private int minimumReviewCount = 0;

    public GUIMainFrame() {
        super("IMDB");
        setSize(1280, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        // Add Header
        add(header, BorderLayout.NORTH);

        // Add Main Page
        add(mainPage, BorderLayout.CENTER);

        viewFrontPage();
        refreshHeader(header);
        setVisible(true);
    }

    public void updateFilters(List<Genre> filteredGenres, int minimumReviewCount) {
        this.filteredGenres = filteredGenres;
        this.minimumReviewCount = minimumReviewCount;
        viewFrontPage();
    }

    public static List<Production> getProductionsListFromFilterCriteria(List<Genre> filteredGenres, int minimumReviewCount) {
        List<Production> totalProductionsList = IMDB.getInstance().getProductionList();
        List<Production> filteredProductionsList = new ArrayList<>();
        for (Production production : totalProductionsList) {
            if ((filteredGenres.isEmpty() || !Collections.disjoint(filteredGenres, production.getGenres())) && production.getReviewCount() >= minimumReviewCount) {
                filteredProductionsList.add(production);
            }
        }

        return filteredProductionsList;
    }

    public void viewFrontPage() {
        JPanel mainPage = this.mainPage;
        List<Genre> filteredGenres = this.filteredGenres;
        int minimumReviewCount = this.minimumReviewCount;

        mainPage.removeAll();
        mainPage.setLayout(new BorderLayout());

        List<Production> productionsList = getProductionsListFromFilterCriteria(filteredGenres, minimumReviewCount);
        Production[] productionsArr = new Production[productionsList.size()];
        productionsList.toArray(productionsArr);
        JList<Production> productionsJList = new JList<>(productionsArr);

        // Create a custom cell renderer to only display production titles
        productionsJList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Production production) {
                    label.setText(production.getTitle());
                }

                return label;
            }
        });

        // Create a scroll pane for the JList
        JScrollPane scrollPane = new JScrollPane(productionsJList);

        // Set up selection mode to allow single selection
        productionsJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Add a list selection listener to respond to item clicks
        productionsJList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                // Get the selected value
                Production selectedValue = productionsJList.getSelectedValue();

                this.viewProduction(selectedValue);
            }
        });

        // Create button toolbar
        JPanel buttonToolbarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton filterButton = new JButton("Filter");
        JButton searchButton = new JButton("Search");
        JButton actorsButton = new JButton("View Actors");

        buttonToolbarPanel.add(filterButton);
        buttonToolbarPanel.add(searchButton);
        buttonToolbarPanel.add(actorsButton);
        mainPage.add(buttonToolbarPanel, BorderLayout.NORTH);
        mainPage.add(scrollPane, BorderLayout.CENTER);
        mainPage.updateUI();

        filterButton.addActionListener(e -> new GUIFilterPopup(this));

        searchButton.addActionListener(e -> new GUISearchPopup(this));

        actorsButton.addActionListener(e -> viewActorsPage());
    }

    public void viewActorsPage() {
        JPanel mainPage = this.mainPage;
        List<Genre> filteredGenres = this.filteredGenres;
        int minimumReviewCount = this.minimumReviewCount;

        mainPage.removeAll();
        mainPage.setLayout(new BorderLayout());

        Set<Actor> actors = IMDB.getInstance().getActors();
        Actor[] actorsArr = new Actor[actors.size()];
        actors.toArray(actorsArr);
        JList<Actor> actorsJList = new JList<>(actorsArr);

        // Create a custom cell renderer to only display actor names
        actorsJList.setCellRenderer(new ActorListCellRenderer());

        // Create a scroll pane for the JList
        JScrollPane scrollPane = new JScrollPane(actorsJList);

        // Set up selection mode to allow single selection
        actorsJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Add a list selection listener to respond to item clicks
        actorsJList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                // Get the selected value
                Actor selectedValue = actorsJList.getSelectedValue();

                this.viewActor(selectedValue);
            }
        });

        // Create button toolbar
        JPanel buttonToolbarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton backButton = new JButton("Go Back");

        buttonToolbarPanel.add(backButton);
        mainPage.add(buttonToolbarPanel, BorderLayout.NORTH);
        mainPage.add(scrollPane, BorderLayout.CENTER);
        mainPage.updateUI();

        backButton.addActionListener(e -> {
            this.viewFrontPage();
        });
    }

    public static int getReleaseYear(Production production) {
        int releaseYear = 0;
        if (production instanceof Movie movie) {
            releaseYear = movie.getReleaseYear();
        } else if (production instanceof Series series) {
            releaseYear = series.getReleaseYear();
        }

        return releaseYear;
    }

    public static JPanel addToNorth(JPanel lastPanel, JComponent component) {
        BorderLayout borderLayout = new BorderLayout();
        borderLayout.setVgap(20);
        JPanel newPanel = new JPanel(borderLayout);
        newPanel.add(component, BorderLayout.NORTH);
        lastPanel.add(newPanel, BorderLayout.CENTER);
        return newPanel;
    }

    public void viewProduction(Production production) {
        int releaseYear = getReleaseYear(production);
        JPanel mainPage = this.mainPage;

        mainPage.removeAll();
        mainPage.setLayout(new BorderLayout());
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPage.add(new JScrollPane(mainPanel), BorderLayout.CENTER);
        JPanel currentPanel = mainPanel;

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton backButton = new JButton("Go back");
        JButton addToFavoritesButton = new JButton("Add to Favorites");
        JButton addReviewButton = new JButton("Add review");

        buttonPanel.add(backButton);
        buttonPanel.add(addToFavoritesButton);
        buttonPanel.add(addReviewButton);

        currentPanel = addToNorth(currentPanel, buttonPanel);

        JPanel titleAndDescription = getTitleAndDescriptionPanel(
                String.format("%s (%d)", production.getTitle(), releaseYear),
                production.getPlot()
        );

        currentPanel = addToNorth(currentPanel, titleAndDescription);

        if (production instanceof Movie movie) {
            JLabel durationLabel = new JLabel(String.format("Duration: %s", movie.getDuration()));
            durationLabel.setFont(new Font("ARIAL", Font.PLAIN, 14));
            currentPanel = addToNorth(currentPanel, durationLabel);
        }

        currentPanel = addToNorth(currentPanel, GUIRating.getWrapped(production.getRatings(), production.getAverageRating()));
        currentPanel = addToNorth(currentPanel, getDirectorsPanel(production));
        currentPanel = addToNorth(currentPanel, getGenresPanel(production));
        currentPanel = addToNorth(currentPanel, getActorsPanel(production, this));

        if (production instanceof Series series) {
            currentPanel = addToNorth(currentPanel, GUISeriesSeasons.getWrapped(series));
        }

        // Align the container to the left
        mainPage.setAlignmentX(Component.LEFT_ALIGNMENT);

        backButton.addActionListener(e -> viewFrontPage());

        mainPage.updateUI();
    }

    public void viewActor(Actor actor) {
        mainPage.removeAll();
        mainPage.setLayout(new BorderLayout());
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPage.add(new JScrollPane(mainPanel), BorderLayout.CENTER);
        JPanel currentPanel = mainPanel;

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton backButton = new JButton("Go back");
        JButton addToFavoritesButton = new JButton("Add to Favorites");

        buttonPanel.add(backButton);
        buttonPanel.add(addToFavoritesButton);

        currentPanel = addToNorth(currentPanel, buttonPanel);

        JPanel nameAndBiography = getTitleAndDescriptionPanel(
                actor.getName(),
                actor.getBiography()
        );

        currentPanel = addToNorth(currentPanel, nameAndBiography);
        currentPanel = addToNorth(currentPanel, getPerformancesPanel(actor));

        // Align the container to the left
        mainPage.setAlignmentX(Component.LEFT_ALIGNMENT);

        backButton.addActionListener(e -> viewFrontPage());

        mainPage.updateUI();
    }

    public static JPanel getDirectorsPanel(Production production) {
        JPanel container = new JPanel();
        container.setLayout(new BorderLayout());
        JLabel label = new JLabel("Producers");
        label.setFont(new Font("Arial", Font.BOLD, 16));

        List<String> directorsList = production.getDirectors();
        String[] directorsArr = new String[directorsList.size()];
        directorsList.toArray(directorsArr);
        JList<String> directorsJList = new JList<>(directorsArr);

        container.add(label, BorderLayout.NORTH);
        container.add(directorsJList, BorderLayout.CENTER);

        return container;
    }

    public static JPanel getGenresPanel(Production production) {
        JPanel container = new JPanel();
        container.setLayout(new BorderLayout());
        JLabel label = new JLabel("Genres");
        label.setFont(new Font("Arial", Font.BOLD, 16));

        List<Genre> genreList = production.getGenres();
        Genre[] genreArr = new Genre[genreList.size()];
        genreList.toArray(genreArr);
        JList<Genre> genreJList = new JList<>(genreArr);

        container.add(label, BorderLayout.NORTH);
        container.add(genreJList, BorderLayout.CENTER);

        return container;
    }

    public static JPanel getActorsPanel(Production production, GUIMainFrame mainFrame) {
        JPanel container = new JPanel();
        container.setLayout(new BorderLayout());
        JLabel label = new JLabel("Actors");
        label.setFont(new Font("Arial", Font.BOLD, 16));

        List<Actor> actorList = production.getActors();
        Actor[] actorArr = new Actor[actorList.size()];
        actorList.toArray(actorArr);
        JList<Actor> actorJList = new JList<>(actorArr);

        // Create a custom cell renderer to only display actor names
        actorJList.setCellRenderer(new ActorListCellRenderer());

        actorJList.addListSelectionListener(e -> {
            if (!actorJList.isSelectionEmpty()) {
                Actor actor = actorJList.getSelectedValue();
                mainFrame.viewActor(actor);
            }
        });

        container.add(label, BorderLayout.NORTH);
        container.add(actorJList, BorderLayout.CENTER);

        return container;
    }

    public static JPanel getTitleAndDescriptionPanel(String title, String description) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));

        JTextArea descriptionTextArea = new JTextArea(description);
        descriptionTextArea.setFont(new Font("Arial", Font.PLAIN, 14));
        descriptionTextArea.setLineWrap(true);
        descriptionTextArea.setWrapStyleWord(true);
        descriptionTextArea.setOpaque(false);
        descriptionTextArea.setEditable(false);

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(descriptionTextArea, BorderLayout.CENTER);

        return panel;
    }

    public static JPanel getPerformancesPanel(Actor actor) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("Performances");
        label.setFont(new Font("Arial", Font.BOLD, 16));

        List<Performance> performances = actor.getPerformances();
        Performance[] performancesArr = new Performance[performances.size()];
        performances.toArray(performancesArr);
        JList<Performance> performanceJList = new JList<>(performancesArr);

        panel.add(label, BorderLayout.NORTH);
        panel.add(performanceJList, BorderLayout.CENTER);

        return panel;
    }

    public static void refreshHeader(JPanel header) {
        User<?> user = IMDB.getInstance().getCurrentUser();
        header.removeAll();
        header.add(new Label(String.format("IMDB | Welcome, %s | Experience: %s",
                user.getUsername(),
                user.getAccountType() == AccountType.ADMIN ? "-" : Integer.toString(user.getExperience())
        )));
        header.updateUI();
    }

    public List<Genre> getFilteredGenres() {
        return new ArrayList<>(this.filteredGenres);
    }

    public int getMinimumReviewCount() {
        return this.minimumReviewCount;
    }
}

class ActorListCellRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value instanceof Actor actor) {
            label.setText(actor.getName());
        }

        return label;
    }
}