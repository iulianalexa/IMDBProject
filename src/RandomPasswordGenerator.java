import java.util.Random;

public class RandomPasswordGenerator {
    public static String generateUsername(String userFullName) {
        int usernameOffset = 0;
        String usernameBase = userFullName.replace(" ", "_");
        if (usernameBase.isEmpty() || usernameBase.isBlank()) {
            usernameBase = "user";
        }

        String username;
        do {
            username = usernameOffset == 0 ? usernameBase : String.format("%s_%d", usernameBase, usernameOffset);
            usernameOffset++;
        } while (IMDB.getInstance().getUser(username) != null);

        return username;
    }
    public static String generate(int characterCount) {
        String pool = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz01234567890!@#%^&*()";
        Random random = new Random();

        StringBuilder password = new StringBuilder();
        for (int i = 0; i < characterCount; i++) {
            password.append(pool.charAt(random.nextInt(pool.length())));
        }

        return password.toString();
    }
}
