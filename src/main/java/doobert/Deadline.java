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
    private static final DateTimeFormatter FORMATTER_WITH_TIME = DateTimeFormatter.ofPattern("d/M/uuuu HHmm");
    private static final DateTimeFormatter FORMATTER_DATE_ONLY = DateTimeFormatter.ofPattern("yyyy-M-d");
    private static final DateTimeFormatter FORMATTER_WITH_DASH = DateTimeFormatter.ofPattern("d-M-yyyy HHmm");


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

        assert description != null && !description.trim().isEmpty() : "Deadline description cannot be empty!";
        assert by != null && !by.trim().isEmpty() : "Deadline due date cannot be empty!";

        this.byDateTime = parseDateTime(by);
        if (this.byDateTime == null) {
            this.byDate = parseDate(by);
        }
    }

    /**
     * Attempts to parse a date string into LocalDateTime.
     *
     * @param by The date string to be parsed.
     * @return A valid LocalDateTime instance or null if not found.
     */
    private LocalDateTime parseDateTime(String by) {
        if (by.matches("\\d+/\\d+/\\d+ \\d+")) {
            return parseDateTimeWithTime(by, FORMATTER_WITH_TIME);
        } else if (by.matches("\\d{1,2}-\\d{1,2}-\\d{4} \\d{4}")) {
            return parseDateTimeWithTime(by, FORMATTER_WITH_DASH);
        }
        return null;
    }

    /**
     * Parses a date string containing time using the specified formatter.
     *
     * @param dateTime The date string.
     * @param formatter The formatter to use.
     * @return A parsed LocalDateTime.
     * @throws IllegalArgumentException If parsing fails.
     */
    private LocalDateTime parseDateTimeWithTime(String dateTime, DateTimeFormatter formatter) {
        try {
            String[] parts = dateTime.split(" ");
            String datePart = parts[0];
            String timePart = parts[1];

            // Ensure time is 4-digit format (e.g., "800" → "0800")
            if (timePart.length() == 3) {
                timePart = "0" + timePart;
            }

            return LocalDateTime.parse(datePart + " " + timePart, formatter);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid deadline format: Use 'd/M/yyyy HHmm', "
                    + "'yyyy-MM-dd', or a weekday name like 'Sunday'.");
        }
    }

    /**
     * Attempts to parse a date-only format or a weekday name.
     *
     * @param by The date string.
     * @return A valid LocalDate instance.
     * @throws IllegalArgumentException If parsing fails.
     */
    private LocalDate parseDate(String by) {
        if (by.matches("\\d{1,2}/\\d{1,2}/\\d{4}")) {
            return parseLocalDate(by, DateTimeFormatter.ofPattern("d/M/yyyy"));
        } else if (by.matches("\\d{4}-\\d{1,2}-\\d{1,2}")) {
            return parseLocalDate(by, FORMATTER_DATE_ONLY);
        }
        return parseNextWeekday(by);
    }

    /**
     * Parses a LocalDate from a string using the given formatter.
     *
     * @param date The date string.
     * @param formatter The formatter to use.
     * @return The parsed LocalDate.
     * @throws IllegalArgumentException If parsing fails.
     */
    private LocalDate parseLocalDate(String date, DateTimeFormatter formatter) {
        try {
            return LocalDate.parse(date, formatter);
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
    private LocalDate parseNextWeekday(String dayName) {
        try {
            DayOfWeek targetDay = DayOfWeek.valueOf(dayName.toUpperCase(Locale.ROOT)); // Convert to uppercase
            LocalDate today = LocalDate.now();
            int daysUntilNext = (targetDay.getValue() - today.getDayOfWeek().getValue() + 7) % 7;
            return today.plusDays(daysUntilNext == 0 ? 7 : daysUntilNext); // If today is the day, move to next week
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid weekday name: Use a valid weekday like 'Sunday'.");
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
