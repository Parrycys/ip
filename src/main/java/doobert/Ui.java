package doobert;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * Handles user interactions
 * The {@code Ui} class is responsible for displaying messages, reading user input,
 * and showing errors in the console.
 */
public class Ui {
    PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)));
    private Scanner scanner;

    /**
     * Constructs a {@code Ui} object and initializes the input scanner.
     */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Displays the welcome message when the program starts.
     */
    public void showWelcome() {
        writer.print("""
                   ____________________________________________________________
                    Hello! I'm Doobert
                    What can I do for you?
                   ____________________________________________________________
                """);
        writer.flush();
    }

    /**
     * Reads a command input from the user.
     * If there is no input available, returns a default "bye" command to prevent errors.
     *
     * @return The user command as a string.
     */
    public String readCommand() {
        if (!scanner.hasNextLine()) { // Prevents NoSuchElementException
            return "bye"; // Default safe command to exit in testing
        }
        return scanner.nextLine();
    }

    /**
     * Displays a message when there is an issue loading tasks from the saved file.
     */
    public void showLoadingError() {
            System.out.println("   ____________________________________________________________");
            System.out.println("    Loading tasks from saved file...");
            System.out.println("    No saved file found! Creating one just for you.");
    }


    /**
     * Displays a standard output message to the user.
     *
     * @param message The message to be displayed.
     */
    public void showOutput(String message) {
        System.out.println("   " + message);
    }

    /**
     * Displays an error message to the user.
     *
     * @param message The error message to be displayed.
     */
    public void showError(String message) {
        System.out.println("   ____________________________________________________________");
        System.out.println("   Error: " + message);
        System.out.println("   ____________________________________________________________");
    }

}
