import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GUIMainFrame extends JFrame {
    JPanel header = new JPanel();
    JPanel mainPage = new JPanel(new BorderLayout());

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

        viewFrontPage(mainPage);
        refreshHeader(header);
        setVisible(true);
    }

    public static void viewFrontPage(JPanel mainPage) {
        mainPage.removeAll();
        mainPage.setLayout(new BorderLayout());

        List<Production> productionsList = IMDB.getInstance().getProductionList();
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

                viewProduction(mainPage, selectedValue);
            }
        });

        mainPage.add(scrollPane, BorderLayout.CENTER);
        mainPage.updateUI();
    }

    public static void viewProduction(JPanel mainPage, Production production) {
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

        backButton.addActionListener(e -> viewFrontPage(mainPage));

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
}
