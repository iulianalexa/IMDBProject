import java.util.List;

public class Actor {
    static private class Production {
        String name;
        ProductionType type;
    }
    String name, biography;
    List<Production> playedIn;
}
