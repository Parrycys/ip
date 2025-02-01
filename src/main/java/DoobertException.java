import java.io.File;

public class DoobertException extends Exception {
    public DoobertException(String message) {
        super(message);
    }

    /*public static void validateMarkCommand(String[] parts) throws DoobertException {
        if (parts.length != 2 || !parts[1].matches("\\d+")) {
            throw new DoobertException("Invalid mark command. Use: mark <task_number>");
        }
    }*/
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
        if (parts.length != 2) {
            throw new DoobertException("Invalid deadline task format. Use: deadline <description> /by <deadline>");
        }
    }

    public static void validateEventCommand(String[] timeParts) throws DoobertException {
        if (timeParts.length != 2 || timeParts[0].trim().isEmpty() || timeParts[1].trim().isEmpty()) {
            throw new DoobertException("Invalid event format. Use: event <description> /from <start> /to <end>");
        }
    }


    // Factory method for delete validation
    public static void validateDeleteCommand(int index, int taskListSize) throws DoobertException {
        if (index < 0 || index >= taskListSize) {
            throw new DoobertException("Invalid task number. No such task exists.");
        }
    }

    public static void fileNotFound(File filePath) throws DoobertException {
        throw new DoobertException(filePath + " File not found.");
    }
}
