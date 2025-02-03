package doobert;

import java.util.Arrays;

/**
 * Represents a command to add a deadline task.
 * A deadline task contains a description and a due date/time.
 */
public class AddDeadlineCommand extends Command {
    private final String description;
    private final String by;

    /**
     * Constructs an {@code AddDeadlineCommand} with the given arguments.
     * It extracts the description and deadline time from the input string.
     *
     * @param arguments The user input containing the task description and deadline.
     * @throws DoobertException If the command format is invalid.
     */
    public AddDeadlineCommand(String arguments) throws DoobertException {
        String[] parts = arguments.split("/by", 2);

        // Ensure that "/by" and description exists in the input
        DoobertException.validateDeadlineCommand(parts);

        this.description = parts[0].trim();
        this.by = parts[1].trim();
    }

    /**
     * Executes the command by adding a new deadline task to the task list.
     * It then saves the updated list and displays a confirmation message.
     *
     * @param tasks   The list of tasks to which the new deadline will be added.
     * @param ui      The user interface for displaying messages.
     * @param storage The storage handler for saving the updated task list.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        Deadline deadline = new Deadline(description, by);
        tasks.addTask(deadline);
        storage.saveTask(tasks);
        ui.showLine();
        ui.showOutput("Got it. I've added this task:\n" + "   "
                + deadline + "\n   Now you have " + tasks.getList().size()
                + " tasks in the list.");
        ui.showLine();
    }
}
