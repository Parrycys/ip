package doobert;
import java.util.List;

/**
 * Represents a command that lists all tasks stored in the task list.
 */
public class ListCommand extends Command {

    /**
     * Executes the list command, returning all tasks currently stored in the task list.
     *
     * @param tasks   The task list containing all the tasks.
     * @param ui      The UI instance (not used in JavaFX mode).
     * @param storage The storage instance (not used in this command).
     * @return A formatted string containing all tasks or a message if the list is empty.
     */
    @Override
    public String execute(TaskList tasks, Ui ui, Storage storage) {
        List<Task> list = tasks.getList();

        if (list.isEmpty()) {
            return "Your task list is empty!";
        }

        // Build the response string
        StringBuilder response = new StringBuilder("Here are the tasks in your list:\n");
        for (int i = 0; i < list.size(); i++) {
            response.append((i + 1)).append(". ").append(list.get(i)).append("\n");
        }

        return response.toString();
    }
}
