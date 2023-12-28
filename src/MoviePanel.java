import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class MoviePanel extends JPanel {

    public MoviePanel() {
        setLayout(new GridLayout(0, 2, 10, 10)); // 2 columns, variable rows, with gaps

        // JButton "Go back"
        JButton goBackButton = new JButton("Go back");
        add(goBackButton);

        // Title
        JLabel titleLabel = new JLabel("Title:");
        add(titleLabel);

        JLabel titleValueLabel = new JLabel("Movie Title");
        add(titleValueLabel);

        // Description
        JLabel descriptionLabel = new JLabel("Description:");
        add(descriptionLabel);

        JLabel descriptionValueLabel = new JLabel("Movie Description");
        add(descriptionValueLabel);

        // Directors
        JLabel directorsLabel = new JLabel("Directors:");
        add(directorsLabel);

        List<String> directors = Arrays.asList("Director 1", "Director 2", "Director 3");
        JList<String> directorsList = new JList<>(directors.toArray(new String[0]));
        JScrollPane directorsScrollPane = new JScrollPane(directorsList);
        add(directorsScrollPane);

        // Actors
        JLabel actorsLabel = new JLabel("Actors:");
        add(actorsLabel);

        List<String> actors = Arrays.asList("Actor 1", "Actor 2", "Actor 3");
        JList<String> actorsList = new JList<>(actors.toArray(new String[0]));
        JScrollPane actorsScrollPane = new JScrollPane(actorsList);
        add(actorsScrollPane);

        // Genres
        JLabel genresLabel = new JLabel("Genres:");
        add(genresLabel);

        List<String> genres = Arrays.asList("Genre 1", "Genre 2", "Genre 3");
        JList<String> genresList = new JList<>(genres.toArray(new String[0]));
        JScrollPane genresScrollPane = new JScrollPane(genresList);
        add(genresScrollPane);

        // Ratings
        JLabel ratingsLabel = new JLabel("Ratings:");
        add(ratingsLabel);

        List<String> ratings = Arrays.asList("Rating 1", "Rating 2", "Rating 3");
        JList<String> ratingsList = new JList<>(ratings.toArray(new String[0]));
        JScrollPane ratingsScrollPane = new JScrollPane(ratingsList);
        add(ratingsScrollPane);

        // Average Rating
        JLabel averageRatingLabel = new JLabel("Average Rating:");
        add(averageRatingLabel);

        JLabel averageRatingValueLabel = new JLabel("4.5"); // Example value, replace with actual average rating
        add(averageRatingValueLabel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Movie Details");
            frame.setSize(400, 500);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            MoviePanel moviePanel = new MoviePanel();
            frame.getContentPane().add(moviePanel);

            frame.setVisible(true);
        });
    }
}
