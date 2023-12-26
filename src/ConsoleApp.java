import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

public class ConsoleApp {
    static int showProduction() throws InvalidCommandException {
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
                    System.out.println("Please choose a genre from the following list:");
                    System.out.println(Arrays.toString(Genre.values()));
                    System.out.print("Genre: ");
                    try {
                        genres.add(Genre.valueOf(scanner.nextLine().toUpperCase()));
                    } catch (IllegalArgumentException e) {
                        throw new InvalidCommandException("Invalid genre");
                    }
                    break;
                case "1":
                    System.out.print("Genre: ");
                    try {
                        genres.remove(Genre.valueOf(scanner.nextLine().toUpperCase()));
                    } catch (IllegalArgumentException e) {
                        throw new InvalidCommandException("Invalid genre");
                    }
                    break;
                case "2":
                    System.out.print("Minimum number of reviews: ");
                    try {
                        minimumReviewCount = Integer.parseInt(scanner.nextLine());
                    } catch (NumberFormatException e) {
                        throw new InvalidCommandException("Invalid number");
                    }
                    break;
                case "3":
                    break label;
                default:
                    throw new InvalidCommandException("Invalid choice. Please retry");
            }
        }

        return 0;
    }

    static int showActors() {
        for (Actor actor: IMDB.getInstance().getActors()) {
            actor.displayFullInfo();
        }

        return 0;
    }

    static int showNotifications() {
        List<String> notificationList = IMDB.getInstance().getCurrentUser().getNotificationList();
        for (String notification : notificationList) {
            System.out.println(notification);
        }

        return 0;
    }

    static int searchSpecific() throws InvalidCommandException {
        Scanner scanner = new Scanner(System.in);

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

                break;

            case "1":
                System.out.print("Series title: ");
                name = scanner.nextLine();
                Series series = IMDB.getInstance().searchForSeries(name);

                if (series != null) {
                    series.displayInfo();
                } else {
                    System.out.println("No results were found.");
                }

                break;

            case "2":
                System.out.print("Actor name: ");
                name = scanner.nextLine();
                Actor actor = IMDB.getInstance().searchForActor(name);

                if (actor != null) {
                    actor.displayFullInfo();
                } else {
                    System.out.println("No results were found.");
                }

                break;

            default:
                throw new InvalidCommandException("Invalid choice. Please retry.");
        }

        return 0;
    }

    static int updateFavorites() throws InvalidCommandException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Update favorites list:");
        System.out.println("0. Add");
        System.out.println("1. Remove");
        System.out.print("Your choice: ");
        String choice = scanner.nextLine();
        if (!choice.equals("0") && !choice.equals("1")) {
            throw new InvalidCommandException("Invalid command. Please retry.");
        }

        System.out.println("Select update category:");
        System.out.println("0. Production");
        System.out.println("1. Actor");
        System.out.print("Your choice: ");
        String choice1 = scanner.nextLine();
        if (!choice1.equals("0") && !choice1.equals("1")) {
            throw new InvalidCommandException("Invalid command. Please retry.");
        }

        String nameOfAdded = null;

        if (choice.equals("0")) {
            System.out.print("Name of added object: ");
            nameOfAdded = scanner.nextLine();
        }

        // Actual updating
        switch (choice) {
            case "0":
                // Adding
                switch (choice1) {
                    case "0":
                        // Adding production
                        Production production = IMDB.getInstance().searchForProduction(nameOfAdded);
                        if (production == null) {
                            System.out.println("Could not find production.");
                            break;
                        }
                        ((User<Production>) IMDB.getInstance().getCurrentUser()).addToFavourites(production);
                        System.out.println("Added production.");
                        break;
                    case "1":
                        // Adding actor
                        Actor actor = IMDB.getInstance().searchForActor(nameOfAdded);
                        if (actor == null) {
                            System.out.println("Could not find actor.");
                            break;
                        }
                        ((User<Actor>) IMDB.getInstance().getCurrentUser()).addToFavourites(actor);
                        System.out.println("Added actor.");
                        break;
                }
                break;
            case "1":
                // Removing
                int n, choice2;
                switch (choice1) {
                    case "0":
                        // Removing production
                        User<Production> user0 = (User<Production>) IMDB.getInstance().getCurrentUser();
                        n = 0;
                        ArrayList<Production> productions = new ArrayList<>();

                        for (Object favorite : user0.getFavorites()) {
                            if (favorite instanceof Production production) {
                                productions.add(production);
                                System.out.printf("%d. %s\n", n, production.getTitle());
                                n++;
                            }
                        }

                        System.out.printf("%d. Return\n", n);
                        System.out.print("Your choice: ");
                        try {
                            choice2 = Integer.parseInt(scanner.nextLine());
                        } catch (NumberFormatException e) {
                            throw new InvalidCommandException("Invalid number.");
                        }
                        if (choice2 < 0 || choice2 > n) {
                            throw new InvalidCommandException("Invalid number.");
                        }

                        if (choice2 == n) {
                            break;
                        }

                        user0.removeFromFavourites(productions.get(choice2));
                        System.out.println("Removed production.");
                        break;
                    case "1":
                        // Removing actor
                        User<Actor> user1 = (User<Actor>) IMDB.getInstance().getCurrentUser();
                        n = 0;
                        ArrayList<Actor> actors = new ArrayList<>();
                        for (Object favorite : user1.getFavorites()) {
                            if (favorite instanceof Actor actor) {
                                actors.add(actor);
                                System.out.printf("%d. %s\n", n, actor.getName());
                                n++;
                            }
                        }

                        System.out.printf("%d. Return\n", n);
                        System.out.print("Your choice: ");
                        try {
                            choice2 = Integer.parseInt(scanner.nextLine());
                        } catch (NumberFormatException e) {
                            throw new InvalidCommandException("Invalid number.");
                        }
                        if (choice2 < 0 || choice2 > n) {
                            throw new InvalidCommandException("Invalid number.");
                        }

                        if (choice2 == n) {
                            break;
                        }

                        user1.removeFromFavourites(actors.get(choice2));
                        System.out.println("Removed actor.");
                        break;
                }
                break;
        }

        return 0;
    }

    static int manageRequests() throws InvalidCommandException {
        Scanner scanner = new Scanner(System.in);
        if (!(IMDB.getInstance().getCurrentUser() instanceof RequestsManager requestsManager)) {
            return -1;
        }

        System.out.println("0. List and manage your current requests");
        System.out.println("1. Add new request");
        System.out.print("Your choice: ");
        String choice = scanner.nextLine();
        switch (choice) {
            case "0":
                int n = 0;
                for (Request r : requestsManager.getRequests()) {
                    System.out.printf("%d. %s\n", n, r.getDescription());
                    n++;
                }
                System.out.printf("%d. Return\n", n);
                System.out.print("Your choice: ");
                int subChoice;
                try {
                    subChoice = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    throw new InvalidCommandException("Invalid selection. Please retry.");
                }

                if (subChoice < 0 || subChoice > n) {
                   throw new InvalidCommandException("Invalid selection. Please retry.");
                }

                if (subChoice == n) {
                    return 0;
                }

                Request selectedRequest = requestsManager.getRequests().get(subChoice);
                System.out.println(selectedRequest);

                System.out.println("0. Remove");
                System.out.println("1. Return");
                System.out.print("Your choice: ");
                String subChoice1 = scanner.nextLine();
                switch (subChoice1) {
                    case "0":
                        requestsManager.removeRequest(selectedRequest);
                        break;
                    case "1":
                        break;
                    default:
                        throw new InvalidCommandException("Invalid choice. Please retry.");
                }

                break;

            case "1":
                System.out.println("0. Delete account");
                System.out.println("1. Actor issue");
                System.out.println("2. Production issue");
                System.out.println("3. Others");
                System.out.print("Your choice: ");
                String typeChoice = scanner.nextLine();
                if (!typeChoice.equals("0") && !typeChoice.equals("1") && !typeChoice.equals("2") && !typeChoice.equals("3")) {
                    throw new InvalidCommandException("Invalid choice. Please retry.");
                }

                System.out.print("Describe your issue: ");
                String description = scanner.nextLine();
                Request request;

                switch (typeChoice) {
                    case "0":
                        request = new Request(
                                RequestType.DELETE_ACCOUNT,
                                LocalDateTime.now(),
                                description,
                                IMDB.getInstance().getCurrentUser().getUsername(),
                                "ADMIN",
                                false
                        );

                        requestsManager.createRequest(request);
                        break;
                    case "1":
                        System.out.print("Actor name: ");
                        String actorName = scanner.nextLine();
                        Actor actor = IMDB.getInstance().searchForActor(actorName);
                        if (actor == null) {
                            break;
                        }

                        if (actor.getAddedBy().equals(IMDB.getInstance().getCurrentUser().getUsername()) && IMDB.getInstance().getCurrentUser().getAccountType() == AccountType.CONTRIBUTOR) {
                            throw new InvalidCommandException("You cannot open a request on your own contribution!");
                        }

                        request = new Request(
                                RequestType.ACTOR_ISSUE,
                                LocalDateTime.now(),
                                description,
                                IMDB.getInstance().getCurrentUser().getUsername(),
                                actor.getAddedBy() == null ? "ADMIN" : actor.getAddedBy(),
                                actor.getAddedBy() != null
                        );
                        request.setTargetName(actor.getName());
                        requestsManager.createRequest(request);

                        break;
                    case "2":
                        System.out.print("Production title: ");
                        String productionTitle = scanner.nextLine();
                        Production production = IMDB.getInstance().searchForProduction(productionTitle);
                        if (production == null) {
                            break;
                        }

                        if (production.getAddedBy().equals(IMDB.getInstance().getCurrentUser().getUsername()) && IMDB.getInstance().getCurrentUser().getAccountType() == AccountType.CONTRIBUTOR) {
                            throw new InvalidCommandException("You cannot open a request on your own contribution!");
                        }

                        request = new Request(
                                RequestType.MOVIE_ISSUE,
                                LocalDateTime.now(),
                                description,
                                IMDB.getInstance().getCurrentUser().getUsername(),
                                production.getAddedBy() == null ? "ADMIN" : production.getAddedBy(),
                                production.getAddedBy() != null
                        );
                        request.setTargetName(production.getTitle());
                        requestsManager.createRequest(request);

                        break;
                    case "3":
                        request = new Request(
                                RequestType.OTHERS,
                                LocalDateTime.now(),
                                description,
                                IMDB.getInstance().getCurrentUser().getUsername(),
                                "ADMIN",
                                false
                        );

                        requestsManager.createRequest(request);

                        break;
                    default:
                        throw new InvalidCommandException("Invalid choice. Please retry.");
                }

                System.out.println("Your request has been sent!");

                break;

            default:
                throw new InvalidCommandException("Invalid choice. Please retry.");
        }

        return 0;
    }

    static int manageActorsAndProductions() throws InvalidCommandException {
        if (!(IMDB.getInstance().getCurrentUser() instanceof Staff<?> staff)) {
            return -1;
        }

        Scanner scanner = new Scanner(System.in);
        System.out.println("0. Add");
        System.out.println("1. Remove");
        System.out.print("Your choice: ");
        String choice = scanner.nextLine();
        String choice1;
        switch (choice) {
            case "0":
                System.out.println("0. Production");
                System.out.println("1. Actor");
                System.out.print("Your choice: ");
                choice1 = scanner.nextLine();
                switch (choice1) {
                    case "0":
                        System.out.print("Title: ");
                        String title = scanner.nextLine();
                        System.out.print("Description: ");
                        String description = scanner.nextLine();

                        System.out.print("Release year: ");
                        int releaseYear;
                        try {
                            releaseYear = Integer.parseInt(scanner.nextLine());
                        } catch (NumberFormatException e) {
                            throw new InvalidCommandException("Not a number.");
                        }

                        System.out.println("Type of production:");
                        System.out.println("0. Movie");
                        System.out.println("1. Series");
                        System.out.print("Your choice: ");
                        String choiceType = scanner.nextLine();

                        switch (choiceType) {
                            case "0":
                                System.out.print("Duration: ");
                                String duration = scanner.nextLine();

                                Movie movie = new Movie(title, description, duration, releaseYear);
                                movie.setAddedBy(staff.getUsername());
                                staff.addProductionSystem(movie);
                                break;
                            case "1":
                                System.out.print("Number of seasons: ");
                                int numberOfSeasons;
                                try {
                                    numberOfSeasons = Integer.parseInt(scanner.nextLine());
                                } catch (NumberFormatException e) {
                                    throw new InvalidCommandException("Not a number.");
                                }
                                Series series = new Series(title, description, releaseYear, numberOfSeasons);
                                series.setAddedBy(staff.getUsername());
                                staff.addProductionSystem(series);
                                break;
                            default:
                                throw new InvalidCommandException("Invalid choice. Please retry.");
                        }
                        break;
                    case "1":
                        System.out.print("Name: ");
                        String actorName = scanner.nextLine();
                        System.out.print("Biography: ");
                        String actorBiography = scanner.nextLine();
                        Actor actor = new Actor(actorName, actorBiography);
                        actor.setAddedBy(staff.getUsername());
                        staff.addActorSystem(actor);
                        break;
                    default:
                        throw new InvalidCommandException("Invalid choice. Please retry.");
                }
                break;
            case "1":
                System.out.println("0. Production");
                System.out.println("1. Actor");
                System.out.print("Your choice: ");
                String removeTypeChoice = scanner.nextLine();
                int n, index;
                switch (removeTypeChoice) {
                    case "0":
                        ArrayList<Production> ownedProductions = new ArrayList<>();
                        for (Movie movie : IMDB.getInstance().getMovieList()) {
                            if ((movie.getAddedBy() != null && movie.getAddedBy().equals(staff.getUsername())) || (movie.getAddedBy() == null && staff.getAccountType() == AccountType.ADMIN)) {
                                ownedProductions.add(movie);
                            }
                        }

                        for (Series series : IMDB.getInstance().getSeriesList()) {
                            if ((series.getAddedBy() != null && series.getAddedBy().equals(staff.getUsername())) || (series.getAddedBy() == null && staff.getAccountType() == AccountType.ADMIN)) {
                                ownedProductions.add(series);
                            }
                        }

                        n = 0;
                        for (Production production : ownedProductions) {
                            System.out.printf("%d. %s\n", n, production.getTitle());
                            n++;
                        }

                        System.out.printf("%d. Return\n", n);
                        System.out.print("Your choice: ");
                        try {
                            index = Integer.parseInt(scanner.nextLine());
                        } catch (NumberFormatException e) {
                            throw new InvalidCommandException("Not a number.");
                        }

                        if (index == n) {
                            break;
                        }

                        staff.removeProductionSystem(ownedProductions.get(index).getTitle());

                        break;
                    case "1":
                        ArrayList<Actor> ownedActors = new ArrayList<>();
                        for (Actor actor : IMDB.getInstance().getActors()) {
                            if ((actor.getAddedBy() != null && actor.getAddedBy().equals(staff.getUsername())) || (actor.getAddedBy() == null && staff.getAccountType() == AccountType.ADMIN)) {
                                ownedActors.add(actor);
                            }
                        }

                        n = 0;
                        for (Actor actor : ownedActors) {
                            System.out.printf("%d. %s\n", n, actor.getName());
                            n++;
                        }

                        System.out.printf("%d. Return\n", n);
                        System.out.print("Your choice: ");
                        try {
                            index = Integer.parseInt(scanner.nextLine());
                        } catch (NumberFormatException e) {
                            throw new InvalidCommandException("Not a number.");
                        }

                        if (index == n) {
                            break;
                        }

                        staff.removeActorSystem(ownedActors.get(index).getName());

                        break;
                    default:
                        throw new InvalidCommandException("Invalid choice. Please retry.");
                }
                break;
            default:
                throw new InvalidCommandException("Invalid choice. Please retry.");
        }
        return 0;
    }

    static int manageStaffRequests() throws InvalidCommandException {
        if (!(IMDB.getInstance().getCurrentUser() instanceof Staff<?> staff)) {
            return -1;
        }

        int n = 0;
        Scanner scanner = new Scanner(System.in);

        ArrayList<Request> requestList = new ArrayList<>(staff.getRequestList());
        if (staff.getAccountType() == AccountType.ADMIN) {
            requestList.addAll(RequestsHolder.getRequests());
        }

        System.out.println("Select a request:");
        for (Request request : requestList) {
            System.out.printf("%d. %s\n", n, request.getDescription());
            n++;
        }

        System.out.printf("%d. Return\n", n);
        System.out.print("Your choice: ");
        int choice;
        try {
            choice = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            throw new InvalidCommandException("Not a number.");
        }

        if (choice < 0 || choice > n) {
            throw new InvalidCommandException("Invalid choice. Please retry.");
        }

        if (choice == n) {
            return 0;
        }

        Request request = requestList.get(choice);
        System.out.println(request);
        System.out.println("0. Accept");
        System.out.println("1. Reject");
        System.out.println("2. Return");
        System.out.print("Your choice: ");
        switch (scanner.nextLine()) {
            case "0":
                staff.solveRequest(request);
                break;
            case "1":
                staff.closeRequest(request);
                break;
            case "2":
                break;
            default:
                throw new InvalidCommandException("Invalid choice. Please retry.");
        }
        return 0;
    }

    static void runConsole() throws IOException {
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

        commandLoop:
        while (true) {
            int choice;
            ArrayList<Commands.Command> commands = Commands.getFor(user.getAccountType());

            for (int i = 0; i < commands.size(); i++) {
                System.out.printf("%d. %s\n", i, commands.get(i).getDescription());
            }

            System.out.printf("%d. logout\n", commands.size());

            System.out.print("command: ");
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid number");
                continue;
            }

            if (choice < 0 || choice > commands.size()) {
                System.out.println("Invalid number");
                continue;
            }

            if (choice == commands.size()) {
                // Logout
                while (true) {
                    System.out.println("Exit?");
                    System.out.println("0. Yes");
                    System.out.println("1. No");
                    System.out.print("Your choice: ");
                    String exitChoice = scanner.nextLine();
                    switch (exitChoice) {
                        case "0":
                            break commandLoop;
                        case "1":
                            IMDB.getInstance().logout();
                            IMDB.getInstance().run();
                            break commandLoop;
                        default:
                            System.out.println("Invalid choice. Please retry");
                            break;
                    }
                }
            }

            try {
                commands.get(choice).execute();
            } catch (Exception e) {
                if (e instanceof InvalidCommandException e1) {
                    System.out.println(e1.getMessage());
                } else {
                    e.printStackTrace();
                }
            }
        }
    }
}
