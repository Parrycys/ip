import java.io.File;

public class DoobertException extends Exception {
    public DoobertException(String message) {
        super(message);
    }

    public static void validateMarkCommand(String[] parts) throws DoobertException {
        if (parts.length != 2 || !parts[1].matches("\\d+")) {
            throw new DoobertException("Invalid mark command. Use: mark <task_number>");
        }
    }

    public static void validateUnmarkCommand(String[] parts) throws DoobertException {
        if (parts.length != 2 || !parts[1].matches("\\d+")) {
            throw new DoobertException("Invalid unmark command. Use: unmark <task_number>");
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
        if (timeParts.length != 2) {
            throw new DoobertException("Invalid event format. Use: event <description> /from <start> /to <end>");
        }
    }

    public static void validateDeleteCommand(String[] parts) throws DoobertException {
        if (parts.length != 2 || !parts[1].matches("\\d+")) {
            throw new DoobertException("Invalid delete command. Use: delete <task_number>");
        }
    }

    public static void fileNotFound(File filePath) throws DoobertException {
        throw new DoobertException(filePath + " File not found.");
    }
}
