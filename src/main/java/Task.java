import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;


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

    // Converts task to savable string format
    public abstract String toFileString();

    // Converts file string back to Task (Static Factory Method)
    public static Task fromFileString(String fileString) {
        String[] parts = fileString.split("\\s*\\|\\s*"); // Fix: Properly split parts

        if (parts.length < 3) { // Ensure minimum parts exist
            throw new IllegalArgumentException("Invalid file format: " + fileString);
        }

        String type = parts[0].trim();
        boolean isDone = parts[1].trim().equals("1");
        String description = parts[2].trim();

        switch (type) {
            case "T": // Todo Task
                Todo todo = new Todo(description);
                if (isDone) todo.markAsDone();
                return todo;
            case "D": // Deadline Task
                Deadline deadline;
                if (parts.length < 4) {
                    throw new IllegalArgumentException("Invalid Deadline format: " + fileString);
                }
                String by = parts[3].trim();

                try {
                    DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("MMM dd yyyy HHmm");
                    DateTimeFormatter inputFormatterForDateOnly = DateTimeFormatter.ofPattern("MMM dd yyyy");
                    DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("d/M/uuuu HHmm");
                    DateTimeFormatter outputFormatterDateOnly = DateTimeFormatter.ofPattern("yyyy-M-d");

                    String convertedBy;

                    if (by.matches("[A-Za-z]{3} \\d{2} \\d{4} \\d{4}")) {
                        // Convert from "MMM dd yyyy HHmm" to "d/M/yyyy HHmm"
                        // For debugging System.out.println("MATCHES!");
                        LocalDateTime parsedDateTime = LocalDateTime.parse(by, inputFormatter);
                        convertedBy = parsedDateTime.format(outputFormatter);
                        deadline = new Deadline(description, convertedBy);
                    } else if (by.matches("\\d{1,2}/\\d{1,2}/\\d{4} \\d{3,4}")) {
                        // Already in "d/M/yyyy HHmm", use as is
                        deadline = new Deadline(description, by);
                    } else if (by.matches("[A-Za-z]{3} \\d{1,2} \\d{4}")) {
                        // Convert from "MMM dd yyyy" to "yyyy-M-d"
                        LocalDate parsedDateTime = LocalDate.parse(by, inputFormatterForDateOnly);
                        convertedBy = parsedDateTime.format(outputFormatterDateOnly);
                        deadline = new Deadline(description, convertedBy);
                    } else if (by.matches("\\d{4}-\\d{1,2}-\\d{1,2}")) {
                        deadline = new Deadline(description, by);
                    }
                    else {
                        throw new IllegalArgumentException("Invalid Deadline format: " + fileString);
                    }


                    if (isDone) deadline.markAsDone();
                    return deadline;
                } catch (DateTimeParseException e) {
                    throw new IllegalArgumentException("Error parsing deadline: " + by);
                }
            case "E": // Event Task (Handles both "Mon 2pm-4pm" and "yyyy-MM-dd HHmm")
                if (parts.length < 4) {
                    throw new IllegalArgumentException("Invalid Event format: " + fileString);
                }
                String duration = parts[3].trim();
                if (duration.endsWith("-")) {
                    duration = duration.substring(0, duration.length() - 1);
                }
                int lastDashIndex = duration.lastIndexOf("-");

                String from = duration.substring(0, lastDashIndex).trim(); // Extract everything before last "-"
                String to = duration.substring(lastDashIndex + 1).trim();  // Extract everything after last "-"

                // Define formatters
                DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
                DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("MMM dd yyyy HHmm");

                // Convert "yyyy-MM-dd HHmm" to "MMM dd yyyy HHmm" if needed
                if (from.matches("\\d{4}-\\d{2}-\\d{2} \\d{4}")) {
                    String[] splitFrom = from.split(" ");
                    String datePart = splitFrom[0].trim();
                    String timePart = splitFrom[1].trim();
                    // Ensure time is always four digits (e.g., "800" -> "0800")
                    if (timePart.length() == 3) {
                        timePart = "0" + timePart;
                    }
                    LocalDateTime parsedFrom = LocalDateTime.parse(datePart + " " + timePart, inputFormatter);
                    from = parsedFrom.format(outputFormatter);
                }
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
