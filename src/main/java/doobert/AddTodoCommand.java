package doobert;

/**
 * Represents a command to add a to-do task.
 * A to-do task only contains a description with no date or time constraints.
 */
public class AddTodoCommand extends Command {

    private String description;

    /**
     * Constructs an {@code AddTodoCommand} with the given task description.
     *
     * @param description The description of the to-do task.
     */
    public AddTodoCommand(String description) {
        this.description = description;
    }

    /**
     * Executes the command by adding a new to-do task to the task list.
     * It validates the description, saves the updated task list, and displays a confirmation message.
     *
     * @param tasks   The list of tasks to which the new to-do will be added.
     * @param ui      The user interface for displaying messages.
     * @param storage The storage handler for saving the updated task list.
     * @throws DoobertException If the description is empty or invalid.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws DoobertException  {
        DoobertException.validateTodoDescription(description);
        Todo todoTask = new Todo(description);
        tasks.addTask(todoTask);
        storage.saveTask(tasks);
        ui.showLine();
        ui.showOutput("Got it. I've added this task: \n" + "      "
                + todoTask.toString() + "\n" + "   Now you have "
                + tasks.getList().size() + " tasks in your list.");
        ui.showLine();
    }

}
