package doobert;

/**
 * Represents a Todo task.
 * A Todo task is a simple task that only contains a description and a completion status.
 */
public class Todo extends Task {

    /**
     * Constructs a Todo task with the given description.
     *
     * @param description The description of the Todo task.
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Factory method to create a new Todo task.
     *
     * @param description The description of the Todo task.
     * @return A new Todo task instance.
     */
    public static Todo createTodo(String description) {
        return new Todo(description);
    }

    /**
     * Returns the string representation of the Todo task for user display.
     *
     * @return The formatted string representation of the Todo task.
     */
    @Override
    public String toString() {
        return "[T] " + super.toString();
    }

    /**
     * Returns a string representation of the Todo task formatted for file storage.
     *
     * @return A formatted string representation of the Todo task for saving to a file.
     */
    @Override
    public String toFileString() {
        return "    T | " + (isDone ? "1" : "0") + " | " + description;
    }
}
