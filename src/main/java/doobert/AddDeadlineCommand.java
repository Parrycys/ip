package doobert;

import java.util.Arrays;

public class AddDeadlineCommand extends Command {
    private String description;
    private String by;


    public AddDeadlineCommand(String arguments) throws DoobertException {
        String[] parts = arguments.split("/by", 2);
        DoobertException.validateDeadlineCommand(parts);
        this.description = parts[0].trim();
        this.by = parts[1].trim();
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        Deadline deadline = new Deadline(description, by);
        tasks.addTask(deadline);
        storage.saveTask(tasks);
        ui.showLine();
        ui.showOutput("Got it. I've added this task:\n" + "   " + deadline +
                "\n   Now you have " + tasks.getList().size() + " tasks in the list.");
        ui.showLine();
    }
}
