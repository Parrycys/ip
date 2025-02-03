package doobert;

import java.util.List;

public class FindCommand extends Command {

    private String keyword;

    /**
     * Constructs a FindCommand with the given keyword.
     *
     * @param keyword The keyword to search for in task descriptions.
     */
    public FindCommand(String keyword) {
        this.keyword = keyword.trim().toLowerCase();
    }

    /**
     * Executes the find command, searching for tasks that contains the keyword.
     *
     * @param tasks   The task list to search within.
     * @param ui      The user interface to display messages.
     * @param storage The storage system (not used in this command).
     * @throws DoobertException If no tasks match the keyword.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws DoobertException {
        List<Task> taskList = tasks.getList();
        boolean found = false;

        ui.showLine();
        ui.showOutput("Here are the matching tasks in your list:");

        // Iterate through the task list and check for matching descriptions
        int count = 1;
        for (Task task : taskList) {
            if (task.description.toLowerCase().contains(keyword)) {
                ui.showOutput(count + ". " + task);
                found = true;
                count++;
            }
        }

        // If no matches found, inform the user
        if (!found) {
            ui.showOutput("No matching tasks found.");
        }

        ui.showLine();
    }
}
