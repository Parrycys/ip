package doobert;

public class UnmarkCommand extends Command {
    private int index;

    public UnmarkCommand(String arguments) throws DoobertException {
        try {
            this.index = Integer.parseInt(arguments.trim()) - 1; // Convert to 0-based index
        } catch (NumberFormatException e) {
            throw new DoobertException("Invalid task number. Please use: unmark <task_number>");
        }
    }

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

