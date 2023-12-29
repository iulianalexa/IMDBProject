import javax.swing.*;

public class GUIAuthFrame extends JFrame {
    private final JTextField usernameField;
    private final JPasswordField passwordField;

    public GUIAuthFrame() {
        super("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Components
        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");

        usernameField = new JTextField(25);
        passwordField = new JPasswordField(25);

        JButton loginButton = new JButton("Login");

        // Layout Manager
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        // Add components to frame
        add(usernameLabel);
        add(usernameField);
        add(passwordLabel);
        add(passwordField);
        add(loginButton);

        // Add action listener to the login button
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            User<?> user = IMDB.getInstance().getUser(username);
            if (user != null && user.checkPassword(password)) {
                IMDB.getInstance().setCurrentUser(user);
                dispose();
                new GUIMainFrame();
            } else {
                JOptionPane.showMessageDialog(this, "Login failed. Invalid credentials.");
            }
        });

        setVisible(true);
        pack();
    }
}
