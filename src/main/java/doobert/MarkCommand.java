package doobert;

/**
 * Represents a command to mark a specific task as done.
 */
public class MarkCommand extends Command {
    private int index;

    /**
     * Constructs a MarkCommand to mark a task as done.
     *
     * @param arguments The task number (1-based) to be marked as done.
     * @throws DoobertException If the task number is not a valid integer.
     */
    public MarkCommand(String arguments) throws DoobertException {
        try {
            this.index = Integer.parseInt(arguments.trim()) - 1; // Convert to 0-based index
        } catch (NumberFormatException e) {
            throw new DoobertException("Invalid task number. Please use: mark <task_number>");
        }
    }

    /**
     * Executes the mark command by marking a task as done.
     *
     * @param tasks   The task list containing all tasks.
     * @param ui      The UI instance used to interact with the user.
     * @param storage The storage instance to save the updated task list.
     * @throws DoobertException If the task index is invalid.
     * @return A string representation of the task marked.
     */
    @Override
    public String execute(TaskList tasks, Ui ui, Storage storage) throws DoobertException {
        // Validate the index before marking
        DoobertException.validateMarkCommand(index, tasks.getList().size());

        // Get the task reference
        Task task = tasks.getList().get(index);

        // Mark the task as done and store the message
        String markMessage = task.markAsDone();

        // Save changes
        storage.saveTask(tasks);

        // Output without running the function again
        tasks.markTask(index);
        return markMessage;
    }

}
