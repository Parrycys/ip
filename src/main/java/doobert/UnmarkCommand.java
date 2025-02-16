package doobert;

/**
 * Represents a command that unmarks a task as done in the task list.
 */
public class UnmarkCommand extends Command {
    private final int index;

    /**
     * Constructs an {@code UnmarkCommand} object with the specified task index.
     *
     * @param arguments The task number to be unmarked (1-based index).
     * @throws DoobertException If the input is not a valid integer.
     */
    public UnmarkCommand(String arguments) throws DoobertException {
        try {
            this.index = Integer.parseInt(arguments.trim()) - 1; // Convert to 0-based index
        } catch (NumberFormatException e) {
            throw new DoobertException("Invalid task number. Please use: unmark <task_number>");
        }
    }

    /**
     * Executes the unmark command, marking a task as undone in the task list.
     * The updated list is saved to storage, and the user is notified.
     *
     * @param tasks   The task list containing all tasks.
     * @param ui      The user interface for displaying messages.
     * @param storage The storage system for saving tasks.
     * @throws DoobertException If the task index is invalid.
     * @return A string representation of the task unmarked.
     */
    @Override
    public String execute(TaskList tasks, Ui ui, Storage storage) throws DoobertException {
        // Validate the index before unmarking
        DoobertException.validateTaskIndex(index, tasks.getList().size());

        // Get the task reference
        Task task = tasks.getList().get(index);

        // Mark the task as undone and store the message
        String markMessage = task.markAsUndone();

        // Save changes
        storage.saveTask(tasks);

        // Output without running the function again
        tasks.unmarkTask(index);
        return markMessage;


    }

}

