package doobert;


public class Doobert {
    /*
    Ui: deals with interactions with the user
    Storage: deals with loading tasks from the file and saving tasks in the file
    Parser: deals with making sense of the user command
    TaskList: contains the task list e.g., it has operations to add/delete tasks in the list
     */
    private Storage storage;
    private TaskList tasks;
    private Ui ui;
    private Parser parser;
    private static final String FILE_PATH = "./data/doobert.txt";

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

    public static void main(String[] args) {
        new Doobert("./data/doobert.txt").run();
    }

}
