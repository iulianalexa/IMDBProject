import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.TreeSet;

public class GUISeriesSeasons {
    private static JPanel get(String seasonName, List<Episode> episodes) {
        JPanel panel = new JPanel(new BorderLayout());

        JLabel seasonNameLabel = new JLabel(seasonName);
        Episode[] episodesArr = new Episode[episodes.size()];
        episodes.toArray(episodesArr);
        JList<Episode> episodesJList = new JList<>(episodesArr);

        panel.add(seasonNameLabel, BorderLayout.NORTH);
        panel.add(episodesJList, BorderLayout.CENTER);

        return panel;
    }

    private static JPanel get(Series series) {
        BorderLayout mainBorderLayout = new BorderLayout();
        mainBorderLayout.setVgap(7);
        JPanel mainPanel = new JPanel(mainBorderLayout);
        JPanel panel = mainPanel;

        TreeSet<String> seasonNames = new TreeSet<>(series.getSeasons().keySet());
        for (String seasonName : seasonNames) {
            List<Episode> episodes = series.getSeasons().get(seasonName);

            panel.add(get(seasonName, episodes), BorderLayout.NORTH);
            BorderLayout borderLayout = new BorderLayout();
            borderLayout.setVgap(7);
            JPanel newPanel = new JPanel(borderLayout);
            panel.add(newPanel, BorderLayout.CENTER);
            panel = newPanel;
        }

        return mainPanel;
    }

    public static JPanel getWrapped(Series series) {
        BorderLayout borderLayout = new BorderLayout();
        borderLayout.setVgap(5);
        JPanel wrapPanel = new JPanel(borderLayout);
        JLabel episodesLabel = new JLabel("Episodes");
        episodesLabel.setFont(new Font("Arial", Font.BOLD, 17));

        wrapPanel.add(episodesLabel, BorderLayout.NORTH);
        wrapPanel.add(get(series), BorderLayout.CENTER);
        return wrapPanel;
    }
}
