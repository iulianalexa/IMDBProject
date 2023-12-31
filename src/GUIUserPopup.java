import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class GUIUserPopup extends JFrame {
    JTextField nameField = new JTextField(25);
    JTextField emailField = new JTextField(25);
    JTextField countryField = new JTextField(25);
    JTextField genderField = new JTextField(25);
    JTextField birthdateField = new JTextField(25);
    JTextField ageField = new JTextField(25);
    JComboBox<AccountType> accountTypeJComboBox = new JComboBox<>(AccountType.values());
    JButton submitButton = new JButton("Submit");
    Admin<?> admin;

    public GUIUserPopup() {
        if (IMDB.getInstance().getCurrentUser() instanceof Admin<?> admin1) {
            this.admin = admin1;
        } else {
            return;
        }

        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());

        add(getMainPanel(), BorderLayout.CENTER);

        setLocationRelativeTo(null);
        pack();
        setVisible(true);

        submitButton.addActionListener(e -> {
            String name = nameField.getText().strip();
            String email = emailField.getText().strip();
            String country = countryField.getText().strip();
            String gender = genderField.getText().strip();
            String birthdate = birthdateField.getText().strip();
            AccountType accountType = (AccountType) accountTypeJComboBox.getSelectedItem();
            String ageText = ageField.getText().strip();
            int age = -1;
            if (!ageText.isEmpty()) {
                try {
                    age = Integer.parseInt(ageText);
                } catch (NumberFormatException ex) {
                    showErrorDialog("Age is not a number.");
                    return;
                }
            }

            String username = RandomPasswordGenerator.generateUsername(name);
            String password = RandomPasswordGenerator.generate(20);

            User.Information.InformationBuilder informationBuilder = new User.Information.InformationBuilder();
            User.Information.Credentials credentials = null;
            if (!name.isEmpty()) {
                informationBuilder = informationBuilder.name(name);
            }

            if (!email.isEmpty()) {
                credentials = new User.Information.Credentials(email, password);
            }

            if (!country.isEmpty()) {
                informationBuilder = informationBuilder.country(country);
            }

            if (!gender.isEmpty()) {
                informationBuilder = informationBuilder.gender(gender);
            }

            if (!birthdate.isEmpty()) {
                try {
                    informationBuilder = informationBuilder.birthDate(birthdate);
                } catch (InvalidInformationException ex) {
                    showErrorDialog("Invalid birth date! Format: YYYY-MM-DD");
                    return;
                }
            }

            if (age != -1) {
                informationBuilder = informationBuilder.age(age);
            }

            if (credentials != null) {
                informationBuilder = informationBuilder.credentials(credentials);
            }

            User.Information information;
            try {
                information = informationBuilder.build();
            } catch (InformationIncompleteException ex) {
                showErrorDialog("Incomplete information!");
                return;
            }

            User.UnknownUser unknownUser = new User.UnknownUser(username, information, accountType);
            User<?> user = UserFactory.factory(unknownUser);
            admin.addUser(user);
            dispose();

            StringSelection stringSelection = new StringSelection(password);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);

            showInfoDialog(String.format("Your new user has been created!\nUsername: %s\nThe password has been copied to your clipboard.",
                    username));
        });
    }

    private JPanel getMainPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JPanel namePanel = new JPanel(new GridLayout(1, 2));
        JLabel nameLabel = new JLabel("Name:");
        namePanel.add(nameLabel);
        namePanel.add(nameField);

        JPanel emailPanel = new JPanel(new GridLayout(1, 2));
        JLabel emailLabel = new JLabel("Email*:");
        emailPanel.add(emailLabel);
        emailPanel.add(emailField);

        JPanel countryPanel = new JPanel(new GridLayout(1, 2));
        JLabel countryLabel = new JLabel("Country:");
        countryPanel.add(countryLabel);
        countryPanel.add(countryField);

        JPanel genderPanel = new JPanel(new GridLayout(1, 2));
        JLabel genderLabel = new JLabel("Gender:");
        genderPanel.add(genderLabel);
        genderPanel.add(genderField);

        JPanel birthdatePanel = new JPanel(new GridLayout(1, 2));
        JLabel birthdateLabel = new JLabel("Birth date:");
        birthdatePanel.add(birthdateLabel);
        birthdatePanel.add(birthdateField);

        JPanel agePanel = new JPanel(new GridLayout(1, 2));
        JLabel ageLabel = new JLabel("Age:");
        agePanel.add(ageLabel);
        agePanel.add(ageField);

        JPanel accountTypePanel = new JPanel(new GridLayout(1, 2));
        JLabel accountTypeLabel = new JLabel("Account type:");
        accountTypePanel.add(accountTypeLabel);
        accountTypePanel.add(accountTypeJComboBox);

        JPanel submitPanel = new JPanel(new BorderLayout());
        submitPanel.add(submitButton);

        panel.add(namePanel);
        panel.add(emailPanel);
        panel.add(countryPanel);
        panel.add(genderPanel);
        panel.add(birthdatePanel);
        panel.add(agePanel);
        panel.add(accountTypePanel);
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

    private static void showInfoDialog(String message) {
        JOptionPane.showMessageDialog(
                null,
                message,
                "Info",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}
