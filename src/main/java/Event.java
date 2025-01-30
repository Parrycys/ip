import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Event extends Task {
    protected String from;
    protected String to;
    protected LocalDateTime fromDateTime;
    protected LocalDate fromDate; // Store separately for date-only formats

    public Event(String description, String from, String to) {
        super(description);

        // Define formatters
        DateTimeFormatter formatterWithTime = DateTimeFormatter.ofPattern("d/M/uuuu HHmm"); // e.g., 2/12/2019 1800
        DateTimeFormatter formatterDateOnly = DateTimeFormatter.ofPattern("yyyy-M-d"); // e.g., 2019-10-15
        DateTimeFormatter formatterEvent = DateTimeFormatter.ofPattern("MMM dd yyyy HHmm"); // e.g., Dec 22 2019 1400

        try {
            if (from.matches("\\d{4}-\\d{2}-\\d{2} \\d{3,4}")) {
                // Convert "yyyy-MM-dd HHmm" → "MMM dd yyyy HHmm"
                System.out.println(from);
                DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
                LocalDateTime parsedDateTime = LocalDateTime.parse(from, inputFormatter);
                this.from = parsedDateTime.format(formatterEvent);
            } else if (from.matches("[A-Za-z]{3} \\d{2} \\d{4} \\d{3,4}")) {
                // Already in "MMM dd yyyy HHmm" format, keep it as is
                this.from = from;
            } else {
                // Assume "Mon 2pm" format and store as-is
                this.from = from;
            }

            // Process 'to' time
            if (to.matches("\\d{3,4}")) {
                // Ensure time is always four digits (e.g., "800" -> "0800")
                if (to.length() == 3) {
                    to = "0" + to;
                }
            }
            this.to = to;
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid event format: '" + from + "'. Use 'Mon 2pm' or 'yyyy-MM-dd HHmm'");
        }
    }

    @Override
    public String toString() {
        return "[E] [" + getStatusIcon() + "] " + description + " (from: " + from + "-" + to + ")";
    }

    @Override
    public String toFileString() {
        return "    E | " + (isDone ? "1" : "0") + " | " + description + " | " + from + "-" + to;
    }
}
