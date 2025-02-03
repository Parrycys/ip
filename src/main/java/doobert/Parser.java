package doobert;

/**
 * The {@code Parser} class is responsible for interpreting user input and
 * returning the corresponding command object.
 */
public class Parser {

    /**
     * Parses the user input and returns the corresponding command.
     *
     * @param input The full user input as a string.
     * @return A {@code Command} object representing the parsed action.
     * @throws DoobertException If the input is invalid or does not match a known command.
     */
    public Command parse(String input) throws DoobertException {
        String[] parts = input.split(" ", 2);
        String commandWord = parts[0].trim();
        String arguments = parts.length > 1 ? parts[1].trim() : "";

        switch (commandWord) {
        case "bye":
            return new ExitCommand();
        case "list":
            return new ListCommand();
        case "todo":
            return new AddTodoCommand(arguments);
        case "deadline":
            return new AddDeadlineCommand(arguments);
        case "event":
            return new AddEventCommand(arguments);
        case "delete":
            return new DeleteCommand(arguments);
        case "mark":
            return new MarkCommand(arguments);
        case "unmark":
            return new UnmarkCommand(arguments);
        default:
            throw new DoobertException("Sorry, I do not understand that.");
        }
    }
}

