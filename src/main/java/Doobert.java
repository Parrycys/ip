import java.util.*;
import java.io.*;

public class Doobert {
    private static final String FILE_PATH = "./data/doobert.txt";
    private List<Task> listOfItems = new ArrayList<>();

    public Doobert() {
        try {
            loadTasks();
        } catch (DoobertException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }


    public static void main(String[] arg) throws IOException {
        boolean exit = false;
        Doobert doobert = new Doobert();
        doobert.printFormattedTaskList();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)));

        writer.print("   ____________________________________________________________\n" +
                "    Hello! I'm Doobert\n" +
                "    What can I do for you?\n" +
                "   ____________________________________________________________\n");
        writer.flush();


        while (!exit) {
            try {
                String input = reader.readLine();
                //Task task = new Task(input);
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
                    doobert.store(todoTask);
                } else if (input.startsWith("deadline")) {
                    String[] fullDesc = input.substring(8).split("/by");
                    DoobertException.validateDeadlineCommand(fullDesc);
                    String description = fullDesc[0].trim(); // Extract description
                    String by = fullDesc[1].trim(); // Extract by (deadline)
                    Deadline deadlineTask = new Deadline(description, by);
                    doobert.store(deadlineTask);
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
                            doobert.store(eventTask);
                        } else {
                            DoobertException.validateEventCommand(timeParts);
                        }
                    }
                } else if (input.startsWith("delete")) {
                    String[] parts = input.split(" ");
                    DoobertException.validateDeleteCommand(parts); // Validate delete command
                    int index = Integer.parseInt(parts[1]) - 1; // Convert to 0-based index
                    doobert.deleteTask(index);
                } else {
                    System.out.println("Invalid task type");
                }

            } catch (DoobertException e) {
                System.out.println("   ____________________________________________________________");
                System.out.println("   Error: " + e.getMessage());
                System.out.println("   ____________________________________________________________");
            }

        }
        writer.flush();
    }


    public void store(Task task) {
        listOfItems.add(task);
        saveTasks();
        System.out.println("   ____________________________________________________________\n" +
                "     Got it. I've added this task:\n" + "      " + task.toString() + "\n" +
                "     Now you have " + listOfItems.size() + " tasks in your list.\n" +
                "   ____________________________________________________________");
    }

    public void printList() {
        System.out.println("   ____________________________________________________________\n" +
                "   Here are the tasks in your list: ");
        for (int i = 0; i < listOfItems.size(); i++) {
            Task task = listOfItems.get(i);
            System.out.println("   " + (i + 1) + "." + task);
        }
        System.out.println("   ____________________________________________________________");
    }

    public void markTask(int index) {
        if (index >= 0 && index < listOfItems.size()) {
            System.out.println("   ____________________________________________________________\n" +
                    listOfItems.get(index).markAsDone() +
                    "   ____________________________________________________________\n");
        } else {
            System.out.println("Invalid task number.");
        }
        saveTasks();
    }

    public void unmarkTask(int index) {
        if (index >= 0 && index < listOfItems.size()) {
            saveTasks();
            System.out.println("   ____________________________________________________________\n" +
                    listOfItems.get(index).markAsUndone() +
                    "   ____________________________________________________________\n");

        } else {
            System.out.println("Invalid task number.");
        }
        saveTasks();
    }

    public void deleteTask(int index) {
        if (index >= 0 && index < listOfItems.size()) {
            Task removedTask = listOfItems.remove(index); // Remove task from the list
            saveTasks();
            System.out.println("   ____________________________________________________________");
            System.out.println("     Noted. I've removed this task:");
            System.out.println("     " + removedTask);
            System.out.println("     Now you have " + listOfItems.size() + " task(s) in the list.");
            System.out.println("   ____________________________________________________________");
        } else {
            System.out.println("Invalid task number to delete.");
        }
    }

    private void saveTasks() {
        try {
            File file = new File(FILE_PATH);
            file.getParentFile().mkdirs();
            PrintWriter writer = new PrintWriter(new FileWriter(file));
            for (Task task : listOfItems) {
                writer.println(task.toFileString());
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving tasks: " + e.getMessage());
        }
    }

    public void printFormattedTaskList() {
        if (listOfItems.isEmpty()) {
            System.out.println("No tasks found.");
        } else {
            System.out.println("   ____________________________________________________________");
            System.out.println("    Loading tasks from saved file...");
            for (Task task : listOfItems) {
                System.out.println(task.toFileString());
            }
        }
    }

    private void loadTasks() throws DoobertException {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            DoobertException.fileNotFound(file);
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Task task = Task.fromFileString(line.trim());
                listOfItems.add(task);
            }
        } catch (IOException e) {
            System.out.println("Error loading tasks: " + e.getMessage());
        }
    }


}
