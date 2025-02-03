package doobert;

/**
 * Represents a command to add an event task.
 * An event task includes a description, start time ("from"), and end time ("to").
 */
public class AddEventCommand extends Command {

    private String description;
    private String from;
    private String to;

    /**
     * Constructs an {@code AddEventCommand} with the given user input.
     * It extracts the task description, start time, and end time from the input string.
     *
     * @param arguments The user input containing the task description, start time, and end time.
     * @throws DoobertException If the command format is invalid.
     */
    public AddEventCommand(String arguments) throws DoobertException{
        String[] parts = arguments.split("/from", 2); // Split at "/from" into description + time
        DoobertException.validateEventCommand(parts);
        description = parts[0].trim();
        String[] timeParts = parts[1].split("/to", 2); // Split /from part into from and to
        DoobertException.validateEventCommand(timeParts);
        from = timeParts[0].trim();
        to = timeParts[1].trim();
    }

    /**
     * Executes the command by adding a new event task to the task list.
     * It then saves the updated list and displays a confirmation message.
     *
     * @param tasks   The list of tasks to which the new event will be added.
     * @param ui      The user interface for displaying messages.
     * @param storage The storage handler for saving the updated task list.
     * @throws DoobertException If there is an issue with parsing event time.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws DoobertException {
        Event eventTask = new Event(description, from, to);
        tasks.addTask(eventTask);
        storage.saveTask(tasks);
        ui.showLine();
        ui.showOutput("Got it. I've added this task:\n" + "   "
                + eventTask + "\n   Now you have " + tasks.getList().size()
                + " tasks in the list.");
        ui.showLine();
    }
}
