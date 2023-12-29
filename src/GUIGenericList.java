import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class GUIGenericList<T> extends GUIItemGeneric<List<T>> {
    /*
    Vertical Grid Separation
    Left: JList
    Right: Buttons: Add, Remove
     */

    private DefaultListModel<T> listModel = new DefaultListModel<>();
    private final JList<T> jList = new JList<>(listModel);
    private final JButton addButton = new JButton("Add");
    private final JButton removeButton = new JButton("Remove");
    private final JButton editButton = new JButton("Edit");
    private final JButton moveUpButton = new JButton("Move up");
    private final JButton moveDownButton = new JButton("Move down");
    private final JPanel buttonPanel = new JPanel();

    public GUIGenericList(Class<T> tClass, List<T> list, DefaultListCellRenderer cellRenderer, Class<? extends GUIItemGeneric<T>> itemPopupClass) {
        super(list);

        for (T element : list) {
            listModel.addElement(element);
        }

        initializeUI(tClass, cellRenderer, itemPopupClass);
    }

    private void addElementToList(T element) {
        getItem().add(element);
        listModel.addElement(element);
    }

    private void addElementToList(int index, T element) {
        getItem().add(index, element);
        listModel.add(index, element);
    }

    private void removeElementFromList(T element) {
        getItem().remove(element);
        listModel.removeElement(element);
    }

    private void initializeUI(Class<T> tClass, DefaultListCellRenderer cellRenderer, Class<? extends GUIItemGeneric<T>> itemPopupClass) {
        setLayout(new BorderLayout());

        add(getMainPanel(), BorderLayout.CENTER);

        setLocationRelativeTo(null);
        pack();
        setVisible(true);

        jList.setCellRenderer(cellRenderer);
        addButton.addActionListener(e -> {
            GUIItemGeneric<T> itemPopup;

            try {
                itemPopup = itemPopupClass.getDeclaredConstructor(tClass).newInstance((Object) null);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException ex) {
                // This shouldn't happen
                throw new RuntimeException(ex);
            }

            itemPopup.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    T element = itemPopup.getItem();
                    if (element != null) {
                        addElementToList(element);
                    }
                }
            });
        });

        removeButton.addActionListener(e -> {
            if (!jList.isSelectionEmpty()) {
                T selection = jList.getSelectedValue();
                removeElementFromList(selection);
            }
        });

        editButton.addActionListener(e -> {
            if (jList.isSelectionEmpty()) {
                return;
            }

            T selection = jList.getSelectedValue();

            GUIItemGeneric<T> itemPopup;

            try {
                itemPopup = itemPopupClass.getDeclaredConstructor(tClass).newInstance(selection);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException ex) {
                // This shouldn't happen
                throw new RuntimeException(ex);
            }

            itemPopup.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    int indOf = getItem().indexOf(selection);
                    if (indOf != -1) {
                        removeElementFromList(selection);
                        addElementToList(indOf, itemPopup.getItem());
                        jList.updateUI();
                        jList.setSelectedIndex(indOf);
                    }
                }
            });

        });

        moveUpButton.addActionListener(e -> {
            if (jList.isSelectionEmpty()) {
                return;
            }

            T selection = jList.getSelectedValue();
            int indOf = getItem().indexOf(selection);
            if (indOf > 0) {
                removeElementFromList(selection);
                addElementToList(indOf - 1, selection);
                jList.updateUI();
                jList.setSelectedIndex(indOf - 1);
            }
        });

        moveDownButton.addActionListener(e -> {
            if (jList.isSelectionEmpty()) {
                return;
            }

            T selection = jList.getSelectedValue();
            int indOf = getItem().indexOf(selection);
            if (indOf < getItem().size() - 1) {
                removeElementFromList(selection);
                addElementToList(indOf + 1, selection);
                jList.updateUI();
                jList.setSelectedIndex(indOf + 1);
            }
        });

        pack();
    }

    private JPanel getMainPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel listPanel = new JPanel(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(jList);
        listPanel.add(scrollPane, BorderLayout.CENTER);

        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(editButton);
        buttonPanel.add(moveUpButton);
        buttonPanel.add(moveDownButton);

        panel.add(listPanel);
        panel.add(buttonPanel);

        return panel;
    }
}
