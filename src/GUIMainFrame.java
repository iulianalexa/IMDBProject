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
        JButton backButton = new JButton("Go back");
        JLabel titleLabel = new JLabel(production.getTitle());

        backButton.addActionListener(e -> viewFrontPage(mainPage));

        mainPage.add(backButton, BorderLayout.NORTH);
        mainPage.add(titleLabel, BorderLayout.CENTER);
        mainPage.updateUI();
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
