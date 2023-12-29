import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.concurrent.Flow;

public class GUIRequestPopup extends JFrame {
    public GUIRequestPopup(Request request, GUIRequestViewer requestViewer) {
        initializeUI(request, requestViewer);
    }

    private void initializeUI(Request request, GUIRequestViewer requestViewer) {
        setLayout(new BorderLayout());

        JPanel buttonsToolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));

        add(getPanel(request, requestViewer), BorderLayout.CENTER);
        add(buttonsToolbar, BorderLayout.NORTH);

        if (request != null && requestViewer == GUIRequestViewer.AUTHOR) {
            JButton closeButton = new JButton("Close Request");
            buttonsToolbar.add(closeButton);
            closeButton.addActionListener(e -> {
                if (IMDB.getInstance().getCurrentUser() instanceof RequestsManager requestsManager) {
                    requestsManager.removeRequest(request);
                    dispose();
                }
            });
        } else if (request != null && requestViewer == GUIRequestViewer.ASSIGNED) {
            JButton acceptButton = new JButton("Accept Request");
            JButton closeButton = new JButton("Close Request");
            buttonsToolbar.add(acceptButton);
            buttonsToolbar.add(closeButton);
            acceptButton.addActionListener(e -> {
                if (IMDB.getInstance().getCurrentUser() instanceof Staff<?> staff) {
                    staff.solveRequest(request);
                    dispose();
                }
            });

            closeButton.addActionListener(e -> {
                if (IMDB.getInstance().getCurrentUser() instanceof Staff<?> staff) {
                    staff.closeRequest(request, false);
                    dispose();
                }
            });
        }

        setLocationRelativeTo(null);
        pack();
        setVisible(true);
    }

    private JPanel getPanel(Request request, GUIRequestViewer requestViewer) {
        // if request is null: assume writeable, otherwise read-only
        JPanel panel0 = new JPanel(new BorderLayout());
        JPanel panel1 = new JPanel(new BorderLayout());

        JPanel usernamesPanel = new JPanel(new GridLayout(2, 2));
        if (request != null) {
            JLabel authorUsernameLabel = new JLabel("Author:");
            JLabel assignedUsernameLabel = new JLabel("Assigned:");
            JTextField authorUsernameField = new JTextField();
            authorUsernameField.setEditable(false);
            authorUsernameField.setText(request.getAuthorUsername());
            JTextField assignedUsernameField = new JTextField();
            assignedUsernameField.setEditable(false);
            assignedUsernameField.setText(request.getAssignedUsername());

            usernamesPanel.add(authorUsernameLabel);
            usernamesPanel.add(authorUsernameField);
            usernamesPanel.add(assignedUsernameLabel);
            usernamesPanel.add(assignedUsernameField);
        }

        JComboBox<RequestType> comboBox = new JComboBox<>(RequestType.values());
        JPanel panel2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel aboutLabel = new JLabel("About:");
        JTextField textField = new JTextField(30);
        panel2.add(aboutLabel);
        panel2.add(textField);
        JTextArea commentTextArea = new JTextArea(10, 30);
        commentTextArea.setLineWrap(true);
        commentTextArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(commentTextArea);
        panel1.add(panel2, BorderLayout.NORTH);
        panel1.add(scrollPane, BorderLayout.CENTER);

        panel0.add(comboBox, BorderLayout.NORTH);
        panel0.add(panel1, BorderLayout.CENTER);

        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(e -> {
            Request newRequest;
            if (commentTextArea.getText().isEmpty()) {
                showErrorDialog("You did not write a description.");
                return;
            }

            switch ((RequestType) comboBox.getSelectedItem()) {
                case null:
                    return;
                case DELETE_ACCOUNT, OTHERS:
                    newRequest = new Request(
                            (RequestType) comboBox.getSelectedItem(),
                            LocalDateTime.now(),
                            commentTextArea.getText(),
                            IMDB.getInstance().getCurrentUser().getUsername(),
                            "ADMIN"
                    );
                    break;
                case ACTOR_ISSUE:
                    if (textField.getText().isEmpty()) {
                        showErrorDialog("You need to specify the actor name.");
                        return;
                    }

                    Actor actor = IMDB.getInstance().searchForActor(textField.getText());
                    if (actor == null) {
                        showErrorDialog("Could not find actor.");
                        return;
                    }

                    User<?> actorAdder = IMDB.getInstance().getAdder(actor);
                    User<?> user = IMDB.getInstance().getCurrentUser();

                    if (actorAdder != null && actorAdder.getUsername().equals(user.getUsername()) && user.getAccountType() == AccountType.CONTRIBUTOR) {
                        showErrorDialog("You cannot open a request on your own contribution!");
                        return;
                    }

                    newRequest = new Request(
                            RequestType.ACTOR_ISSUE,
                            LocalDateTime.now(),
                            commentTextArea.getText(),
                            user.getUsername(),
                            actorAdder == null ? "ADMIN" : actorAdder.getUsername()
                    );

                    newRequest.setTargetName(actor.getName());
                    break;
                case MOVIE_ISSUE:
                    if (textField.getText().isEmpty()) {
                        showErrorDialog("You need to specify the production title.");
                        return;
                    }
                    Production production = IMDB.getInstance().searchForProduction(textField.getText());
                    if (production == null) {
                        showErrorDialog("Could not find production.");
                        return;
                    }
                    User<?> productionAdder = IMDB.getInstance().getAdder(production);
                    newRequest = new Request(
                            RequestType.MOVIE_ISSUE,
                            LocalDateTime.now(),
                            commentTextArea.getText(),
                            IMDB.getInstance().getCurrentUser().getUsername(),
                            productionAdder == null ? "ADMIN" : productionAdder.getUsername()
                    );

                    newRequest.setTargetName(production.getTitle());
                    break;
            }

            if (IMDB.getInstance().getCurrentUser() instanceof RequestsManager requestsManager) {
                requestsManager.createRequest(newRequest);
                dispose();
            }
        });

        if (request == null) {
            panel0.add(sendButton, BorderLayout.SOUTH);
        }

        textField.setEditable(false);
        comboBox.addActionListener(e -> {
            RequestType requestType = (RequestType) comboBox.getSelectedItem();
            switch (requestType) {
                case ACTOR_ISSUE, MOVIE_ISSUE:
                    textField.setEditable(true);
                    break;
                case DELETE_ACCOUNT, OTHERS:
                    textField.setEditable(false);
                    textField.setText("");
                    break;
                case null:
                    break;
            }
        });

        if (request != null) {
            comboBox.setEnabled(false);
            textField.setEditable(false);
            commentTextArea.setEditable(false);

            comboBox.setSelectedItem(request.getType());
            textField.setText(request.getTargetName() == null ? "" : request.getTargetName());
            commentTextArea.setText(request.getDescription());
        }

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(panel0, BorderLayout.CENTER);
        if (request != null) {
            panel.add(usernamesPanel, BorderLayout.NORTH);
        }

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
