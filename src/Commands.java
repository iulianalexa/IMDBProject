import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

public class Commands {
    public static class Command {
        private final List<AccountType> allowedTypes;
        private final Callable<Integer> executor;
        private final String description;

        public Command(List<AccountType> allowedTypes, Callable<Integer> executor, String description) {
            this.allowedTypes = allowedTypes;
            this.executor = executor;
            this.description = description;
        }

        public void execute() throws Exception {
            this.executor.call();
        }

        public String getDescription() {
            return description;
        }
    }

    private static Command[] get() {
        return new Command[]{
                new Command(
                        Arrays.asList(AccountType.REGULAR, AccountType.CONTRIBUTOR, AccountType.ADMIN),
                        ConsoleApp::showProduction,
                        "Show & filter all productions"
                ),

                new Command(
                        Arrays.asList(AccountType.REGULAR, AccountType.CONTRIBUTOR, AccountType.ADMIN),
                        ConsoleApp::showActors,
                        "Show all actors, sorted by name"
                ),

                new Command(
                        Arrays.asList(AccountType.REGULAR, AccountType.CONTRIBUTOR, AccountType.ADMIN),
                        ConsoleApp::showNotifications,
                        "Show all notifications for the current user"
                ),

                new Command(
                        Arrays.asList(AccountType.REGULAR, AccountType.CONTRIBUTOR, AccountType.ADMIN),
                        ConsoleApp::searchSpecific,
                        "Search for a specific movie/series/actor"
                ),

                new Command(
                        Arrays.asList(AccountType.REGULAR, AccountType.CONTRIBUTOR, AccountType.ADMIN),
                        ConsoleApp::updateFavorites,
                        "Update list of favorites for the current user"
                ),

                new Command(
                        Arrays.asList(AccountType.REGULAR, AccountType.CONTRIBUTOR),
                        ConsoleApp::manageRequests,
                        "Manage requests"
                ),

                new Command(
                        Arrays.asList(AccountType.CONTRIBUTOR, AccountType.ADMIN),
                        ConsoleApp::manageActorsAndProductions,
                        "Manage actors and productions"
                ),

                new Command(
                        Arrays.asList(AccountType.CONTRIBUTOR, AccountType.ADMIN),
                        ConsoleApp::manageStaffRequests,
                        "View and solve staff requests"
                ),

                new Command(
                        Arrays.asList(AccountType.CONTRIBUTOR, AccountType.ADMIN),
                        ConsoleApp::updateProductionActor,
                        "Update a production/actor"
                ),

                new Command(
                        Arrays.asList(AccountType.REGULAR),
                        ConsoleApp::rateProduction,
                        "Add/Remove rating for a production"
                )
        };
    }

    public static ArrayList<Command> getFor(AccountType type) {
        Command[] commands = get();
        ArrayList<Command> allowedCommands = new ArrayList<>();
        for (Command command : commands) {
            if (command.allowedTypes.contains(type)) {
                allowedCommands.add(command);
            }
        }

        return allowedCommands;
    }
}
