import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GUIRating {
    private static JPanel get(Rating rating) {
        BorderLayout borderLayout = new BorderLayout();
        borderLayout.setVgap(2);
        JPanel panel = new JPanel(borderLayout);
        JLabel nameAndRatingLabel = new JLabel(String.format("%s (%d)", rating.getUsername(), rating.getRating()));
        nameAndRatingLabel.setFont(new Font("Arial", Font.BOLD, 16));
        JLabel commentLabel = new JLabel(rating.getComment());
        commentLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(nameAndRatingLabel, BorderLayout.NORTH);
        panel.add(commentLabel, BorderLayout.CENTER);

        return panel;
    }

    private static JPanel get(List<Rating> ratings) {
        BorderLayout mainBorderLayout = new BorderLayout();
        mainBorderLayout.setVgap(7);
        JPanel mainPanel = new JPanel(mainBorderLayout);
        JPanel panel = mainPanel;
        for (Rating rating : ratings) {
            panel.add(get(rating), BorderLayout.NORTH);
            BorderLayout borderLayout = new BorderLayout();
            borderLayout.setVgap(7);
            JPanel newPanel = new JPanel(borderLayout);
            panel.add(newPanel, BorderLayout.CENTER);
            panel = newPanel;
        }

        return mainPanel;
    }

    public static JPanel getWrapped(List<Rating> ratings, Double averageRating) {
        BorderLayout borderLayout = new BorderLayout();
        borderLayout.setVgap(5);
        JPanel wrapPanel = new JPanel(borderLayout);
        JLabel ratingsLabel = new JLabel(String.format("Ratings (%.02f)", averageRating));
        ratingsLabel.setFont(new Font("Arial", Font.BOLD, 17));

        wrapPanel.add(ratingsLabel, BorderLayout.NORTH);
        wrapPanel.add(get(ratings), BorderLayout.CENTER);
        return wrapPanel;
    }
}
