import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Rating implements Subject {
    private String username, comment;
    private Integer rating;

    private final Map<String, ArrayList<Observer>> observers = new HashMap<>();

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
    public void subscribe(String observerType, Observer observer) {
        if (!observers.containsKey(observerType)) {
            observers.put(observerType, new ArrayList<>());
        }

        ArrayList<Observer> subObservers = observers.get(observerType);
        subObservers.add(observer);
    }

    @Override
    public void unsubscribe(String observerType, Observer observer) {
        ArrayList<Observer> subObservers = observers.get(observerType);
        if (subObservers != null) {
            subObservers.remove(observer);
            if (subObservers.isEmpty()) {
                observers.remove(observerType);
            }
        }
    }

    @Override
    public void sendNotification(String notificationType, String message) {
        ArrayList<Observer> subObservers = observers.get(notificationType);
        if (subObservers != null) {
            for (Observer observer : subObservers) {
                observer.update(message);
            }
        }
    }
}
