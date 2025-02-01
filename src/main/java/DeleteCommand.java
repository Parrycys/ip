public class DeleteCommand extends Command {
    private int index;

    public DeleteCommand(String arguments) throws DoobertException {
        try {
            this.index = Integer.parseInt(arguments.trim()) - 1; // Convert to 0-based index
        } catch (NumberFormatException e) {
            throw new DoobertException("Invalid task number. Please use: delete <task_number>");
        }
    }

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
        ui.showOutput("Noted. I've removed this task:\n   " + deletedTask +
                "\n   Now you have " + tasks.getList().size() + " tasks in the list.");
        ui.showLine();
    }
}
