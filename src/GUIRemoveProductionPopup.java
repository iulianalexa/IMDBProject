import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GUIRemoveProductionPopup extends JFrame {
    DefaultListModel<Production> productionDefaultListModel = new DefaultListModel<>();
    JList<Production> productionJList = new JList<>(productionDefaultListModel);
    JButton removeButton = new JButton("Remove");
    Staff<?> staff;

    public GUIRemoveProductionPopup() {
        if (IMDB.getInstance().getCurrentUser() instanceof Staff<?> staff1) {
            this.staff = staff1;
        } else {
            return;
        }

        List<Production> productionList = new ArrayList<>();
        for (Production production : IMDB.getInstance().getProductionList()) {
            User<?> adder = IMDB.getInstance().getAdder(production);
            if ((adder == null && staff.getAccountType() == AccountType.ADMIN) || (adder != null && adder.getUsername().equals(staff.getUsername()))) {
                productionList.add(production);
            }
        }

        initializeUI(productionList);
    }

    public void initializeUI(List<Production> productionList) {
        setLayout(new BorderLayout());

        add(getMainPanel(), BorderLayout.CENTER);
        productionJList.setCellRenderer(new ProductionCellRenderer());
        productionDefaultListModel.addAll(productionList);

        setLocationRelativeTo(null);
        pack();
        setVisible(true);

        removeButton.addActionListener(e -> {
            if (!productionJList.isSelectionEmpty()) {
                Production selectedProduction = productionJList.getSelectedValue();
                productionDefaultListModel.removeElement(selectedProduction);
                staff.removeProductionSystem(selectedProduction);
            }
        });
    }

    public JPanel getMainPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JScrollPane scrollPane = new JScrollPane(productionJList);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.add(removeButton);

        panel.add(scrollPane);
        panel.add(buttonPanel);

        return panel;
    }
}
