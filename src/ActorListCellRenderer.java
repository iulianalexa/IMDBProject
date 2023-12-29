import javax.swing.*;
import java.awt.*;

public class ActorListCellRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value instanceof Actor actor) {
            label.setText(actor.getName());
        }

        return label;
    }
}
