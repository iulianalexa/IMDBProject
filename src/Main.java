import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        IMDB imdb = IMDB.getInstance();
        imdb.run();
    }
}
