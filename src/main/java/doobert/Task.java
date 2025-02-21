package doobert;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;


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
            return "Nice! I've marked this task as done: \n" + "   [X] " + description;
        }
        return "    This task is already marked as done.\n";
    }

    public String markAsUndone() {
        if (isDone) {
            isDone = false;
            return "OK, I've marked this task as not done yet: \n" + "   [ ] " + description;
        }
        return "    This task is already marked as not done.\n";
    }

    public String getDescription() {
        return description;
    }

    // Converts task to savable string format
    public abstract String toFileString();


    /**
     * Parses a task from a formatted file string and returns the corresponding Task object.
     *
     * @param fileString The formatted string representing a task in the file.
     * @return The Task object (Todo, Deadline, or Event) based on the parsed data.
     * @throws DoobertException If the event task contains an invalid time range.
     * @throws IllegalArgumentException If the file format is invalid.
     */
    public static Task fromFileString(String fileString) throws DoobertException {
        String[] parts = fileString.split("\\s*\\|\\s*");

        if (parts.length < 3) {
            throw new IllegalArgumentException("Invalid file format: " + fileString);
        }

        String type = parts[0].trim();
        boolean isDone = parts[1].trim().equals("1");
        String description = parts[2].trim();

        switch (type) {
        case "T": return createTodo(description, isDone);
        case "D": return createDeadline(parts, description, isDone, fileString);
        case "E": return createEvent(parts, description, isDone, fileString);
        default: throw new IllegalArgumentException("Unknown task type in file: " + type);
        }
    }

    /**
     * Creates a Todo task from a given description and completion status.
     *
     * @param description The task description.
     * @param isDone Whether the task is marked as completed.
     * @return A Todo object.
     */
    private static Todo createTodo(String description, boolean isDone) {
        Todo todo = new Todo(description);
        if (isDone) todo.markAsDone();
        return todo;
    }

    /**
     * Creates a Deadline task from file data.
     *
     * @param parts The parsed parts of the file string.
     * @param description The task description.
     * @param isDone Whether the task is marked as completed.
     * @param fileString The original file string for error reporting.
     * @return A Deadline object.
     * @throws IllegalArgumentException If the deadline format is invalid.
     */
    private static Deadline createDeadline(String[] parts, String description, boolean isDone, String fileString) {
        if (parts.length < 4) {
            throw new IllegalArgumentException("Invalid Deadline format: " + fileString);
        }
        String by = parts[3].trim();

        try {
            Deadline deadline = new Deadline(description, convertDeadlineFormat(by));
            if (isDone) deadline.markAsDone();
            return deadline;
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Error parsing deadline: " + by);
        }
    }

    /**
     * Converts various deadline formats into a standardized format.
     *
     * @param by The deadline string in different formats.
     * @return The standardized deadline format.
     * @throws IllegalArgumentException If the format is unknown or invalid.
     */
    private static String convertDeadlineFormat(String by) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("MMM dd yyyy HHmm");
        DateTimeFormatter inputFormatterForDateOnly = DateTimeFormatter.ofPattern("MMM dd yyyy");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("d/M/uuuu HHmm");
        DateTimeFormatter outputFormatterDateOnly = DateTimeFormatter.ofPattern("yyyy-M-d");

        if (by.matches("[A-Za-z]{3} \\d{2} \\d{4} \\d{4}")) {
            // Convert from "MMM dd yyyy HHmm" to "d/M/yyyy HHmm"
            return LocalDateTime.parse(by, inputFormatter).format(outputFormatter);
        } else if (by.matches("\\d{1,2}/\\d{1,2}/\\d{4} \\d{3,4}")) {
            // Already in "d/M/yyyy HHmm", use as is
            return by;
        } else if (by.matches("[A-Za-z]{3} \\d{1,2} \\d{4}")) {
            // Convert from "MMM dd yyyy" to "yyyy-M-d"
            return LocalDate.parse(by, inputFormatterForDateOnly).format(outputFormatterDateOnly);
        } else if (by.matches("\\d{4}-\\d{1,2}-\\d{1,2}")) {
            return by;
        }
        throw new IllegalArgumentException("Unknown deadline format: " + by);
    }

    /**
     * Creates an Event task from file data.
     *
     * @param parts The parsed parts of the file string.
     * @param description The task description.
     * @param isDone Whether the task is marked as completed.
     * @param fileString The original file string for error reporting.
     * @return An Event object.
     * @throws DoobertException If the event time range is invalid.
     * @throws IllegalArgumentException If the event format is invalid.
     */
    private static Event createEvent(String[] parts, String description, boolean isDone, String fileString)
            throws DoobertException {
        if (parts.length < 4) {
            throw new IllegalArgumentException("Invalid Event format: " + fileString);
        }

        String[] timeParts = parts[3].trim().split("-");
        if (timeParts.length != 2) {
            throw new IllegalArgumentException("Invalid Event format (missing '-'): " + fileString);
        }

        String from = timeParts[0].trim().replaceAll("\\s+", " ");
        String to = timeParts[1].trim().replaceAll("\\s+", "");

        try {
            DateTimeFormatter fileFormatterWithDate = DateTimeFormatter.ofPattern("MMM dd yyyy HHmm");
            DateTimeFormatter outputFormatterTimeOnly = DateTimeFormatter.ofPattern("HHmm");

            LocalDateTime fromDateTime = LocalDateTime.parse(from, fileFormatterWithDate);

            if (to.length() == 3) {
                to = "0" + to; // Convert "500" -> "0500"
            }
            LocalDateTime toDateTime = fromDateTime.withHour(Integer.parseInt(to.substring(0, 2)))
                    .withMinute(Integer.parseInt(to.substring(2)));

            if (fromDateTime.isAfter(toDateTime)) {
                throw new DoobertException("Invalid event time: The start time ('from') cannot be "
                        + "after the end time ('to').");
            }

            Event event = new Event(description, fromDateTime.format(fileFormatterWithDate),
                    toDateTime.format(outputFormatterTimeOnly));
            if (isDone) event.markAsDone();
            return event;
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Error parsing event: " + fileString + " -> " + e.getMessage());
        }
    }




    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
}
