package doobert;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Represents an event task with a start time ("from") and an end time ("to").
 * This class extends {@code Task} and includes date-time validation.
 */
public class Event extends Task {
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

        // Define formatters
        DateTimeFormatter inputFormatter1 = DateTimeFormatter.ofPattern("yyyy-M-d Hmm");  // e.g., 2019-1-2 0500
        DateTimeFormatter inputFormatter2 = DateTimeFormatter.ofPattern("MMM dd yyyy HHmm"); // e.g., Jan 02 2019 1400
        DateTimeFormatter inputFormatter3 = DateTimeFormatter.ofPattern("yyyy/M/d Hmm");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("MMM dd yyyy HHmm"); // Standard output format

        try {
            System.out.println("DEBUG: Creating Event with:");
            System.out.println(" - description -> " + description);
            System.out.println(" - from -> " + from);
            System.out.println(" - to -> " + to);

            // Try parsing with the first format
            try {
                this.fromDateTime = LocalDateTime.parse(from, inputFormatter1);
            } catch (DateTimeParseException e1) {
                // If inputFormatter1 fails, try inputFormatter2
                try {
                    this.fromDateTime = LocalDateTime.parse(from, inputFormatter2);
                } catch (DateTimeParseException e2) {
                    // If inputFormatter2 fails, try inputFormatter3
                    this.fromDateTime = LocalDateTime.parse(from, inputFormatter3);
                }
            }

            // Ensure `to` is 4-digit format (e.g., "500" -> "0500")
            if (to.length() == 3) {
                to = "0" + to;
            }
            int hour = Integer.parseInt(to.substring(0, 2));
            int minute = Integer.parseInt(to.substring(2));

            this.toDateTime = this.fromDateTime.withHour(hour).withMinute(minute);
            DoobertException.validateEventTime(this.fromDateTime, this.toDateTime);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("ERROR: Failed to parse Event date/time -> " + e.getMessage());
        }
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
