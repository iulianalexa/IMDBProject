import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GUIActorPopup extends JFrame {
    JTextField nameField = new JTextField(25);
    JTextArea biographyTextArea = new JTextArea(10, 25);
    JButton performancesButton = new JButton("Performances...");
    JButton submitButton = new JButton("Submit");
    final List<Performance> performanceList = new ArrayList<>();

    private Staff<?> staff;

    public GUIActorPopup(Actor actor) {
        if (IMDB.getInstance().getCurrentUser() instanceof Staff<?> staff1) {
            this.staff = staff1;
        } else {
            return;
        }

        initializeUI(actor);
    }

    private void initializeUI(Actor actor) {
        setLayout(new BorderLayout());

        add(getMainPanel(), BorderLayout.CENTER);

        setLocationRelativeTo(null);
        pack();
        setVisible(true);

        performancesButton.addActionListener(e -> new GUIGenericList<>(
                Performance.class,
                performanceList,
                new DefaultListCellRenderer(),
                GUIItemPerformance.class
        ));

        submitButton.addActionListener(e -> {
            String name = nameField.getText();
            String biography = biographyTextArea.getText();

            if (name.isEmpty()) {
                showErrorDialog("Actor name cannot be empty!");
                return;
            }

            if (biography.isEmpty()) {
                biography = null;
            }

            Actor newActor = new Actor(name, biography);
            newActor.setPerformances(performanceList);

            if (actor != null) {
                staff.removeActorSystem(actor);
            }

            staff.addActorSystem(newActor);
            dispose();
        });

        if (actor != null) {
            // Set relevant fields
            nameField.setText(actor.getName());
            biographyTextArea.setText(biographyTextArea.getText());
            performanceList.addAll(actor.getPerformances());
        }
    }

    private JPanel getMainPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JPanel namePanel = new JPanel(new GridLayout(1, 2));
        JLabel nameLabel = new JLabel("Name:");
        namePanel.add(nameLabel);
        namePanel.add(nameField);

        JPanel biographyPanel = new JPanel(new GridLayout(1, 2));
        JLabel biographyLabel = new JLabel("Biography:");
        biographyTextArea.setLineWrap(true);
        biographyTextArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(biographyTextArea);
        biographyPanel.add(biographyLabel);
        biographyPanel.add(scrollPane);

        JPanel performancesPanel = new JPanel(new BorderLayout());
        performancesPanel.add(performancesButton, BorderLayout.CENTER);

        JPanel submitPanel = new JPanel(new BorderLayout());
        submitPanel.add(submitButton, BorderLayout.CENTER);

        panel.add(namePanel);
        panel.add(biographyPanel);
        panel.add(performancesPanel);
        panel.add(submitPanel);

        return panel;
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
