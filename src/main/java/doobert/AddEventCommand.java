package doobert;

public class AddEventCommand extends Command {

    private String description;
    private String from;
    private String to;

    public AddEventCommand(String arguments) throws DoobertException{
        String[] parts = arguments.split("/from", 2); // Split at "/from" into description + time
        DoobertException.validateEventCommand(parts);
        description = parts[0].trim();
        String[] timeParts = parts[1].split("/to", 2); // Split /from part into from and to
        DoobertException.validateEventCommand(timeParts);
        from = timeParts[0].trim();
        to = timeParts[1].trim();
    }


    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws DoobertException {
        Event eventTask = new Event(description, from, to);
        tasks.addTask(eventTask);
        storage.saveTask(tasks);
        ui.showLine();
        ui.showOutput("Got it. I've added this task:\n" + "   " + eventTask +
                "\n   Now you have " + tasks.getList().size() + " tasks in the list.");
        ui.showLine();
    }
}
