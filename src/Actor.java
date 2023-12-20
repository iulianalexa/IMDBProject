import java.util.List;

enum Type {
    MOVIE,
    SERIES
}

public class Actor {
    static private class Production {
        String name;
        Type type;
    }
    String name, biography;
    List<Production> playedIn;
}
