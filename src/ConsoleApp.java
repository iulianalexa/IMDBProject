import java.time.LocalDateTime;
import java.util.*;

public class ConsoleApp {
    static int showProduction() throws InvalidCommandException {
        ArrayList<Genre> genres = new ArrayList<>();
        Integer minimumReviewCount = 0;
        List<Production> productions = IMDB.getInstance().getProductionList();

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

                        @SuppressWarnings("unchecked")
                        User<Production> productionUser = (User<Production>) IMDB.getInstance().getCurrentUser();
                        productionUser.addToFavourites(production);
                        System.out.println("Added production.");
                        break;
                    case "1":
                        // Adding actor
                        Actor actor = IMDB.getInstance().searchForActor(nameOfAdded);
                        if (actor == null) {
                            System.out.println("Could not find actor.");
                            break;
                        }

                        @SuppressWarnings("unchecked")
                        User<Actor> actorUser = (User<Actor>) IMDB.getInstance().getCurrentUser();
                        actorUser.addToFavourites(actor);
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
                        @SuppressWarnings("unchecked")
                        User<Production> productionUser = (User<Production>) IMDB.getInstance().getCurrentUser();
                        n = 0;
                        ArrayList<Production> productions = new ArrayList<>();

                        for (Object favorite : productionUser.getFavorites()) {
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

                        productionUser.removeFromFavourites(productions.get(choice2));
                        System.out.println("Removed production.");
                        break;
                    case "1":
                        // Removing actor
                        @SuppressWarnings("unchecked")
                        User<Actor> actorUser = (User<Actor>) IMDB.getInstance().getCurrentUser();
                        n = 0;
                        ArrayList<Actor> actors = new ArrayList<>();
                        for (Object favorite : actorUser.getFavorites()) {
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

                        actorUser.removeFromFavourites(actors.get(choice2));
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
        User<?> user = IMDB.getInstance().getCurrentUser();
        Staff<?> adder;

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
                                user.getUsername(),
                                "ADMIN"
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

                        adder = IMDB.getInstance().getAdder(actor);
                        if (adder != null && adder.getUsername().equals(user.getUsername()) && user.getAccountType() == AccountType.CONTRIBUTOR) {
                            throw new InvalidCommandException("You cannot open a request on your own contribution!");
                        }

                        request = new Request(
                                RequestType.ACTOR_ISSUE,
                                LocalDateTime.now(),
                                description,
                                user.getUsername(),
                                adder == null ? "ADMIN" : adder.getUsername()
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

                        adder = IMDB.getInstance().getAdder(production);
                        if (adder != null && adder.getUsername().equals(user.getUsername()) && user.getAccountType() == AccountType.CONTRIBUTOR) {
                            throw new InvalidCommandException("You cannot open a request on your own contribution!");
                        }

                        request = new Request(
                                RequestType.MOVIE_ISSUE,
                                LocalDateTime.now(),
                                description,
                                user.getUsername(),
                                adder == null ? "ADMIN" : adder.getUsername()
                        );
                        request.setTargetName(production.getTitle());
                        requestsManager.createRequest(request);

                        break;
                    case "3":
                        request = new Request(
                                RequestType.OTHERS,
                                LocalDateTime.now(),
                                description,
                                user.getUsername(),
                                "ADMIN"
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
                            Staff<?> adder = IMDB.getInstance().getAdder(movie);
                            if ((adder != null && adder.getUsername().equals(staff.getUsername())) || (adder == null && staff.getAccountType() == AccountType.ADMIN)) {
                                ownedProductions.add(movie);
                            }
                        }

                        for (Series series : IMDB.getInstance().getSeriesList()) {
                            Staff<?> adder = IMDB.getInstance().getAdder(series);
                            if ((adder != null && adder.getUsername().equals(staff.getUsername())) || (adder == null && staff.getAccountType() == AccountType.ADMIN)) {
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

                        staff.removeProductionSystem(ownedProductions.get(index));
                        System.out.println("Production removed.");
                        break;
                    case "1":
                        ArrayList<Actor> ownedActors = new ArrayList<>();
                        for (Actor actor : IMDB.getInstance().getActors()) {
                            Staff<?> adder = IMDB.getInstance().getAdder(actor);
                            if ((adder != null && adder.getUsername().equals(staff.getUsername())) || (adder == null && staff.getAccountType() == AccountType.ADMIN)) {
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

                        staff.removeActorSystem(ownedActors.get(index));
                        System.out.println("Removed actor.");
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
                staff.closeRequest(request, false);
                break;
            case "2":
                break;
            default:
                throw new InvalidCommandException("Invalid choice. Please retry.");
        }
        return 0;
    }

    static int updateProductionActor() throws InvalidCommandException {
        if (!(IMDB.getInstance().getCurrentUser() instanceof Staff<?> staff)) {
            return -1;
        }

        Scanner scanner = new Scanner(System.in);
        System.out.println("0. Production");
        System.out.println("1. Actor");
        System.out.println("2. Return");
        System.out.print("Your choice: ");
        String choice = scanner.nextLine();
        int n, selectionChoice;
        switch (choice) {
            case "0":
                ArrayList<Production> productions = new ArrayList<>();
                for (Movie movie : IMDB.getInstance().getMovieList()) {
                    Staff<?> adder = IMDB.getInstance().getAdder(movie);
                    if ((adder != null && adder.getUsername().equals(staff.getUsername())) || (adder == null && staff.getAccountType() == AccountType.ADMIN)) {
                        productions.add(movie);
                    }
                }

                for (Series series : IMDB.getInstance().getSeriesList()) {
                    Staff<?> adder = IMDB.getInstance().getAdder(series);
                    if ((adder != null && adder.getUsername().equals(staff.getUsername())) || (adder == null && staff.getAccountType() == AccountType.ADMIN)) {
                        productions.add(series);
                    }
                }

                n = 0;
                for (Production production : productions) {
                    System.out.printf("%d. %s\n", n, production.getTitle());
                    n++;
                }
                System.out.printf("%d. Return\n", n);
                System.out.print("Your choice: ");
                try {
                    selectionChoice = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    throw new InvalidCommandException("Not a number.");
                }

                if (selectionChoice < 0 || selectionChoice > n) {
                    throw new InvalidCommandException("Invalid choice. Please retry");
                }

                if (selectionChoice == n) {
                    break;
                }

                Production production = productions.get(selectionChoice);
                System.out.println("Update:");
                System.out.println("0. Title");
                System.out.println("1. Description");
                System.out.println("2. Directors");
                System.out.println("3. Actors");
                System.out.println("4. Genres");
                System.out.println("5. Release Year");
                if (production.getType() == ProductionType.MOVIE) {
                    System.out.println("6. Duration");
                } else if (production.getType() == ProductionType.SERIES) {
                    System.out.println("6. Number of seasons");
                    System.out.println("7. Season list");
                }
                System.out.print("Your choice: ");
                String productionField = scanner.nextLine();
                switch (productionField) {
                    case "0":
                        System.out.print("New title: ");
                        production.setTitle(scanner.nextLine());
                        System.out.println("Updated title.");
                        break;
                    case "1":
                        System.out.println("New description: ");
                        production.setPlot(scanner.nextLine());
                        System.out.println("Updated description.");
                        break;
                    case "2":
                        System.out.println("0. Add Director");
                        System.out.println("1. Remove Director");
                        System.out.println("2. Return");
                        System.out.print("Your choice: ");
                        switch (scanner.nextLine()) {
                            case "0":
                                System.out.print("Director name: ");
                                production.addDirector(scanner.nextLine());
                                System.out.println("Added director.");
                                break;
                            case "1":
                                int n1 = 0;
                                for (String director : production.getDirectors()) {
                                    System.out.printf("%d. %s\n", n1, director);
                                    n1++;
                                }
                                System.out.printf("%d. Return\n", n1);
                                int productionField1;
                                try {
                                    productionField1 = Integer.parseInt(scanner.nextLine());
                                } catch (NumberFormatException e) {
                                    throw new InvalidCommandException("Not a number.");
                                }

                                if (productionField1 < 0 || productionField1 > n1) {
                                    throw new InvalidCommandException("Invalid choice. Please retry.");
                                }

                                if (productionField1 == n1) {
                                    break;
                                }

                                production.removeDirector(production.getDirectors().get(productionField1));
                                System.out.println("Removed director.");
                                break;
                            case "2":
                                break;
                            default:
                                throw new InvalidCommandException("Invalid choice. Please retry.");
                        }
                        break;
                    case "3":
                        System.out.println("0. Add Actor");
                        System.out.println("1. Remove Actor");
                        System.out.println("2. Return");
                        System.out.print("Your choice: ");
                        switch (scanner.nextLine()) {
                            case "0":
                                System.out.print("Actor name: ");
                                Actor actor = IMDB.getInstance().searchForActor(scanner.nextLine());
                                if (actor == null) {
                                    System.out.println("Actor was not found.");
                                    break;
                                }

                                production.addActor(actor);
                                System.out.println("Added actor.");
                                break;
                            case "1":
                                int n1 = 0;
                                for (Actor actor1 : production.getActors()) {
                                    System.out.printf("%d. %s\n", n1, actor1.getName());
                                    n1++;
                                }
                                System.out.printf("%d. Return\n", n1);
                                int productionField3;
                                try {
                                    productionField3 = Integer.parseInt(scanner.nextLine());
                                } catch (NumberFormatException e) {
                                    throw new InvalidCommandException("Not a number.");
                                }

                                if (productionField3 < 0 || productionField3 > n1) {
                                    throw new InvalidCommandException("Invalid choice. Please retry.");
                                }

                                if (productionField3 == n1) {
                                    break;
                                }

                                production.removeActor(production.getActors().get(productionField3));
                                System.out.println("Removed actor.");
                                break;
                            case "2":
                                break;
                            default:
                                throw new InvalidCommandException("Invalid choice. Please retry.");
                        }
                        break;
                    case "4":
                        System.out.println("0. Add genre");
                        System.out.println("1. Remove genre");
                        System.out.println("2. Return");
                        System.out.print("Your choice: ");
                        switch (scanner.nextLine()) {
                            case "0":
                                System.out.println("Please select one of the following genres:");
                                System.out.println(Arrays.toString(Genre.values()));
                                System.out.print("Genre: ");
                                Genre genre;
                                try {
                                    genre = Genre.valueOf(scanner.nextLine().toUpperCase());
                                } catch (IllegalArgumentException e) {
                                    throw new InvalidCommandException("Invalid genre.");
                                }

                                if (production.getGenres().contains(genre)) {
                                    throw new InvalidCommandException("Genre already assigned.");
                                }

                                production.addGenre(genre);
                                System.out.println("Genre added.");
                                break;
                            case "1":
                                int n1 = 0;
                                for (Genre genre1 : production.getGenres()) {
                                    System.out.printf("%d. %s\n", n1, genre1);
                                    n1++;
                                }
                                System.out.printf("%d. Return\n", n1);
                                int productionField4;
                                try {
                                    productionField4 = Integer.parseInt(scanner.nextLine());
                                } catch (NumberFormatException e) {
                                    throw new InvalidCommandException("Not a number.");
                                }

                                if (productionField4 < 0 || productionField4 > n1) {
                                    throw new InvalidCommandException("Invalid choice. Please retry.");
                                }

                                if (productionField4 == n1) {
                                    break;
                                }

                                production.removeGenre(production.getGenres().get(productionField4));
                                System.out.println("Removed genre.");
                                break;
                            case "2":
                                break;
                            default:
                                throw new InvalidCommandException("Invalid choice. Please retry.");
                        }
                        break;
                    case "5":
                        System.out.print("Set release year: ");
                        int productionField5;
                        try {
                            productionField5 = Integer.parseInt(scanner.nextLine());
                        } catch (NumberFormatException e) {
                            throw new InvalidCommandException("Not a number.");
                        }

                        if (production instanceof Movie movie) {
                            movie.setReleaseYear(productionField5);
                        } else if (production instanceof Series series) {
                            series.setReleaseYear(productionField5);
                        }

                        System.out.println("Set release year.");
                        break;
                }

                if (production instanceof Movie movie) {
                    if (productionField.equals("6")) {
                        System.out.print("New duration: ");
                        movie.setDuration(scanner.nextLine());
                        System.out.println("Set duration.");
                    } else {
                        throw new InvalidCommandException("Invalid choice. Please retry.");
                    }
                } else if (production instanceof Series series) {
                    switch (productionField) {
                        case "6":
                            System.out.println("New number of seasons: ");
                            int productionField7;
                            try {
                                productionField7 = Integer.parseInt(scanner.nextLine());
                            } catch (NumberFormatException e) {
                                throw new InvalidCommandException("Invalid choice. Please retry.");
                            }

                            series.setNumSeasons(productionField7);
                            System.out.println("Set new number of seasons.");
                            break;
                        case "7":
                            System.out.println("0. Add season");
                            System.out.println("1. Edit season");
                            System.out.println("2. Return");
                            System.out.print("Your choice: ");
                            switch (scanner.nextLine()) {
                                case "0":
                                    System.out.print("New season name: ");
                                    series.addSeason(scanner.nextLine());
                                    System.out.println("Added new season.");
                                    break;
                                case "1":
                                    Map<String, List<Episode>> seasons = series.getSeasons();
                                    for (String seasonName : series.getSeasons().keySet()) {
                                        System.out.println(seasonName);
                                    }

                                    System.out.println("Please type the name of the season you would like to edit (case sensitive), or type nothing to return.");
                                    System.out.print("Season name: ");
                                    String productionField8 = scanner.nextLine();
                                    if (productionField8.isEmpty()) {
                                        break;
                                    }

                                    if (!seasons.containsKey(productionField8)) {
                                        throw new InvalidCommandException("Season does not exist.");
                                    }

                                    List<Episode> season = seasons.get(productionField8);
                                    System.out.println("0. Edit season name");
                                    System.out.println("1. Edit episode list");
                                    System.out.println("2. Return");
                                    System.out.print("Your choice: ");
                                    switch (scanner.nextLine()) {
                                        case "0":
                                            System.out.print("New season name: ");
                                            series.changeSeasonName(productionField8, scanner.nextLine());
                                            System.out.println("Season name changed.");
                                            break;
                                        case "1":
                                            int n1 = 0;
                                            for (Episode episode : season) {
                                                System.out.printf("%d. %s\n", n1, episode.getEpisodeName());
                                                n1++;
                                            }
                                            System.out.printf("%d. Return\n", n1);
                                            System.out.print("Your choice: ");
                                            int productionField9;
                                            try {
                                                productionField9 = Integer.parseInt(scanner.nextLine());
                                            } catch (NumberFormatException e) {
                                                throw new InvalidCommandException("Not a number.");
                                            }

                                            if (productionField9 < 0 || productionField9 > n1) {
                                                throw new InvalidCommandException("Invalid choice. Please retry.");
                                            }

                                            if (productionField9 == n1) {
                                                break;
                                            }

                                            Episode episode = season.get(productionField9);
                                            System.out.println(episode);
                                            System.out.println("0. Change episode name");
                                            System.out.println("1. Change episode duration");
                                            System.out.println("2. Remove");
                                            System.out.println("3. Return");
                                            System.out.print("Your choice: ");
                                            switch (scanner.nextLine()) {
                                                case "0":
                                                    System.out.print("New episode name: ");
                                                    episode.setEpisodeName(scanner.nextLine());
                                                    System.out.println("Episode name set.");
                                                    break;
                                                case "1":
                                                    System.out.println("New episode duration: ");
                                                    episode.setDuration(scanner.nextLine());
                                                    System.out.println("Episode duration set.");
                                                    break;
                                                case "2":
                                                    season.remove(episode);
                                                    System.out.println("Episode removed.");
                                                    break;
                                                case "3":
                                                    break;
                                                default:
                                                    throw new InvalidCommandException("Invalid choice. Please try again.");
                                            }
                                            break;
                                        case "2":
                                            break;
                                        default:
                                            throw new InvalidCommandException("Invalid choice. Please retry.");
                                    }
                                    break;
                                case "2":
                                    break;
                                default:
                                    throw new InvalidCommandException("Invalid choice. Please retry.");
                            }
                            break;
                        default:
                            throw new InvalidCommandException("Invalid choice. Please retry.");
                    }
                }

                break;
            case "1":
                ArrayList<Actor> actors = new ArrayList<>();
                for (Actor actor : IMDB.getInstance().getActors()) {
                    Staff<?> adder = IMDB.getInstance().getAdder(actor);
                    if ((adder != null && adder.getUsername().equals(staff.getUsername())) || (adder == null && staff.getAccountType() == AccountType.ADMIN)) {
                        actors.add(actor);
                    }
                }

                n = 0;
                for (Actor actor : actors) {
                    System.out.printf("%d. %s\n", n, actor.getName());
                    n++;
                }
                System.out.printf("%d. Return\n", n);
                System.out.print("Your choice: ");

                int actorIndex;
                try {
                    actorIndex = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    throw new InvalidCommandException("Not a number.");
                }

                if (actorIndex < 0 || actorIndex > n) {
                    throw new InvalidCommandException("Invalid choice. Please retry.");
                }

                Actor actor = actors.get(actorIndex);
                actor.displayFullInfo();
                System.out.println("Update actor:");
                System.out.println("0. Name");
                System.out.println("1. Biography");
                System.out.println("2. Performances");
                System.out.print("Your choice: ");
                switch (scanner.nextLine()) {
                    case "0":
                        System.out.print("New name: ");
                        actor.setName(scanner.nextLine());
                        System.out.println("Set name.");
                        break;
                    case "1":
                        System.out.print("New biography: ");
                        actor.setBiography(scanner.nextLine());
                        System.out.println("Set biography.");
                        break;
                    case "2":
                        System.out.println("0. Add performance");
                        System.out.println("1. Update existing");
                        System.out.println("2. Return");
                        System.out.print("Your choice: ");
                        switch (scanner.nextLine()) {
                            case "0":
                                System.out.println("Type:");
                                System.out.println("0. Movie");
                                System.out.println("1. Series");
                                System.out.print("Your choice: ");
                                ProductionType type = switch (scanner.nextLine()) {
                                    case "0" -> ProductionType.MOVIE;
                                    case "1" -> ProductionType.SERIES;
                                    default -> throw new InvalidCommandException("Invalid choice. Please retry.");
                                };

                                System.out.print("Title: ");
                                String title = scanner.nextLine();
                                actor.addPerformance(title, type);
                                System.out.println("Added performance.");
                                break;
                            case "1":
                                int n1 = 0;
                                for (Performance performance : actor.getPerformances()) {
                                    System.out.printf("%d. %s (%s)\n", n1, performance.getTitle(), performance.getType());
                                    n1++;
                                }
                                System.out.printf("%d. Return\n", n1);

                                int performanceIndex;
                                try {
                                    performanceIndex = Integer.parseInt(scanner.nextLine());
                                } catch (NumberFormatException e) {
                                    throw new InvalidCommandException("Not a number.");
                                }

                                if (performanceIndex < 0 || performanceIndex > n1) {
                                    throw new InvalidCommandException("Invalid choice. Please retry.");
                                }

                                if (performanceIndex == n1) {
                                    break;
                                }

                                Performance performance = actor.getPerformances().get(performanceIndex);
                                System.out.println("0. Change title");
                                System.out.println("1. Remove");
                                System.out.println("2. Return");
                                System.out.print("Your choice: ");
                                switch (scanner.nextLine()) {
                                    case "0":
                                        System.out.print("New title: ");
                                        performance.setTitle(scanner.nextLine());
                                        System.out.println("Set title.");
                                        break;
                                    case "1":
                                        actor.removePerformance(performance);
                                        System.out.println("Removed performance.");
                                        break;
                                    case "2":
                                        break;
                                    default:
                                        throw new InvalidCommandException("Invalid choice. Please retry.");
                                }

                                break;
                            case "2":
                                break;
                            default:
                                throw new InvalidCommandException("Invalid choice. Please retry.");
                        }

                    default:
                        throw new InvalidCommandException("Invalid choice. Please retry.");
                }
                break;
            case "2":
                break;
            default:
                throw new InvalidCommandException("Invalid choice. Please retry.");
        }

        return 0;
    }

    static int rateProduction() throws InvalidCommandException {
        if (!(IMDB.getInstance().getCurrentUser() instanceof Regular<?> regular)) {
            return -1;
        }

        Scanner scanner = new Scanner(System.in);
        System.out.print("Production name: ");
        Production production = IMDB.getInstance().searchForProduction(scanner.nextLine());
        if (production == null) {
            throw new InvalidCommandException("Could not find production.");
        }

        Rating currentRating = null;
        for (Rating rating : production.getRatings()) {
            if (rating.getUsername().equals(IMDB.getInstance().getCurrentUser().getUsername())) {
                currentRating = rating;
                break;
            }
        }

        boolean willRate = true;
        if (currentRating != null) {
            willRate = false;
            System.out.println("You have already rated this production!");
            System.out.println(currentRating);
            System.out.println("0. Remove rating");
            System.out.println("1. Modify rating");
            System.out.println("2. Return");
            System.out.print("Your choice: ");
            switch (scanner.nextLine()) {
                case "0":
                    production.removeRating(currentRating);
                    System.out.println("Rating removed.");
                    break;
                case "1":
                    willRate = true;
                    break;
                case "2":
                    break;
                default:
                    throw new InvalidCommandException("Invalid choice. Please retry.");
            }
        }

        if (willRate) {
            System.out.print("Score (1-10): ");
            int score;
            try {
                score = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                throw new InvalidCommandException("Not a number.");
            }

            if (score < 1 || score > 10) {
                throw new InvalidCommandException("Invalid choice. Please retry.");
            }

            System.out.print("Comment: ");
            String comment = scanner.nextLine();
            regular.rate(production, score, comment);
            if (currentRating != null) {
                production.removeRating(currentRating);
            }

            System.out.println("Thank you for your rating!");
        }
        return 0;
    }

    static int manageUsers() throws InvalidCommandException {
        if (!(IMDB.getInstance().getCurrentUser() instanceof Admin<?> admin)) {
            return -1;
        }

        Scanner scanner = new Scanner(System.in);
        System.out.println("0. Add user");
        System.out.println("1. Remove user");
        System.out.println("2. Return");
        System.out.print("Your choice: ");
        switch (scanner.nextLine()) {
            case "0":
                System.out.println("Please choose an account type from the following list:");
                System.out.println(Arrays.toString(AccountType.values()));
                System.out.print("Account type: ");
                AccountType userType;
                try {
                    userType = AccountType.valueOf(scanner.nextLine().toUpperCase());
                } catch (IllegalArgumentException e) {
                    throw new InvalidCommandException("Invalid choice. Please retry.");
                }
                System.out.println("For optional fields, type nothing to skip.");
                System.out.print("Name: ");
                String userFullName = scanner.nextLine().strip();
                System.out.print("Email*: ");
                String email = scanner.nextLine().strip();
                System.out.print("Country: ");
                String country = scanner.nextLine().strip();
                System.out.print("Gender: ");
                String gender = scanner.nextLine().strip();
                System.out.print("Birth date: ");
                String birthDate = scanner.nextLine().strip();
                System.out.print("Age: ");
                int age = -1;
                String ageToParse = scanner.nextLine().strip();
                if (!ageToParse.isEmpty()) {
                    try {
                        // why do we still need to input the age if we have a birthdate?...
                        age = Integer.parseInt(scanner.nextLine());
                    } catch (NumberFormatException e) {
                        throw new InvalidCommandException("Not a number.");
                    }
                }

                // Create credentials
                String username =  RandomPasswordGenerator.generateUsername(userFullName);
                String password = RandomPasswordGenerator.generate(20);

                User.Information.Credentials credentials = null;
                if (!email.isEmpty()) {
                    credentials = new User.Information.Credentials(email, password);
                }

                // Build information
                User.Information.InformationBuilder informationBuilder = new User.Information.InformationBuilder();
                if (credentials != null) {
                    informationBuilder = informationBuilder.credentials(credentials);
                }

                if (!userFullName.isEmpty()) {
                    informationBuilder = informationBuilder.name(userFullName);
                }

                if (!country.isEmpty()) {
                    informationBuilder = informationBuilder.country(country);
                }

                if (!gender.isEmpty()) {
                    informationBuilder = informationBuilder.gender(gender);
                }

                if (!birthDate.isEmpty()) {
                    try {
                        informationBuilder = informationBuilder.birthDate(birthDate);
                    } catch (InvalidInformationException e) {
                        throw new InvalidCommandException("Birth date is not valid!");
                    }
                }

                if (age != -1) {
                    informationBuilder = informationBuilder.age(age);
                }

                User.Information information;
                try {
                    information = informationBuilder.build();
                } catch (InformationIncompleteException e) {
                    throw new InvalidCommandException(e.getMessage());
                }

                // Create user
                User.UnknownUser unknownUser = new User.UnknownUser(username, information, userType);
                User<?> user = UserFactory.factory(unknownUser);
                admin.addUser(user);
                System.out.printf("Added user %s with password %s.\n", username, password);

                break;
            case "1":
                ArrayList<User<?>> userList = new ArrayList<>(IMDB.getInstance().getRegulars());
                userList.addAll(IMDB.getInstance().getContributors());
                userList.addAll(IMDB.getInstance().getAdmins());

                int n = 0;
                for (User<?> user1 : userList) {
                    System.out.printf("%d. %s\n", n, user1.getUsername());
                    n++;
                }

                System.out.printf("%d. Return\n", n);
                System.out.print("Your choice: ");
                int numberChoice;
                try {
                    numberChoice = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    throw new InvalidCommandException("Not a number.");
                }

                if (numberChoice < 0 || numberChoice > n) {
                    throw new InvalidCommandException("Invalid choice. Please retry.");
                }

                if (numberChoice == n) {
                    break;
                }

                // Remove user
                User<?> userToDelete = userList.get(numberChoice);
                admin.removeUser(userToDelete);
                System.out.println("User has been deleted.");
                break;
            case "2":
                break;
            default:
                throw new InvalidCommandException("Invalid choice. Please retry.");
        }

        return 0;
    }

    static void runConsole() {
        Scanner scanner = new Scanner(System.in);
        User<?> user;

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
                            IMDB.getInstance().save();
                            break commandLoop;
                        case "1":
                            IMDB.getInstance().getCurrentUser().logout();
                            ConsoleApp.runConsole();
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
                } else if (e instanceof RuntimeException e2) {
                    throw e2;
                }
                System.err.println("Impossible error.");
            }
        }
    }
}
