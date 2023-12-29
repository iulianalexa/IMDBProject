import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
        JButton menuButton = new JButton("Menu");

        buttonToolbarPanel.add(filterButton);
        buttonToolbarPanel.add(searchButton);
        buttonToolbarPanel.add(actorsButton);
        buttonToolbarPanel.add(menuButton);
        mainPage.add(buttonToolbarPanel, BorderLayout.NORTH);
        mainPage.add(scrollPane, BorderLayout.CENTER);
        mainPage.updateUI();

        filterButton.addActionListener(e -> new GUIFilterPopup(this));

        searchButton.addActionListener(e -> new GUISearchPopup(this));

        actorsButton.addActionListener(e -> viewActorsPage());

        menuButton.addActionListener(e -> viewMenuPage());
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

    private enum MenuItemType {
        VIEW_NOTIFICATIONS,
        VIEW_REQUESTS,
        VIEW_STAFF_REQUESTS
    }

    private static class MenuItem {
        String description;
        MenuItemType type;

        public MenuItem(String description, MenuItemType type) {
            this.description = description;
            this.type = type;
        }

        @Override
        public String toString() {
            return this.description;
        }
    }

    public void viewMenuPage() {
        JPanel mainPage = this.mainPage;
        List<Genre> filteredGenres = this.filteredGenres;
        int minimumReviewCount = this.minimumReviewCount;
        User<?> user = IMDB.getInstance().getCurrentUser();

        mainPage.removeAll();
        mainPage.setLayout(new BorderLayout());

        DefaultListModel<MenuItem> listModel = new DefaultListModel<>();
        JList<MenuItem> jList = new JList<>(listModel);

        // View Notifications
        listModel.addElement(new MenuItem("View Notifications", MenuItemType.VIEW_NOTIFICATIONS));

        // View Requests
        if (user.getAccountType() == AccountType.REGULAR || user.getAccountType() == AccountType.CONTRIBUTOR) {
            listModel.addElement(new MenuItem("View Requests", MenuItemType.VIEW_REQUESTS));
        }

        // View Staff Requests
        if (user.getAccountType() == AccountType.CONTRIBUTOR || user.getAccountType() == AccountType.ADMIN) {
            listModel.addElement(new MenuItem("View Staff Requests", MenuItemType.VIEW_STAFF_REQUESTS));
        }

        // Create a scroll pane for the JList
        JScrollPane scrollPane = new JScrollPane(jList);

        // Set up selection mode to allow single selection
        jList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Add a list selection listener to respond to item clicks
        jList.addListSelectionListener(e -> {
            if (!jList.isSelectionEmpty() && !e.getValueIsAdjusting()) {
                // Get the selected value
                MenuItem selectedValue = jList.getSelectedValue();
                switch (selectedValue.type) {
                    case VIEW_NOTIFICATIONS:
                        new GUINotificationListPopup();
                        break;
                    case VIEW_REQUESTS:
                        new GUIAuthorRequestListPopup();
                        break;
                    case VIEW_STAFF_REQUESTS:
                        new GUIAssignedRequestListPopup();
                        break;
                }

                // Deselect
                jList.clearSelection();
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

    private <T extends Comparable<Object>> JButton getFavoritesButton(T content, Class<T> tClass) {
        JButton addButton = new JButton("Add to Favorites");
        JButton removeButton = new JButton("Remove from Favorites");

        @SuppressWarnings("unchecked")
        User<T> user = (User<T>) IMDB.getInstance().getCurrentUser();

        addButton.addActionListener(e -> {
            user.addToFavourites(content);
            if (Production.class.isAssignableFrom(tClass)) {
                viewProduction((Production) content);
            } else if (Actor.class.isAssignableFrom(tClass)) {
                viewActor((Actor) content);
            }
        });

        removeButton.addActionListener(e -> {
            user.removeFromFavourites(content);
            if (Production.class.isAssignableFrom(tClass)) {
                viewProduction((Production) content);
            } else if (Actor.class.isAssignableFrom(tClass)) {
                viewActor((Actor) content);
            }
        });

        if (user.getFavorites().contains(content)) {
            // In favorites
            return removeButton;
        }

        // Not in favorites
        return addButton;
    }

    private class CustomRateWindowAdapter extends WindowAdapter {
        Production production;
        public CustomRateWindowAdapter(Production production) {
            super();
            this.production = production;
        }

        @Override
        public void windowClosed(WindowEvent e) {
            viewProduction(production);
        }
    }

    private JButton getRateButton(Production production) {
        JButton addRatingButton = new JButton("Add review");
        JButton seeRatingButton = new JButton("See review");

        if (IMDB.getInstance().getCurrentUser().getAccountType() != AccountType.REGULAR) {
            addRatingButton.setEnabled(false);
        }

        addRatingButton.addActionListener(e -> {
            JFrame popup = new GUIRatingPopup(production, null);
            popup.addWindowListener(new CustomRateWindowAdapter(production));
        });

        Rating userRating = null;
        for (Rating rating : production.getRatings()) {
            if (rating.getUsername().equals(IMDB.getInstance().getCurrentUser().getUsername())) {
                userRating = rating;
                break;
            }
        }

        if (userRating != null) {
            Rating finalUserRating = userRating;
            seeRatingButton.addActionListener(e -> {
                JFrame popup = new GUIRatingPopup(production, finalUserRating);
                popup.addWindowListener(new CustomRateWindowAdapter(production));
            });

            return seeRatingButton;
        }

        return addRatingButton;
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
        JButton favoritesButton = getFavoritesButton(production, Production.class);
        JButton addReviewButton = getRateButton(production);

        buttonPanel.add(backButton);
        buttonPanel.add(favoritesButton);
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

        currentPanel = addToNorth(currentPanel, GUIRatingDisplay.getWrapped(production.getRatings(), production.getAverageRating()));
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
        JButton favoritesButton = getFavoritesButton(actor, Actor.class);

        buttonPanel.add(backButton);
        buttonPanel.add(favoritesButton);

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