import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Deadline extends Task {
    protected LocalDateTime byDateTime;
    protected LocalDate byDate; // Store separately for date-only formats

    public Deadline(String description, String by) {
        super(description);

        // Define formatters
        DateTimeFormatter formatterWithTime = DateTimeFormatter.ofPattern("d/M/uuuu HHmm"); // e.g., 2/12/2019 1800
        DateTimeFormatter formatterDateOnly = DateTimeFormatter.ofPattern("yyyy-M-d"); // e.g., 2019-10-15

        try {
            // If input contains both date & time
            if (by.matches("\\d+/\\d+/\\d+ \\d+")) {
                String[] parts = by.split(" ");
                String datePart = parts[0];
                String timePart = parts[1];

                // Ensure time is always four digits (e.g., "800" -> "0800")
                if (timePart.length() == 3) {
                    timePart = "0" + timePart;
                }

                this.byDateTime = LocalDateTime.parse(datePart + " " + timePart, formatterWithTime);
            } else {
                // If input is date-only
                this.byDate = LocalDate.parse(by, formatterDateOnly);
            }
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid deadline format: '" + by + "'. Use 'd/M/yyyy HHmm' or 'yyyy-MM-dd'");
        }
    }

    @Override
    public String toString() {
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("MMM dd yyyy");

        if (byDateTime != null) {
            // If date and time exist, format with time
            DateTimeFormatter outputFormatterWithTime = DateTimeFormatter.ofPattern("MMM dd yyyy, h:mm a");
            return "[D] [" + getStatusIcon() + "] " + description + " (by: " + byDateTime.format(outputFormatterWithTime) + ")";
        } else {
            // If only date exists, format date only
            return "[D] [" + getStatusIcon() + "] " + description + " (by: " + byDate.format(outputFormatter) + ")";
        }
    }

    @Override
    public String toFileString() {
        DateTimeFormatter fileFormatterWithTime = DateTimeFormatter.ofPattern("MMM dd yyyy HHmm"); // Fixed: Ensures 4-digit time
        DateTimeFormatter fileFormatterDateOnly = DateTimeFormatter.ofPattern("MMM dd yyyy"); // Standardized date format

        if (byDateTime != null) {
            return "    D | " + (isDone ? "1" : "0") + " | " + description + " | " + byDateTime.format(fileFormatterWithTime);
        } else {
            return "    D | " + (isDone ? "1" : "0") + " | " + description + " | " + byDate.format(fileFormatterDateOnly);
        }
    }
}
