package doobert;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Scanner;

public class Ui {
    PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)));
    private Scanner scanner;

    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    public void showWelcome() {
        writer.print("   ____________________________________________________________\n" +
                "    Hello! I'm Doobert\n" +
                "    What can I do for you?\n" +
                "   ____________________________________________________________\n");
        writer.flush();
    }

    public String readCommand() {
        if (!scanner.hasNextLine()) { // ✅ Prevents NoSuchElementException
            return "bye"; // Default safe command to exit in testing
        }
        return scanner.nextLine();
    }

    public void showLoadingError() {
            System.out.println("   ____________________________________________________________");
            System.out.println("    Loading tasks from saved file...");
            System.out.println("    No saved file found! Creating one just for you.");
            /*for (Task task : listOfItems) {
                System.out.println(task.toFileString());
            }*/
    }

    public void showLine() {
        System.out.println("   ____________________________________________________________");
    }

    public void showOutput(String message) {
        System.out.println("   " + message);
    }

    public void showError(String message) {
        System.out.println("   ____________________________________________________________");
        System.out.println("   Error: " + message);
        System.out.println("   ____________________________________________________________");
    }

}
