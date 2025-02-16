package doobert;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

/**
 * Represents an event task with a start time ("from") and an end time ("to").
 * This class extends {@code Task} and includes date-time validation.
 */
public class Event extends Task {
    private static final DateTimeFormatter FORMATTER_1 = DateTimeFormatter.ofPattern("yyyy-M-d Hmm");
    private static final DateTimeFormatter FORMATTER_2 = DateTimeFormatter.ofPattern("MMM dd yyyy HHmm");
    private static final DateTimeFormatter FORMATTER_3 = DateTimeFormatter.ofPattern("yyyy/M/d Hmm");


    protected LocalDateTime fromDateTime;
    protected LocalDateTime toDateTime;

    /**
     * Constructs an {@code Event} object with a description and a time range.
     *
     * @param description The description of the event.
     * @param from        The start date and time in a supported format.
     * @param to          The end time (only HHmm format is accepted).
     * @throws DoobertException If the date/time format is invalid or if {@code from} is after {@code to}.
     */
    public Event(String description, String from, String to) throws DoobertException {
        super(description);

        assert description != null && !description.trim().isEmpty() : "Event description cannot be empty or null!";
        assert from != null && to != null : "Event times cannot be null.";

        this.fromDateTime = parseDateTime(from);
        this.toDateTime = parseToTime(to, fromDateTime);

        DoobertException.validateEventTime(this.fromDateTime, this.toDateTime);
    }

    /**
     * Converts "2pm" or "4AM" to a LocalTime in 24-hour format.
     *
     * @param time The time string.
     * @return A LocalTime object in 24-hour format.
     * @throws IllegalArgumentException If parsing fails.
     */
    private LocalTime parseTimeOnly(String time) {
        // ✅ Ensure AM/PM is uppercase for parsing
        time = time.trim().toUpperCase();

        // ✅ Ensure "2pm" becomes "02PM" for proper parsing
        if (time.matches("\\d{1}[apAP][mM]")) {
            time = "0" + time; // Convert "2PM" -> "02PM"
        }

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hha", Locale.ENGLISH);

        try {
            return LocalTime.parse(time, timeFormatter); // ✅ Ensures correct parsing
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid time format: Use '2pm', '11AM', etc.");
        }
    }


    /**
     * Attempts to parse a date string into LocalDateTime using predefined formatters.
     *
     * @param date The date string to be parsed.
     * @return A valid LocalDateTime instance.
     * @throws IllegalArgumentException If parsing fails.
     */
    private LocalDateTime parseDateTime(String date) {
        DateTimeFormatter[] formatters = { FORMATTER_1, FORMATTER_2, FORMATTER_3 };

        for (DateTimeFormatter formatter : formatters) {
            try {
                return LocalDateTime.parse(date, formatter);
            } catch (DateTimeParseException ignored) {}
        }

        // Handle "2pm" or "4AM" (assumes today)
        if (date.matches("\\d{1,2}[apAP][mM]")) {
            return LocalDateTime.now().with(parseTimeOnly(date));
        }

        // Handle "Mon 2pm" (Weekday + Time)
        String[] parts = date.split(" ");
        if (parts.length == 2) {
            String dayPart = parts[0];  // "Mon"
            String timePart = parts[1]; // "2pm"

            if (timePart.matches("\\d{1,2}[apAP][mM]")) {
                return parseWeekdayTime(dayPart, timePart); // ✅ FIX: Correct method call
            }
        }

        throw new IllegalArgumentException("ERROR: Failed to parse event date/time -> " + date);
    }


    /**
     * Parses the end time ("to") and applies it to the event's date.
     *
     * @param to The time string (e.g., "4pm", "14:00").
     * @param fromDateTime The start date-time object.
     * @return The end time as LocalDateTime.
     */
    private LocalDateTime parseToTime(String to, LocalDateTime fromDateTime) {
        assert to != null && !to.trim().isEmpty() : "Event end time cannot be null or empty.";

        // Handle "4pm" format
        if (to.matches("\\d{1,2}[apAP][mM]")) {
            LocalTime endTime = parseTimeOnly(to);
            return LocalDateTime.of(fromDateTime.toLocalDate(), endTime);
        }

        // Convert "1400" format
        if (to.length() == 3) {
            to = "0" + to;
        }
        int hour = Integer.parseInt(to.substring(0, 2));
        int minute = Integer.parseInt(to.substring(2));

        return fromDateTime.withHour(hour).withMinute(minute);
    }


    /**
     * Finds the next occurrence of a given day of the week.
     *
     * @param targetDay The target day (e.g. MONDAY).
     * @return The LocalDate of the next occurrence of that day.
     */
    private LocalDate getNextOccurrence(DayOfWeek targetDay) {
        LocalDate today = LocalDate.now();
        int daysUntilNext = (targetDay.getValue() - today.getDayOfWeek().getValue() + 7) % 7;
        return today.plusDays(daysUntilNext == 0 ? 7 : daysUntilNext);
    }


    /**
     * Converts "Mon" or "Monday" to a `DayOfWeek` enum.
     *
     * @param day The day name in "Mon" or "Monday" format.
     * @return The corresponding DayOfWeek.
     * @throws IllegalArgumentException If the day name is invalid.
     */
    private DayOfWeek getDayOfWeek(String day) {
        for (DayOfWeek d : DayOfWeek.values()) {
            // Support both "Mon" and "Monday"
            if (d.getDisplayName(TextStyle.SHORT, Locale.ENGLISH).equalsIgnoreCase(day) ||
                    d.getDisplayName(TextStyle.FULL, Locale.ENGLISH).equalsIgnoreCase(day)) {
                return d;
            }
        }
        throw new IllegalArgumentException("Invalid weekday name: Use 'Mon', 'Tue', etc.");
    }


    /**
     * Parses a weekday + time string (e.g., "Mon 2pm") into LocalDateTime.
     *
     * @param day The weekday (e.g., "Mon", "Friday").
     * @param time The time in "2pm" or "11AM" format.
     * @return LocalDateTime of the next occurrence of that weekday at the given time.
     */
    private LocalDateTime parseWeekdayTime(String day, String time) {
        DayOfWeek targetDay = getDayOfWeek(day);
        LocalDate nextDay = getNextOccurrence(targetDay);
        LocalTime eventTime = parseTimeOnly(time);

        return LocalDateTime.of(nextDay, eventTime);
    }


    /**
     * Returns a string representation of the event task for display.
     *
     * @return A formatted string with event details.
     */
    @Override
    public String toString() {
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("MMM dd yyyy, h:mm a");
        return "[E] [" + getStatusIcon() + "] " + description + " (from: "
                + fromDateTime.format(outputFormatter) + " - "
                + toDateTime.format(DateTimeFormatter.ofPattern("h:mm a")) + ")";
    }

    /**
     * Returns a string representation of the event task formatted for file storage.
     *
     * @return A string formatted for saving content to a file.
     */
    @Override
    public String toFileString() {
        DateTimeFormatter fileFormatterWithDate = DateTimeFormatter.ofPattern("MMM dd yyyy HHmm");
        DateTimeFormatter fileFormatterTimeOnly = DateTimeFormatter.ofPattern("HHmm");

        return "    E | " + (isDone ? "1" : "0") + " | " + description + " | "
                + fromDateTime.format(fileFormatterWithDate) + " - "
                + toDateTime.format(fileFormatterTimeOnly);
    }


}
