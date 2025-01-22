public class Task {
    protected String description;
    protected boolean isDone;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public String getStatusIcon() {
        return (isDone ? "X" : " "); // mark done task with X
    }

    public String markAsDone() {
        if (!isDone) {
            isDone = true;
            return "   Nice! I've marked this task as done: \n" + "   [X] " + description + "\n";
        }
        return "    This task is already marked as done.\n";
    }

    public String markAsUndone() {
        if (isDone) {
            isDone = false;
            return "   OK, I've marked this task as not done yet: \n" + "   [ ] " + description + "\n";
        }
        return "    This task is already marked as not done.\n";
    }

    @Override
    public String toString() {
        return description; // Return the task description
    }
}
