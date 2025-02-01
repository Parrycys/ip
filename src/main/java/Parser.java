public class Parser {
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

