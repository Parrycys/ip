package doobert;
import java.util.List;

/**
 * Represents a command that lists all tasks stored in the task list.
 */
public class ListCommand extends Command {

    /**
     * Executes the list command, displaying all tasks currently stored in the task list.
     *
     * @param tasks   The task list containing all the tasks.
     * @param ui      The UI instance used to interact with the user.
     * @param storage The storage instance (not used in this command).
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showLine();
        ui.showOutput("Here are the tasks in your list:");
        List<Task> list = tasks.getList();
        for (int i = 0; i < list.size(); i++) {
            ui.showOutput((i + 1) + ". " + list.get(i));
        }
        ui.showLine();
    }
}
