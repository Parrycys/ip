package doobert;

/**
 * Represents the command to exit the application.
 * This command displays a farewell message and signals termination.
 */
public class ExitCommand extends Command {

    /**
     * Executes the exit command by displaying a farewell message.
     *
     * @param tasks   The current task list (not used in this command).
     * @param ui      The UI instance to handle user interaction.
     * @param storage The storage instance (not used in this command).
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showLine();
        ui.showOutput("Bye. Hope to see you again soon!");
        ui.showLine();
    }

    /**
     * Indicates that this command should terminate the chatbot application.
     *
     * @return {@code true}, signaling that the application should exit.
     */
    @Override
    public boolean isExit() {
        return true;
    }
}
