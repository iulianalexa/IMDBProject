import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Commands {
    public static class Command {
        private final List<AccountType> allowedTypes;
        private final Runnable executor;
        private final String description;

        public Command(List<AccountType> allowedTypes, Runnable executor, String description) {
            this.allowedTypes = allowedTypes;
            this.executor = executor;
            this.description = description;
        }

        public void execute() {
            this.executor.run();
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
