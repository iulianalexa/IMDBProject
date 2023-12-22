import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class ConsoleApp {
    static void showProduction() {
        ArrayList<Genre> genres = new ArrayList<>();
        Integer minimumReviewCount = 0;
        ArrayList<Production> productions = new ArrayList<>(IMDB.getInstance().getMovieList());
        productions.addAll(IMDB.getInstance().getSeriesList());

        label:
        while (true) {
            // Show production
            for (Production production : productions) {
                if ((genres.isEmpty() || !Collections.disjoint(genres, production.getGenres())) && production.getReviewCount() >= minimumReviewCount) {
                    production.displayInfo();
                }
            }

            // Filters
            System.out.println("please choose:");
            System.out.println("0. add genre to filter");
            System.out.println("1. remove genre from filter");
            System.out.println("2. set minimum review count filter");
            System.out.println("3. return");

            System.out.print("Your choice: ");
            Scanner scanner = new Scanner(System.in);
            String choice = scanner.nextLine();
            switch (choice) {
                case "0":
                    System.out.print("Genre: ");
                    try {
                        genres.add(Genre.valueOf(scanner.nextLine().toUpperCase()));
                    } catch (IllegalArgumentException e) {
                        System.out.println("Invalid genre.");
                    }
                    break;
                case "1":
                    System.out.print("Genre: ");
                    try {
                        genres.remove(Genre.valueOf(scanner.nextLine().toUpperCase()));
                    } catch (IllegalArgumentException e) {
                        System.out.println("Invalid genre.");
                    }
                    break;
                case "2":
                    System.out.print("Minimum number of reviews: ");
                    try {
                        minimumReviewCount = Integer.parseInt(scanner.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid number");
                    }
                    break;
                case "3":
                    break label;
            }
        }
    }

    static void showActors() {
        for (Actor actor: IMDB.getInstance().getActors()) {
            System.out.println(
                    actor.getName() + '\n' +
                    actor.getBiography() + '\n' +
                    "Performances: " + actor.getPerformances() + '\n'
            );
        }
    }

    static void runConsole() {
        Scanner scanner = new Scanner(System.in);
        User user;

        System.out.println("hi! please log in.");
        while (true) {
            System.out.print("username: ");
            String username = scanner.nextLine();

            System.out.print("password: ");
            String password = scanner.nextLine();

            user = IMDB.getInstance().getUser(username);

            if (user == null) {
                System.out.println("invalid username! please try again");
                continue;
            }

            if (user.checkPassword(password)) {
                break;
            }

            System.out.println("invalid password! please try again.");
        }

        System.out.printf("welcome back, %s!\n", user.getUsername());
        System.out.printf("experience: %s\n", user.getAccountType() == AccountType.ADMIN ? "-" : Integer.toString(user.getExperience()));

        // Command runner

        while (true) {
            int choice;
            ArrayList<Commands.Command> commands = Commands.getFor(user.getAccountType());

            for (int i = 0; i < commands.size(); i++) {
                System.out.printf("%d. %s\n", i, commands.get(i).getDescription());
            }

            System.out.print("command: ");
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid number");
                continue;
            }

            if (choice < 0 || choice > commands.size() - 1) {
                System.out.println("Invalid number");
                continue;
            }

            commands.get(choice).execute();
        }
    }
}
