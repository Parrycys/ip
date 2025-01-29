public abstract class Task {
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

    // Converts task to savable string format
    public abstract String toFileString();

    // Converts file string back to Task (Static Factory Method)
    public static Task fromFileString(String fileString) {
        String[] parts = fileString.split("\\s*[|\\-]\\s*");
        String type = parts[0];
        boolean isDone = parts[1].equals("1");
        String description = parts[2];

        switch (type) {
            case "T":
                Todo todo = new Todo(description);
                if (isDone) todo.markAsDone();
                return todo;
            case "D":
                String by = parts[3];
                Deadline deadline = new Deadline(description, by);
                if (isDone) deadline.markAsDone();
                return deadline;
            case "E":
                String from = parts[3];
                String to = parts[4];
                Event event = new Event(description, from, to);
                if (isDone) event.markAsDone();
                return event;
            default:
                throw new IllegalArgumentException("Unknown task type in file: " + type);
        }
    }

    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
}
