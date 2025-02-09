package doobert;

import java.util.List;
import java.util.stream.Collectors;

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
     * Executes the find command, searching for tasks that contain the keyword.
     *
     * @param tasks   The task list to search within.
     * @param ui      The user interface (not used in JavaFX mode).
     * @param storage The storage system (not used in this command).
     * @return A string representation of the found tasks or a message if no matches are found.
     */
    @Override
    public String execute(TaskList tasks, Ui ui, Storage storage) {
        List<Task> taskList = tasks.getList();

        // Filter tasks based on keyword
        List<Task> matchingTasks = taskList.stream()
                .filter(task -> task.getDescription().toLowerCase().contains(keyword))
                .collect(Collectors.toList());

        if (matchingTasks.isEmpty()) {
            return "No matching tasks found.";
        }

        // Format and return the matching tasks
        StringBuilder response = new StringBuilder("Here are the matching tasks in your list:\n");
        for (int i = 0; i < matchingTasks.size(); i++) {
            response.append((i + 1)).append(". ").append(matchingTasks.get(i)).append("\n");
        }

        return response.toString();
    }
}
