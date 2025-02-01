public class MarkCommand extends Command {
    private int index;

    public MarkCommand(String arguments) throws DoobertException {
        try {
            this.index = Integer.parseInt(arguments.trim()) - 1; // Convert to 0-based index
        } catch (NumberFormatException e) {
            throw new DoobertException("Invalid task number. Please use: mark <task_number>");
        }
    }


    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws DoobertException {
        // Validate the index before marking
        DoobertException.validateMarkCommand(index, tasks.getList().size());

        // Get the task reference
        Task task = tasks.getList().get(index);

        // Mark the task as done and store the message
        String markMessage = task.markAsDone();

        // Output without running the function again
        ui.showLine();
        tasks.markTask(index);
        ui.showOutput(markMessage);
        ui.showLine();

        // Save changes
        storage.saveTask(tasks);
    }

}
