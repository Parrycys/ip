import java.util.*;
import java.io.*;

public class Doobert {
    private Task[] listOfItems = new Task[100];
    private int currentIndex = 0;

    public static void main(String[] arg) throws IOException {
        boolean exit = false;
        Doobert doobert = new Doobert();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)));

        writer.print("   ____________________________________________________________\n" +
                "    Hello! I'm Doobert\n" +
                "    What can I do for you?\n" +
                "   ____________________________________________________________\n");
        writer.flush();


        while(!exit) {
            try {
                String input = reader.readLine();
                Task task = new Task(input);
                if (input.equals("list")) {
                    doobert.printList();
                } else if (input.equals("blah")) {
                    throw new DoobertException("Sorry, I do not understand that.");
                } else if (input.equals("bye")) {
                    writer.print("   ____________________________________________________________\n" +
                            "   Bye. Hope to see you again soon!\n" +
                            "   ____________________________________________________________\n");
                    exit = true;
                } else if (input.startsWith("mark")) {
                    String[] parts = input.split(" ");
                    DoobertException.validateMarkCommand(parts);
                    int index = Integer.parseInt(input.split(" ")[1]) - 1;
                    doobert.markTask(index);
                } else if (input.startsWith("unmark")) {
                    String[] parts = input.split(" ");
                    DoobertException.validateUnmarkCommand(parts);
                    int index = Integer.parseInt(input.split(" ")[1]) - 1;
                    doobert.unmarkTask(index);
                } else if (input.startsWith("todo")) {
                    String description = input.substring(4).trim();
                    DoobertException.validateTodoDescription(description);
                    Todo todoTask = new Todo(description);
                    doobert.store(todoTask, description);
                } else if (input.startsWith("deadline")) {
                    String[] fullDesc = input.substring(8).split("/by");
                    DoobertException.validateDeadlineCommand(fullDesc);
                    String description = fullDesc[0].trim(); // Extract description
                    String by = fullDesc[1].trim(); // Extract by (deadline)
                    Deadline deadlineTask = new Deadline(description, by);
                    doobert.store(deadlineTask, description);
                } else if (input.startsWith("event")) {
                    String[] parts = input.substring(5).split("/from");
                    if (parts.length != 2) {
                        DoobertException.validateEventCommand(parts);
                    }
                    if (parts.length == 2) {
                        String description = parts[0].trim(); // Extract description
                        String[] timeParts = parts[1].split(" /to "); // Split /from part into from and to
                        if (timeParts.length == 2) {
                            String from = timeParts[0].trim(); // Extract from time
                            String to = timeParts[1].trim(); // Extract to time
                            Event eventTask = new Event(description, from, to); // Create Event object
                            doobert.store(eventTask, description);
                        } else {
                            DoobertException.validateEventCommand(timeParts);
                        }
                    }
                } else {
                    doobert.store(task, input);
                }

            } catch (DoobertException e) {
                System.out.println("   ____________________________________________________________");
                System.out.println("   Error: " + e.getMessage());
                System.out.println("   ____________________________________________________________");
            }

        }
        writer.flush();
    }


    public void store(Task task, String description) {
        if (currentIndex < listOfItems.length) {
            listOfItems[currentIndex] = task ;
            currentIndex++;
            System.out.println("   ____________________________________________________________\n" +
                    "     Got it. I've added this task:\n" + "      " + task.toString() + "\n" +
            "     Now you have " + currentIndex + " tasks in your list.\n" +
                    "   ____________________________________________________________");
        } else {
            System.out.println("List is full.");
        }
    }

    public void printList() {
        System.out.println("   ____________________________________________________________\n" +
                "   Here are the tasks in your list: " );
        for (int i = 0; i < currentIndex; i++) {
            Task task = listOfItems[i];
            System.out.println("   " + (i + 1) + "." + task);
        }
        System.out.println("   ____________________________________________________________");
    }

    public void markTask(int index) {
        if (index >= 0 && index < currentIndex) {
            System.out.println("   ____________________________________________________________\n" +
                    listOfItems[index].markAsDone() +
                    "   ____________________________________________________________\n");
        } else {
            System.out.println("Invalid task number.");
        }
    }

    public void unmarkTask(int index) {
        if (index >= 0 && index < currentIndex) {
            System.out.println("   ____________________________________________________________\n" +
                    listOfItems[index].markAsUndone() +
                    "   ____________________________________________________________\n");
        } else {
            System.out.println("Invalid task number.");
        }
    }

}
