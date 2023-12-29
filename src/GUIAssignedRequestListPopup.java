import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class GUIAssignedRequestListPopup extends JFrame {
    Staff<?> staff;
    JPanel panel = new JPanel(new BorderLayout());

    class CustomWindowAdapter extends WindowAdapter {
        @Override
        public void windowClosed(WindowEvent e) {
            remakeUI(panel);
        }
    }

    public GUIAssignedRequestListPopup() {
        if (IMDB.getInstance().getCurrentUser() instanceof Staff<?> staff1) {
            this.staff = staff1;
        } else {
            return;
        }

        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());

        remakeUI(panel);

        add(panel, BorderLayout.CENTER);
        setLocationRelativeTo(null);
        pack();
        setVisible(true);
    }

    private void remakeUI(JPanel panel) {
        panel.removeAll();

        List<Request> requests = new ArrayList<>(staff.getRequestList());
        if (staff.getAccountType() == AccountType.ADMIN) {
            requests.addAll(RequestsHolder.getRequests());
        }

        Request[] requestsArr = new Request[requests.size()];
        requests.toArray(requestsArr);
        JList<Request> requestJList = new JList<>(requestsArr);

        requestJList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Request request) {
                    label.setText(String.format("%s: %s", request.getType(), request.getDescription().split("\n")[0]));
                }

                return label;
            }
        });

        JScrollPane scrollPane = new JScrollPane(requestJList);


        panel.add(scrollPane, BorderLayout.CENTER);

        requestJList.addListSelectionListener(e -> {
            if (!requestJList.isSelectionEmpty() && !e.getValueIsAdjusting()) {
                Request request = requestJList.getSelectedValue();
                JFrame popup = new GUIRequestPopup(request, GUIRequestViewer.ASSIGNED);
                popup.addWindowListener(new CustomWindowAdapter());
                requestJList.clearSelection();
            }
        });

        pack();
    }
}