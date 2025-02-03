package doobert;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DoobertException extends Exception {
    public DoobertException(String message) {
        super(message);
    }

    public static void validateMarkCommand(int index, int taskListSize) throws DoobertException {
        if (index < 0 || index >= taskListSize) {
            throw new DoobertException("Invalid task number. No such task exists.");
        }
    }

    public static void validateUnmarkCommand(int index, int taskListSize) throws DoobertException {
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
            //System.out.println("DEBUG: Throwing missing /by or deadline error");
            throw new DoobertException("Invalid deadline task format. Use: deadline <description> /by <deadline>");
        }

        // NEW: Ensure the description is not just empty spaces
        if (parts[0].trim().isEmpty() || parts[0].trim().equalsIgnoreCase("deadline")) {
            //System.out.println("DEBUG: Throwing missing description error");
            throw new DoobertException("Invalid deadline task format. Use: deadline <description> /by <deadline>");
        }

        // System.out.println("DEBUG: Validation passed!");
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

    public static void validateDeleteCommand(int index, int taskListSize) throws DoobertException {
        if (index < 0 || index >= taskListSize) {
            throw new DoobertException("Invalid task number. No such task exists.");
        }
    }

    public static void fileNotFound(File filePath) throws DoobertException {
        throw new DoobertException(filePath + " File not found.");
    }
}
