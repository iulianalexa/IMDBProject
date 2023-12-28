import javax.swing.*;
import java.awt.*;

public class CustomPanel {

    public static JPanel createCustomPanel() {
        JPanel customPanel = new JPanel();
        customPanel.setLayout(new BoxLayout(customPanel, BoxLayout.Y_AXIS));

        // Row 1: Buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(new JButton("Button 1"));
        buttonPanel.add(new JButton("Button 2"));
        buttonPanel.add(new JButton("Button 3"));
        customPanel.add(buttonPanel);

        // Row 2: Large Font Label
        JLabel largeFontLabel = new JLabel("Large Font Label");
        largeFontLabel.setFont(new Font("Arial", Font.BOLD, 20)); // Customize font as needed

        JPanel labelPanel1 = new JPanel();
        labelPanel1.setLayout(new FlowLayout(FlowLayout.LEFT));
        labelPanel1.add(largeFontLabel);
        customPanel.add(labelPanel1);

        // Row 3: Smaller Font Label
        JLabel smallerFontLabel = new JLabel("Smaller Font Label");
        smallerFontLabel.setFont(new Font("Arial", Font.PLAIN, 14)); // Customize font as needed

        JPanel labelPanel2 = new JPanel();
        labelPanel2.setLayout(new FlowLayout(FlowLayout.LEFT));
        labelPanel2.add(smallerFontLabel);
        customPanel.add(labelPanel2);

        // Align the customPanel to the left
        customPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        return customPanel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Custom Panel Example");
            frame.setSize(300, 200);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
            mainPanel.add(createCustomPanel());

            frame.getContentPane().add(mainPanel);
            frame.setVisible(true);
        });
    }
}
