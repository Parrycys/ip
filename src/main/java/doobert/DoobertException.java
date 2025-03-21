package doobert;

import java.time.LocalDateTime;


public class DoobertException extends Exception {
    public DoobertException(String message) {
        super(message);
    }

    public DoobertException(String message, Throwable cause) {
        super(message, cause);
    }

    public static void validateTaskIndex(int index, int taskListSize) throws DoobertException {
        if (index < 0 || index >= taskListSize) {
            throw new DoobertException("Invalid task number. No such task exists.");
        }
    }

    public static void validateTodoDescription(String description) throws DoobertException {
        if (description == null || description.trim().isEmpty()) {
            throw new DoobertException("The description of a todo cannot be empty. Use: todo <description>");
        }
    }

    public static void validateDeadlineCommand(String[] parts) throws DoobertException {
        // Ensure "/by" and deadline exist
        if (parts.length < 2 || parts[1].trim().isEmpty()) {
            throw new DoobertException("Invalid deadline task format. Use: deadline <description> /by <deadline>");
        }

        if (parts[0].trim().isEmpty() || parts[0].trim().equalsIgnoreCase("deadline")) {
            throw new DoobertException("Invalid deadline task format. Use: deadline <description> /by <deadline>");
        }
    }


    public static void validateEventCommand(String[] timeParts) throws DoobertException {
        // Check if the description is empty
        if (timeParts[0].trim().isEmpty() || timeParts[0].trim().equalsIgnoreCase("event")) {
            throw new DoobertException("Invalid event format: Description cannot be empty. " +
                    "Use 'event <description> /from <start> /to <end>'.");
        }

        if (timeParts.length != 2 || timeParts[1].trim().isEmpty()) {
            throw new DoobertException("Invalid event format: Use <description> /from <time format of:> '"
                    + "d/M/yyyy HHmm', 'yyyy-MM-dd', "
                    + "or a weekday name like 'Sunday'. /to <end>");
        }


    }

    public static void validateEventTime(LocalDateTime from, LocalDateTime to) throws DoobertException {
        if (from.isAfter(to)) {
            throw new DoobertException("Invalid event time: The start time ('from') cannot be after "
                    + "the end time ('to').");
        }
    }

}
