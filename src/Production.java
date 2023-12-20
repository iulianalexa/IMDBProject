import java.util.List;

enum Genre {
    ACTION, ADVENTURE, COMEDY, DRAMA, HORROR, SF, FANTASY, ROMANCE, MYSTERY, THRILLER, CRIME, BIOGRAPHY, WAR
}
abstract public class Production implements Comparable<Production> {
    String title, description;
    List<String> producers, actors;
    List<Genre> genres;
    List<Rating> ratings;
    Double score;

    public abstract void displayInfo();

    @Override
    public int compareTo(Production production) {
        return this.title.compareTo(production.title);
    }

    public void updateScore() {
        if (this.ratings.isEmpty()) {
            this.score = null;
        } else {
            Integer total = 0;
            for (Rating rating : this.ratings) {
                total += rating.score;
            }
            this.score = (double) total / this.ratings.size();
        }
    }
}
