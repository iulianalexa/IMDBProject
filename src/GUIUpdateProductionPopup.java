import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class GUIUpdateProductionPopup extends JFrame {
    DefaultListModel<Production> productionDefaultListModel = new DefaultListModel<>();
    JList<Production> productionJList = new JList<>(productionDefaultListModel);
    JButton updateButton = new JButton("Update");
    Staff<?> staff;

    public GUIUpdateProductionPopup() {
        if (IMDB.getInstance().getCurrentUser() instanceof Staff<?> staff1) {
            this.staff = staff1;
        } else {
            return;
        }

        List<Production> productionList = getProductionList();

        initializeUI(productionList);
    }

    private List<Production> getProductionList() {
        List<Production> productionList = new ArrayList<>();
        for (Production production : IMDB.getInstance().getProductionList()) {
            User<?> adder = IMDB.getInstance().getAdder(production);
            if ((adder == null && staff.getAccountType() == AccountType.ADMIN) || (adder != null && adder.getUsername().equals(staff.getUsername()))) {
                productionList.add(production);
            }
        }

        return productionList;
    }

    private void initializeUI(List<Production> productionList) {
        setLayout(new BorderLayout());

        add(getMainPanel(), BorderLayout.CENTER);
        productionJList.setCellRenderer(new ProductionCellRenderer());
        productionDefaultListModel.addAll(productionList);

        setLocationRelativeTo(null);
        pack();
        setVisible(true);

        updateButton.addActionListener(e -> {
            if (!productionJList.isSelectionEmpty()) {
                Production selectedProduction = productionJList.getSelectedValue();
                JFrame popup = new GUIProductionPopup(selectedProduction);
                popup.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        // Get current selection index
                        int selectionInd = productionJList.getSelectedIndex();

                        // Refresh production list
                        productionDefaultListModel.removeAllElements();
                        productionDefaultListModel.addAll(getProductionList());

                        // Reset selection index
                        if (selectionInd != -1) {
                            productionJList.setSelectedIndex(selectionInd);
                        }
                    }
                });
            }
        });
    }

    public JPanel getMainPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JScrollPane scrollPane = new JScrollPane(productionJList);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.add(updateButton);

        panel.add(scrollPane);
        panel.add(buttonPanel);

        return panel;
    }
}
