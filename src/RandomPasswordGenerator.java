import java.util.Random;

public class RandomPasswordGenerator {
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
