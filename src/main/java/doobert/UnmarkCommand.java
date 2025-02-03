package doobert;

/**
 * Represents a command that unmarks a task as done in the task list.
 */
public class UnmarkCommand extends Command {
    private int index;

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
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws DoobertException {
        // Validate the index before deletion
        DoobertException.validateUnmarkCommand(index, tasks.getList().size());

        // Get the task reference
        Task task = tasks.getList().get(index);

        // Mark the task as undone and store the message
        String markMessage = task.markAsUndone();

        // Output without running the function again
        ui.showLine();
        tasks.unmarkTask(index);
        ui.showOutput(markMessage);
        ui.showLine();

        // Save changes
        storage.saveTask(tasks);
    }

}

