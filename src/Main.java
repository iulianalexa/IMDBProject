public class Main {
    public static void main(String[] args) {
        IMDB imdb = IMDB.getInstance();

        if (args.length >= 1 && args[0].equals("nogui")) {
            imdb.setNoGui(true);
        }
        imdb.run();
    }
}
