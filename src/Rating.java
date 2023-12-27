import java.util.ArrayList;

public class Rating implements Subject {
    private String username, comment;
    private Integer rating;

    private ArrayList<Observer> observers = new ArrayList<>();

    public Rating() {}

    public Rating(String username, String comment, Integer rating) {
        this.username = username;
        this.comment = comment;
        this.rating = rating;
    }

    public String getUsername() {
        return username;
    }

    public String getComment() {
        return comment;
    }

    public Integer getRating() {
        return rating;
    }

    @Override
    public String toString() {
        return String.format("%s (%d)\n%s", this.username, this.rating, this.comment);
    }

    @Override
    public void subscribe(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void unsubscribe(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void sendNotification(String notificationType, String message) {
        switch (notificationType) {
            case "regular":
                for (Observer observer : observers) {
                    if (observer instanceof Regular<?>) {
                        observer.update(message);
                    }
                }
                break;
            case "contributor":
                for (Observer observer : observers) {
                    if (observer instanceof Contributor<?>) {
                        observer.update(message);
                    }
                }
                break;
        }
    }
}
