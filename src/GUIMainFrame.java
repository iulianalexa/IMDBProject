import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

        buttonToolbarPanel.add(filterButton);
        buttonToolbarPanel.add(searchButton);
        mainPage.add(buttonToolbarPanel, BorderLayout.NORTH);
        mainPage.add(scrollPane, BorderLayout.CENTER);
        mainPage.updateUI();

        filterButton.addActionListener(e -> {
            new GUIFilterPopup(this);
        });

        searchButton.addActionListener(e -> {
            new GUISearchPopup(this);
        });
    }

    public void viewProduction(Production production) {
        JPanel mainPage = this.mainPage;

        mainPage.removeAll();
        mainPage.setLayout(new BorderLayout());

        // Row 1: Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton backButton = new JButton("Go back");
        JButton addToFavoritesButton = new JButton("Add to Favorites");
        JButton addReviewButton = new JButton("Add review");

        buttonPanel.add(backButton);
        buttonPanel.add(addToFavoritesButton);
        buttonPanel.add(addReviewButton);

        mainPage.add(buttonPanel, BorderLayout.NORTH);

        // Row 2: Title
        JPanel titleAndInfoPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel(production.getTitle());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleAndInfoPanel.add(titleLabel, BorderLayout.NORTH);

        // Row 3: Description
        JTextArea descriptionTextArea = new JTextArea(production.getPlot());
        descriptionTextArea.setFont(new Font("Arial", Font.PLAIN, 14));
        descriptionTextArea.setLineWrap(true);
        descriptionTextArea.setWrapStyleWord(true);
        descriptionTextArea.setOpaque(false);
        descriptionTextArea.setEditable(false);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.add(descriptionTextArea);
        infoPanel.add(getDirectorsPanel(production));
        infoPanel.add(getGenresPanel(production));

        titleAndInfoPanel.add(infoPanel, BorderLayout.CENTER);

        mainPage.add(titleAndInfoPanel, BorderLayout.CENTER);

        // Align the container to the left
        mainPage.setAlignmentX(Component.LEFT_ALIGNMENT);

        backButton.addActionListener(e -> viewFrontPage());

        mainPage.updateUI();
    }

    public void viewActor(Actor actor) {
        JPanel mainPage = this.mainPage;

        mainPage.removeAll();
        mainPage.setLayout(new BorderLayout());

        // Row 1: Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton backButton = new JButton("Go back");
        JButton addToFavoritesButton = new JButton("Add to Favorites");

        buttonPanel.add(backButton);
        buttonPanel.add(addToFavoritesButton);

        mainPage.add(buttonPanel, BorderLayout.NORTH);

        // Row 2: Name
        JPanel nameAndInfoPanel = new JPanel(new BorderLayout());
        JLabel nameLabel = new JLabel(actor.getName());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 20));
        nameAndInfoPanel.add(nameLabel, BorderLayout.NORTH);

        // Row 3: Biography
        JTextArea biographyTextArea = new JTextArea(actor.getBiography());
        biographyTextArea.setFont(new Font("Arial", Font.PLAIN, 14));
        biographyTextArea.setLineWrap(true);
        biographyTextArea.setWrapStyleWord(true);
        biographyTextArea.setOpaque(false);
        biographyTextArea.setEditable(false);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.add(biographyTextArea);

        nameAndInfoPanel.add(infoPanel, BorderLayout.CENTER);

        mainPage.add(nameAndInfoPanel, BorderLayout.CENTER);

        // Align the container to the left
        mainPage.setAlignmentX(Component.LEFT_ALIGNMENT);

        backButton.addActionListener(e -> viewFrontPage());

        mainPage.updateUI();
    }

    public static JPanel getDirectorsPanel(Production production) {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        JLabel label = new JLabel("Producers");

        List<String> directorsList = production.getDirectors();
        String[] directorsArr = new String[directorsList.size()];
        directorsList.toArray(directorsArr);
        JList<String> directorsJList = new JList<>(directorsArr);

        JScrollPane scrollPane = new JScrollPane(directorsJList);
        container.add(label);
        container.add(scrollPane);

        return container;
    }

    public static JPanel getGenresPanel(Production production) {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        JLabel label = new JLabel("Genres");

        List<Genre> genreList = production.getGenres();
        Genre[] genreArr = new Genre[genreList.size()];
        genreList.toArray(genreArr);
        JList<Genre> genreJList = new JList<>(genreArr);

        JScrollPane scrollPane = new JScrollPane(genreJList);
        container.add(label);
        container.add(scrollPane);

        return container;
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
