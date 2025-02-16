package doobert;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Represents a command to view tasks scheduled for a specific date.
 */
public class ViewScheduleCommand extends Command {
    private LocalDate date;

    /**
     * Constructs a {@code ViewScheduleCommand} with the given date argument.
     *
     * @param arguments The date input as a string.
     * @throws DoobertException If the date format is invalid.
     */
    public ViewScheduleCommand(String arguments) throws DoobertException {
        this.date = parseDate(arguments);
    }

    /**
     * Parses a date string into a LocalDate, allowing multiple formats.
     *
     * @param dateString The date string entered by the user.
     * @return The parsed LocalDate.
     * @throws DoobertException If the date format is invalid.
     */
    private LocalDate parseDate(String dateString) throws DoobertException {
        DateTimeFormatter[] formatters = {
                DateTimeFormatter.ofPattern("dd-MM-yyyy"),
                DateTimeFormatter.ofPattern("dd/MM/yyyy")
        };
        DateTimeParseException lastException = null;
        for (DateTimeFormatter formatter : formatters) {
            try {
                return LocalDate.parse(dateString.trim(), formatter);
            } catch (DateTimeParseException e) {
                lastException = e;
            }
        }
        throw new DoobertException("Invalid date format! Use dd-MM-yyyy or dd/MM/yyyy "
                + "(e.g., 20-02-2025 or 20/02/2025).", lastException);
    }

    /**
     * Executes the command to display tasks scheduled for the given date.
     *
     * @param tasks   The task list to check for scheduled tasks.
     * @param ui      The user interface for displaying messages.
     * @param storage The storage handler.
     * @return The formatted schedule for the given date.
     */
    @Override
    public String execute(TaskList tasks, Ui ui, Storage storage) {
        return tasks.viewSchedule(date);
    }
}
