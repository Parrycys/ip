package doobert;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a list of tasks. Provides methods to add, delete, mark, and unmark tasks.
 */
public class TaskList {
    private List<Task> listOfItems = new ArrayList<>();
    private static final DateTimeFormatter OUTPUT_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

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
            Task removedTask = listOfItems.remove(index);
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

    /**
     * Returns a formatted list of tasks scheduled for the given date.
     *
     * @param date The date for which the schedule should be displayed.
     * @return A formatted string containing scheduled tasks.
     */
    public String viewSchedule(LocalDate date) {
        List<Task> scheduledTasks = listOfItems.stream()
                .filter(task -> task instanceof Event || task instanceof Deadline)
                .filter(task -> taskFallsOnDate(task, date))
                .collect(Collectors.toList());

        if (scheduledTasks.isEmpty()) {
            return "No tasks scheduled for " + date.format(OUTPUT_FORMATTER);
        }

        StringBuilder output = new StringBuilder("Schedule for " + date.format(OUTPUT_FORMATTER) + ":\n");
        for (int i = 0; i < scheduledTasks.size(); i++) {
            output.append(i + 1).append(". ").append(scheduledTasks.get(i)).append("\n");
        }
        return output.toString();
    }

    /**
     * Checks if a task falls on a specific date.
     *
     * @param task The task to check.
     * @param date The date to match.
     * @return true if the task occurs on the given date.
     */
    private boolean taskFallsOnDate(Task task, LocalDate date) {
        if (task instanceof Event) {
            Event event = (Event) task;
            return event.fromDateTime.toLocalDate().equals(date);
        } else if (task instanceof Deadline) {
            Deadline deadline = (Deadline) task;
            return (deadline.byDateTime != null && deadline.byDateTime.toLocalDate().equals(date)) ||
                    (deadline.byDate != null && deadline.byDate.equals(date));
        }
        return false;
    }

}
