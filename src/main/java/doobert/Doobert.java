package doobert;

import java.io.File;

/**
 * The main class for the Doobert chatbot application.
 * <p>
 * This chatbot helps users manage their tasks, including adding,
 * deleting, and marking tasks as done. It interacts with the user,
 * processes commands, and manages task storage.
 */
public class Doobert {
    /*
    Ui: deals with interactions with the user
    Storage: deals with loading tasks from the file and saving tasks in the file
    Parser: deals with making sense of the user command
    TaskList: contains the task list e.g., it has operations to add/delete tasks in the list
    FILE_PATH: The file path where tasks are stored.
     */
    private Storage storage;
    private TaskList tasks;
    private Ui ui;
    private Parser parser;

    /**
     * Constructs a new Doobert instance.
     *
     * @param FILE_PATH The file path where task data is stored.
     */
    public Doobert(String FILE_PATH) {
        ui = new Ui();
        storage = new Storage(FILE_PATH);
        parser = new Parser();

        try {
            tasks = new TaskList(storage.loadTasks());
        } catch (DoobertException e) {
            ui.showLoadingError();
            tasks = new TaskList();
        }
    }

    /**
     * Starts the chatbot and processes user commands in a loop until 'bye' command.
     */
    public void run() {
        ui.showWelcome();
        boolean isExit = false;
        while (!isExit) {
            try {
                String fullCommand = ui.readCommand();
                Command c = parser.parse(fullCommand);
                assert c != null : "Parsed command should never be null.";
                c.execute(tasks, ui, storage);
                isExit = c.isExit();
            } catch (DoobertException e) {
                ui.showError(e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        new Doobert("./data/doobert.txt").run();
    }

    public String getStartupMessage() {
        File file = new File("./data/doobert.txt");

        if (!file.exists()) {
            return "Welcome to Doobert!\nNo saved file found. "
                    + "Creating one just for you. "
                    + "Start adding tasks!";
        }
        assert file.exists() : "File should be created but does not exist!";

        if (tasks.getList().isEmpty()) {
            return "Welcome to Doobert!\nYou have no saved tasks.";
        }

        StringBuilder response = new StringBuilder("Welcome to Doobert!\nHere are your saved tasks:\n");
        for (int i = 0; i < tasks.getList().size(); i++) {
            response.append((i + 1)).append(". ").append(tasks.getList().get(i)).append("\n");
        }
        return response.toString();
    }

    /**
     * Generates a response for the user's chat message.
     */
    public String getResponse(String input) {
        try {
            // 1. Parse the input into a Command object
            Command c = parser.parse(input);

            // 2. Execute the command and return the output string
            return c.execute(tasks, ui, storage);

        } catch (DoobertException e) {
            // If an error occurs, return the error message
            return "Error: " + e.getMessage();
        }
    }


}
