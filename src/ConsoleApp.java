import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
            actor.displayFullInfo();
        }
    }

    static void showNotifications() {
        List<String> notificationList = IMDB.getInstance().getCurrentUser().getNotificationList();
        for (String notification : notificationList) {
            System.out.println(notification);
        }
    }

    static void searchSpecific() {
        Scanner scanner = new Scanner(System.in);

        outerloop:
        while (true) {
            System.out.println("Search for:");
            System.out.println("0. Movie");
            System.out.println("1. Series");
            System.out.println("2. Actor");
            System.out.print("Your choice: ");
            String choice = scanner.nextLine();
            String name;
            switch (choice) {
                case "0":
                    System.out.print("Movie title: ");
                    name = scanner.nextLine();
                    Movie movie = IMDB.getInstance().searchForMovie(name);

                    if (movie != null) {
                        movie.displayInfo();
                    } else {
                        System.out.println("No results were found.");
                    }

                    break outerloop;

                case "1":
                    System.out.print("Series title: ");
                    name = scanner.nextLine();
                    Series series = IMDB.getInstance().searchForSeries(name);

                    if (series != null) {
                        series.displayInfo();
                    } else {
                        System.out.println("No results were found.");
                    }

                    break outerloop;

                case "2":
                    System.out.print("Actor name: ");
                    name = scanner.nextLine();
                    Actor actor = IMDB.getInstance().searchForActor(name);

                    if (actor != null) {
                        actor.displayFullInfo();
                    } else {
                        System.out.println("No results were found.");
                    }

                    break outerloop;
            }
            System.out.println("Invalid choice. Please retry.");
        }
    }

    static void updateFavorites() {
        outerloop:
        while (true) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Update favorites list:");
            System.out.println("0. Add");
            System.out.println("1. Remove");
            System.out.print("Your choice: ");
            String choice = scanner.nextLine();
            if (!choice.equals("0") && !choice.equals("1")) {
                System.out.println("Invalid choice. Please retry.");
                continue;
            }

            System.out.println("Select update category:");
            System.out.println("0. Production");
            System.out.println("1. Actor");
            System.out.print("Your choice: ");
            String choice1 = scanner.nextLine();
            if (!choice1.equals("0") && !choice1.equals("1")) {
                System.out.println("Invalid choice. Please retry.");
                continue;
            }

            System.out.print("Name of updated object: ");
            String choice2 = scanner.nextLine();

            // Actual updating
            switch (choice1) {
                case "0":
                    Production production = IMDB.getInstance().searchForProduction(choice2);
                    if (production == null) {
                        System.out.println("No results were found.");
                        break outerloop;
                    }

                    System.out.println(
                            "Preview: You are going to " +
                                    (choice.equals("0") ? "ADD" : "REMOVE") +
                                    " the following production:"
                    );

                    production.displayInfo();

                    while (true) {
                        System.out.print("Confirm? (y/n): ");
                        String confirmation = scanner.nextLine();
                        if (confirmation.equalsIgnoreCase("y")) {
                            User<Production> user = (User<Production>) IMDB.getInstance().getCurrentUser();
                            switch (choice) {
                                case "0":
                                    // Adding
                                    if (user.getFavorites().contains(production)) {
                                        System.out.println("Production already added to favorites.");
                                        break;
                                    }
                                    user.addToFavourites(production);
                                    System.out.println("Added production.");
                                    break;
                                case "1":
                                    // Removing
                                    if (!user.getFavorites().contains(production)) {
                                        System.out.println("Production is not in your favorites list.");
                                        break;
                                    }
                                    user.removeFromFavourites(production);
                                    System.out.println("Removed production.");
                                    break;
                            }
                            break;
                        } else if (confirmation.equalsIgnoreCase("n")) {
                            System.out.println("Aborting.");
                            break;
                        }
                        System.out.println("Invalid choice.");
                    }

                    break outerloop;
                case "1":
                    Actor actor = IMDB.getInstance().searchForActor(choice2);

                    if (actor == null) {
                        System.out.println("No results were found.");
                        break outerloop;
                    }

                    System.out.println(
                            "Preview: You are going to " +
                                    (choice.equals("0") ? "ADD" : "REMOVE") +
                                    " the following actor:"
                    );

                    actor.displayFullInfo();

                    while (true) {
                        System.out.print("Confirm? (y/n): ");
                        String confirmation = scanner.nextLine();
                        if (confirmation.equalsIgnoreCase("y")) {
                            User<Actor> user = (User<Actor>) IMDB.getInstance().getCurrentUser();
                            switch (choice) {
                                case "0":
                                    // Adding
                                    if (user.getFavorites().contains(actor)) {
                                        System.out.println("Actor already added to favorites.");
                                        break;
                                    }
                                    user.addToFavourites(actor);
                                    System.out.println("Added actor.");
                                    break;
                                case "1":
                                    // Removing
                                    if (!user.getFavorites().contains(actor)) {
                                        System.out.println("Actor is not in your favorites list.");
                                        break;
                                    }
                                    user.removeFromFavourites(actor);
                                    System.out.println("Removed actor.");
                                    break;
                            }
                            break;
                        } else if (confirmation.equalsIgnoreCase("n")) {
                            System.out.println("Aborting.");
                            break;
                        }
                        System.out.println("Invalid choice.");
                    }

                    break outerloop;
            }
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

        IMDB.getInstance().setCurrentUser(user);
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
