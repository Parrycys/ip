package doobert;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

/**
 * Represents a deadline task with a specific due date and optional time.
 * The due date can be in various formats including:
 * - "d/M/yyyy HHmm" (e.g., "2/12/2024 1800")
 * - "yyyy-M-d" (e.g., "2024-12-02")
 * - A weekday name (e.g., "Sunday" → next occurrence)
 */
public class Deadline extends Task {
    protected LocalDateTime byDateTime;
    protected LocalDate byDate;

    /**
     * Constructs a new {@code Deadline} task with a given description and due date.
     *
     * @param description The task description.
     * @param by          The due date in one of the accepted formats.
     * @throws IllegalArgumentException If the date format is invalid.
     */
    public Deadline(String description, String by) {
        super(description);

        // Define formatters
        DateTimeFormatter formatterWithTime = DateTimeFormatter.ofPattern("d/M/uuuu HHmm");
        DateTimeFormatter formatterDateOnly = DateTimeFormatter.ofPattern("yyyy-M-d");
        DateTimeFormatter formatterWithTimeForDash = DateTimeFormatter.ofPattern("d-M-yyyy HHmm");

        try {
            // Handle full date + time
            if (by.matches("\\d+/\\d+/\\d+ \\d+")) {
                String[] parts = by.split(" ");
                String datePart = parts[0];
                String timePart = parts[1];

                if (timePart.length() == 3) { // Ensure 4-digit time (e.g., 800 → 0800)
                    timePart = "0" + timePart;
                }

                this.byDateTime = LocalDateTime.parse(datePart + " " + timePart, formatterWithTime);
            } else if (by.matches("\\d{1,2}-\\d{1,2}-\\d{4} \\d{4}")) {
                    try {
                        this.byDateTime = LocalDateTime.parse(by, formatterWithTimeForDash);
                    } catch (DateTimeParseException e) {
                        throw new IllegalArgumentException("Invalid deadline format: Use 'd/M/yyyy HHmm', "
                                + "'yyyy-MM-dd', " + "or a weekday name like 'Sunday'.");
                    }
                }
            // Handle numeric date-only input
            else if (by.matches("\\d{4}-\\d{1,2}-\\d{1,2}")) {
                this.byDate = LocalDate.parse(by, formatterDateOnly);
            }
            // Handle weekday names like "Sunday"
            else {
                this.byDate = getNextDayOfWeek(by);
            }
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid deadline format: Use 'd/M/yyyy HHmm', 'yyyy-MM-dd', "
                    + "or a weekday name like 'Sunday'.");
        }
    }

    /**
     * Converts a weekday name (e.g., "Sunday") to the next occurrence of that day.
     *
     * @param dayName The name of the weekday.
     * @return The {@code LocalDate} representing the next occurrence of the given weekday.
     * @throws IllegalArgumentException If the provided day name is not valid.
     */
    private LocalDate getNextDayOfWeek(String dayName) {
        try {
            DayOfWeek targetDay = DayOfWeek.valueOf(dayName.toUpperCase(Locale.ROOT)); // Convert to uppercase
            LocalDate today = LocalDate.now();
            int daysUntilNext = (targetDay.getValue() - today.getDayOfWeek().getValue() + 7) % 7;
            return today.plusDays(daysUntilNext == 0 ? 7 : daysUntilNext); // If today is the day, move to next week
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid deadline format: Use 'd/M/yyyy HHmm', 'yyyy-MM-dd', "
                    + "or a weekday name like 'Sunday'.");
        }
    }

    /**
     * Returns a string representation of the deadline task.
     *
     * @return The formatted deadline task as a string.
     */
    @Override
    public String toString() {
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("MMM dd yyyy");

        if (byDateTime != null) {
            DateTimeFormatter outputFormatterWithTime = DateTimeFormatter.ofPattern("MMM dd yyyy, h:mm a");
            return "[D] [" + getStatusIcon() + "] " + description + " (by: "
                    + byDateTime.format(outputFormatterWithTime) + ")";
        } else {
            return "[D] [" + getStatusIcon() + "] " + description + " (by: "
                    + byDate.format(outputFormatter) + ")";
        }
    }

    /**
     * Returns a string representation of the deadline task formatted for file storage.
     *
     * @return A formatted string that can be saved in a file.
     */
    @Override
    public String toFileString() {
        DateTimeFormatter fileFormatterWithTime = DateTimeFormatter.ofPattern("MMM dd yyyy HHmm");
        DateTimeFormatter fileFormatterDateOnly = DateTimeFormatter.ofPattern("MMM dd yyyy");

        if (byDateTime != null) {
            return "    D | " + (isDone ? "1" : "0") + " | " + description + " | "
                    + byDateTime.format(fileFormatterWithTime);
        } else {
            return "    D | " + (isDone ? "1" : "0") + " | " + description + " | "
                    + byDate.format(fileFormatterDateOnly);
        }
    }
}
