import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Rating implements Subject, Comparable<Rating> {
    private String username, comment;
    private Integer rating;

    private final Map<String, ArrayList<String>> observerUsernames = new HashMap<>();

    @JsonCreator
    public Rating() {}

    public Rating(String username, String comment, Integer rating) {
        this.username = username;
        this.comment = comment;
        this.rating = rating;
    }

    public String getUsername() {
        return username;
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
        if (!observerUsernames.containsKey(observerType)) {
            observerUsernames.put(observerType, new ArrayList<>());
        }

        ArrayList<String> subObservers = observerUsernames.get(observerType);
        subObservers.add(((User<?>) observer).getUsername());
    }

    @Override
    public void unsubscribe(String observerType, Observer observer) {
        ArrayList<String> subObservers = observerUsernames.get(observerType);
        if (subObservers != null) {
            subObservers.remove(((User<?>) observer).getUsername());
            if (subObservers.isEmpty()) {
                observerUsernames.remove(observerType);
            }
        }
    }

    @Override
    public void sendNotification(String notificationType, String message) {
        ArrayList<String> subObserverUsernames = observerUsernames.get(notificationType);
        if (subObserverUsernames != null) {
            for (String username : subObserverUsernames) {
                Observer observer = IMDB.getInstance().getUser(username);
                if (observer != null) {
                    observer.update(message);
                }
            }
        }
    }

    @Override
    public int compareTo(Rating otherRating) {
        User<?> author = IMDB.getInstance().getUser(username);
        User<?> otherAuthor = IMDB.getInstance().getUser(otherRating.getUsername());
        if (author == null && otherAuthor == null) {
            return 0;
        }

        if (author == null) {
            return -1;
        }

        if (otherAuthor == null) {
            return -1;
        }

        return Integer.compare(author.getExperience(), otherAuthor.getExperience());
    }

    public String getComment() {
        return comment;
    }
}