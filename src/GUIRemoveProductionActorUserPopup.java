import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GUIRemoveProductionActorUserPopup<T> extends JFrame {
    DefaultListModel<T> itemDefaultListModel = new DefaultListModel<>();
    JList<T> itemJList = new JList<>(itemDefaultListModel);
    JButton removeButton = new JButton("Remove");
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

    public GUIRemoveProductionActorUserPopup(Class<T> tClass) {
        if (IMDB.getInstance().getCurrentUser() instanceof Staff<?> staff1) {
            this.staff = staff1;
        } else {
            return;
        }

        this.tClass = tClass;
        List<T> itemList;
        if (tClass.isAssignableFrom(Production.class)) {
            @SuppressWarnings("unchecked")
            List<T> prodList = (List<T>) getProductionList(staff);
            itemJList.setCellRenderer(new ProductionListCellRenderer());
            itemList = prodList;
        } else if (tClass.isAssignableFrom(Actor.class)) {
            @SuppressWarnings("unchecked")
            List<T> actorList = (List<T>) getActorList(staff);
            itemJList.setCellRenderer(new ActorListCellRenderer());
            itemList = actorList;
        } else if (tClass.isAssignableFrom(User.class)) {
            @SuppressWarnings("unchecked")
            List<T> userList = (List<T>) IMDB.getInstance().getUsers();
            itemJList.setCellRenderer(new UserListCellRenderer());
            itemList = userList;
        } else {
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

        removeButton.addActionListener(e -> {
            if (!itemJList.isSelectionEmpty()) {
                T selectedItem = itemJList.getSelectedValue();

                if (tClass.isAssignableFrom(Production.class)) {
                    Production selectedProduction = (Production) selectedItem;
                    staff.removeProductionSystem(selectedProduction);
                } else if (tClass.isAssignableFrom(Actor.class)) {
                    Actor selectedActor = (Actor) selectedItem;
                    staff.removeActorSystem(selectedActor);
                } else if (tClass.isAssignableFrom(User.class)) {
                    User<?> selectedUser = (User<?>) selectedItem;
                    if (staff instanceof Admin<?> admin) {
                        try {
                            admin.removeUser(selectedUser);
                        } catch (InvalidCommandException ex) {
                            showErrorDialog(ex.getMessage());
                            return;
                        }
                    }
                }

                itemDefaultListModel.removeElement(selectedItem);
            }
        });
    }

    public JPanel getMainPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JScrollPane scrollPane = new JScrollPane(itemJList);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.add(removeButton);

        panel.add(scrollPane);
        panel.add(buttonPanel);

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

class UserListCellRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value instanceof User<?> user) {
            label.setText(user.getUsername());
        }

        return label;
    }
}