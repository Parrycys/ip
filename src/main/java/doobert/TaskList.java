package doobert;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a list of tasks. Provides methods to add, delete, mark, and unmark tasks.
 */
public class TaskList {
    private List<Task> listOfItems = new ArrayList<>();

    /**
     * Constructs a TaskList with a given list of tasks.
     *
     * @param listOfItems The list of tasks to initialize the TaskList.
     */
    public TaskList(List<Task> listOfItems) {
        this.listOfItems = listOfItems;
    }

    /**
     * Constructs an empty TaskList.
     */
    public TaskList() {
        this.listOfItems = new ArrayList<>();
    }

    /**
     * Retrieves the list of tasks.
     *
     * @return The list of tasks.
     */
    public List<Task> getList() {
        return listOfItems;
    }

    /**
     * Adds a task to the task list.
     *
     * @param task The task to be added.
     */
    public void addTask(Task task) {
        listOfItems.add(task);
    }

    /**
     * Deletes a task from the task list at a specified index.
     *
     * @param index The index of the task to delete (zero-based index).
     * @return The updated list of tasks after deletion.
     */
    public List<Task> deleteTask(int index) {
        if (index >= 0 && index < listOfItems.size()) {
            Task removedTask = listOfItems.remove(index); // Remove task from the list
        } else {
            System.out.println("Invalid task number to delete.");
        }
        return listOfItems;
    }

    /**
     * Marks a task as done.
     *
     * @param index The index of the task to mark as done (zero-based index).
     */
    public void markTask(int index) {
        boolean isValidIndex = index >= 0 && index < listOfItems.size();
        if (!isValidIndex) {
            System.out.println("Invalid task number.");
            return;
        }
        listOfItems.get(index).markAsDone();
    }


    /**
     * Unmarks a task (marks it as not done).
     *
     * @param index The index of the task to unmark (zero-based index).
     */
    public void unmarkTask(int index) {
        boolean isValidIndex = index >= 0 && index < listOfItems.size();
        if (!isValidIndex) {
            System.out.println("Invalid task number.");
            return;
        }
        listOfItems.get(index).markAsUndone();
    }

}
