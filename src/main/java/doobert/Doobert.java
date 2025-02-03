package doobert;

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
    private static final String FILE_PATH = "./data/doobert.txt";

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
                c.execute(tasks, ui, storage);
                isExit = c.isExit();
            } catch (DoobertException e) {
                ui.showError(e.getMessage());
            }
        }
    }

    /**
     * The main entry point of the application.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        new Doobert("./data/doobert.txt").run();
    }

}
