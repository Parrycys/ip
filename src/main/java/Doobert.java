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
            writer.print("Your input: "); // Prompt for input
            writer.flush();
            String input = reader.readLine();
            Task task = new Task(input);
                if (input.equals("list")) {
                    doobert.printList();
                } else if (input.equals("blah")) {
                    writer.print("   ____________________________________________________________\n" +
                            "   blah\n" +
                            "   ____________________________________________________________\n");
                } else if (input.equals("bye")) {
                    writer.print("   ____________________________________________________________\n" +
                            "   Bye. Hope to see you again soon!\n" +
                            "   ____________________________________________________________\n");
                    exit = true;
                } else if (input.startsWith("mark")) {
                    int index = Integer.parseInt(input.split(" ")[1]) - 1;
                    doobert.markTask(index);
                } else if (input.startsWith("unmark")) {
                    int index = Integer.parseInt(input.split(" ")[1]) - 1;
                    doobert.unmarkTask(index);
                }
                 else {
                    doobert.store(input);
            }
        }



        writer.flush();
    }


    public void store(String input) {
        if (currentIndex < listOfItems.length) {
            listOfItems[currentIndex] = new Task(input);
            currentIndex++;
            System.out.println("   ____________________________________________________________\n" +
                    "   added: " + input + "\n" +
                    "   ____________________________________________________________\n");
        } else {
            System.out.println("List is full.");
        }
    }

    public void printList() {
        System.out.println("   ____________________________________________________________\n" +
                "   Here are the tasks in your list: " );
        for (int i = 0; i < currentIndex; i++) {
            Task task = listOfItems[i];
            System.out.println("   " + (i + 1) + ". [" + task.getStatusIcon() + "] " + task);
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
