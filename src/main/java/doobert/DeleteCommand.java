package doobert;

/**
 * Represents a command to delete a task from the task list.
 */
public class DeleteCommand extends Command {
    private int index;

    /**
     * Constructs a {@code DeleteCommand} with the specified task number.
     *
     * @param arguments The task number to be deleted (1-based index).
     * @throws DoobertException If the input is not a valid integer.
     */
    public DeleteCommand(String arguments) throws DoobertException {
        try {
            this.index = Integer.parseInt(arguments.trim()) - 1; // Convert to 0-based index
        } catch (NumberFormatException e) {
            throw new DoobertException("Invalid task number. Please use: delete <task_number>");
        }
    }

    /**
     * Executes the delete command by removing the specified task from the task list.
     *
     * @param tasks   The list of tasks.
     * @param ui      The user interface for displaying messages.
     * @param storage The storage handler for saving task updates.
     * @throws DoobertException If the task index is out of bounds.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws DoobertException {
        // Validate the index before deletion
        DoobertException.validateDeleteCommand(index, tasks.getList().size());
        Task deletedTask = tasks.getList().get(index);

        // Perform deletion
        tasks.deleteTask(index);

        // Save updated tasks
        storage.saveTask(tasks);

        // Notify the user
        ui.showLine();
        ui.showOutput("Noted. I've removed this task:\n   " + deletedTask
                + "\n   Now you have " + tasks.getList().size() + " tasks in the list.");
        ui.showLine();
    }
}
