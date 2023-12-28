import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GUINotificationListPopup extends JFrame {
    public GUINotificationListPopup() {
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());

        JLabel notificationsLabel = new JLabel("Your notifications:");
        List<String> notifications = IMDB.getInstance().getCurrentUser().getNotificationList();
        String[] notificationsArr = new String[notifications.size()];
        notifications.toArray(notificationsArr);
        JList<String> notificationsJList = new JList<>(notificationsArr);

        JScrollPane scrollPane = new JScrollPane(notificationsJList);

        add(notificationsLabel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        setLocationRelativeTo(null);
        pack();
        setVisible(true);
    }
}
