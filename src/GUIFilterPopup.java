import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GUIFilterPopup extends JFrame {
    private JPanel contentPane;
    private DefaultListModel<Genre> listModel;
    private JList<Genre> list1;
    private JButton setMinimumReviewCountButton;
    private JTextField a0TextField;
    private JButton addGenreButton;
    private JComboBox<Genre> comboBox1;
    private final List<Genre> filteredList = new ArrayList<>();
    private int minimumReviewCount = 0;

    public GUIFilterPopup(GUIMainFrame mainFrame) {
        initializeUI(mainFrame);
    }

    private void updateListModel(DefaultListModel<Genre> listModel, List<Genre> list) {
        listModel.removeAllElements();
        for (Genre genre : list) {
            listModel.addElement(genre);
        }
    }

    private JComboBox<Genre> getComboBox() {
        return new JComboBox<>(Genre.values());
    }

    private void initializeUI(GUIMainFrame mainFrame) {
        contentPane = new JPanel();
        contentPane.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 10, 10, 10);

        // First row
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1;  // Adjust weight for the first row
        JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayout(1, 1));
        contentPane.add(panel1, gbc);

        JScrollPane scrollPane1 = new JScrollPane();
        panel1.add(scrollPane1);

        listModel = new DefaultListModel<>();
        list1 = new JList<>(listModel);
        list1.addListSelectionListener(e -> {
            if (!list1.isSelectionEmpty()) {
                Genre selectedGenre = list1.getSelectedValue();
                if (filteredList.contains(selectedGenre)) {
                    filteredList.remove(selectedGenre);
                    updateListModel(listModel, filteredList);
                    mainFrame.updateFilters(new ArrayList<>(filteredList), minimumReviewCount);
                }
            }
        });

        scrollPane1.setViewportView(list1);

        // Second row
        gbc.gridy = 1;
        gbc.weighty = 0;  // Adjust weight for the second row
        JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayout(1, 2));
        contentPane.add(panel2, gbc);

        setMinimumReviewCountButton = new JButton("Set minimum review count");
        setMinimumReviewCountButton.addActionListener(e -> {
            try {
                minimumReviewCount = Integer.parseInt(a0TextField.getText());
                mainFrame.updateFilters(new ArrayList<>(filteredList), minimumReviewCount);
            } catch (NumberFormatException ignored) {}
        });

        a0TextField = new JTextField("0");
        panel2.add(a0TextField);
        panel2.add(setMinimumReviewCountButton);

        // Third row
        gbc.gridy = 2;
        gbc.weighty = 0;  // Adjust weight for the third row
        JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayout(1, 2));
        contentPane.add(panel3, gbc);

        addGenreButton = new JButton("Add Genre");
        addGenreButton.addActionListener(e -> {
            Genre genre = (Genre) comboBox1.getSelectedItem();
            if (!filteredList.contains(genre)) {
                filteredList.add(genre);
                updateListModel(listModel, filteredList);
                mainFrame.updateFilters(new ArrayList<>(filteredList), minimumReviewCount);
            }
        });

        comboBox1 = getComboBox();
        panel3.add(comboBox1);
        panel3.add(addGenreButton);

        setContentPane(contentPane);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

    }
}