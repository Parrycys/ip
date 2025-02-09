package doobert;

/**
 * Represents an abstract command that can be executed by the application.
 * Each command will perform a specific action such as adding, deleting, or modifying tasks.
 */
public abstract class Command {

    /**
     * Executes the command with the given task list, user interface, and storage.
     *
     * @param tasks   The list of tasks on which the command operates.
     * @param ui      The user interface for displaying messages.
     * @param storage The storage handler for saving and loading tasks.
     * @throws DoobertException If there is an error while executing the command.
     * @return A string representation based on the command given by user.
     */
    public abstract String execute(TaskList tasks, Ui ui, Storage storage) throws DoobertException;

    /**
     * Determines whether the command is an exit command.
     * This method can be overridden by specific commands such as "exit" to terminate the program.
     *
     * @return {@code false} by default, indicating that the program should continue running.
     */
    public boolean isExit() {
        return false;
    }
}
