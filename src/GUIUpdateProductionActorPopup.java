import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class GUIUpdateProductionActorPopup<T> extends JFrame {
    DefaultListModel<T> itemDefaultListModel = new DefaultListModel<>();
    JList<T> itemJList = new JList<>(itemDefaultListModel);
    JButton updateButton = new JButton("Update");
    Staff<?> staff;
    Class<T> tClass;

    private static List<Production> getProductionList(Staff<?> staff) {
        List<Production> productionList = new ArrayList<>();
        for (Production production : IMDB.getInstance().getProductionList()) {
            User<?> adder = IMDB.getInstance().getAdder(production);
            if ((adder == null && staff.getAccountType() == AccountType.ADMIN) || (adder != null && adder.getUsername().equals(staff.getUsername()))) {
                productionList.add(production);
            }
        }

        return productionList;
    }

    private List<Actor> getActorList(Staff<?> staff) {
        List<Actor> actorList = new ArrayList<>();
        for (Actor actor : IMDB.getInstance().getActors()) {
            User<?> adder = IMDB.getInstance().getAdder(actor);
            if ((adder == null && staff.getAccountType() == AccountType.ADMIN) || (adder != null && adder.getUsername().equals(staff.getUsername()))) {
                actorList.add(actor);
            }
        }

        return actorList;
    }

    public List<T> getItemList() {
        List<T> itemList = new ArrayList<>();

        if (tClass.isAssignableFrom(Production.class)) {
            @SuppressWarnings("unchecked")
            List<T> prodList = (List<T>) getProductionList(staff);
            itemJList.setCellRenderer(new ProductionListCellRenderer());
            itemList.addAll(prodList);
            return itemList;
        } else if (tClass.isAssignableFrom(Actor.class)) {
            @SuppressWarnings("unchecked")
            List<T> actorList = (List<T>) getActorList(staff);
            itemJList.setCellRenderer(new ActorListCellRenderer());
            itemList.addAll(actorList);
            return itemList;
        }

        return null;
    }

    public GUIUpdateProductionActorPopup(Class<T> tClass) {
        if (IMDB.getInstance().getCurrentUser() instanceof Staff<?> staff1) {
            this.staff = staff1;
        } else {
            return;
        }

        this.tClass = tClass;
        List<T> itemList = getItemList();
        if (itemList == null) {
            return;
        }

        initializeUI(itemList);
    }

    private void initializeUI(List<T> itemList) {
        setLayout(new BorderLayout());

        add(getMainPanel(), BorderLayout.CENTER);
        itemDefaultListModel.addAll(itemList);

        setLocationRelativeTo(null);
        pack();
        setVisible(true);

        updateButton.addActionListener(e -> {
            if (!itemJList.isSelectionEmpty()) {
                T selectedItem = itemJList.getSelectedValue();
                JFrame popup;

                if (tClass.isAssignableFrom(Production.class)) {
                    popup = new GUIProductionPopup((Production) selectedItem);
                } else {
                    popup = new GUIActorPopup((Actor) selectedItem);
                }

                popup.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        // Get current selection index
                        int selectionInd = itemJList.getSelectedIndex();

                        // Refresh item list
                        itemDefaultListModel.removeAllElements();
                        itemDefaultListModel.addAll(getItemList());

                        // Reset selection index
                        if (selectionInd != -1) {
                            itemJList.setSelectedIndex(selectionInd);
                        }
                    }
                });
            }
        });
    }

    public JPanel getMainPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JScrollPane scrollPane = new JScrollPane(itemJList);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.add(updateButton);

        panel.add(scrollPane);
        panel.add(buttonPanel);

        return panel;
    }
}
