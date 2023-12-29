import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class GUIGenericList<T> extends JFrame {
    /*
    Vertical Grid Separation
    Left: JList
    Right: Buttons: Add, Remove
     */

    private final List<T> list;
    private DefaultListModel<T> listModel = new DefaultListModel<>();
    private final JList<T> jList = new JList<>(listModel);
    private final JButton addButton = new JButton("Add");
    private final JButton removeButton = new JButton("Remove");
    private final JPanel buttonPanel = new JPanel();

    public GUIGenericList(List<T> list, DefaultListCellRenderer cellRenderer, Class<? extends GUIItemGeneric<T>> itemPopupClass) {
        this.list = list;

        for (T element : list) {
            listModel.addElement(element);
        }

        initializeUI(cellRenderer, itemPopupClass);
    }

    private void addElementToList(T element) {
        list.add(element);
        listModel.addElement(element);
    }

    private void removeElementFromList(T element) {
        list.remove(element);
        listModel.removeElement(element);
    }

    private void initializeUI(DefaultListCellRenderer cellRenderer, Class<? extends GUIItemGeneric<T>> itemPopupClass) {
        setLayout(new BorderLayout());

        add(getMainPanel(), BorderLayout.CENTER);

        setLocationRelativeTo(null);
        pack();
        setVisible(true);

        jList.setCellRenderer(cellRenderer);
        addButton.addActionListener(e -> {
            GUIItemGeneric<T> itemPopup;

            try {
                itemPopup = itemPopupClass.getDeclaredConstructor().newInstance();
            } catch (InstantiationException | NoSuchMethodException | InvocationTargetException |
                     IllegalAccessException ex) {
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

        panel.add(listPanel);
        panel.add(buttonPanel);

        return panel;
    }

    public List<T> getList() {
        return new ArrayList<>(list);
    }
}
