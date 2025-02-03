package doobert;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@code Storage} class handles loading tasks from a file and saving tasks to a file.
 */
public class Storage {
    private String filePath;

    /**
     * Constructs a {@code Storage} object with the specified file path.
     *
     * @param filePath The path to the file where tasks are stored.
     */
    public Storage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Loads tasks from the file into a list.
     *
     * @return A list of tasks retrieved from the file.
     * @throws DoobertException If the file is not found or cannot be read.
     */
    public List<Task> loadTasks() throws DoobertException {
        List<Task> listOfItems = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) {
            DoobertException.fileNotFound(file);
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    Task task = Task.fromFileString(line.trim());
                    listOfItems.add(task);
                    System.out.println("DEBUG: Added task to list!");
                } catch (IllegalArgumentException e) {
                    System.out.println("Skipping invalid task format: " + line);
                }
            }
            System.out.println("   ____________________________________________________________");
            System.out.println("    Loading tasks from saved file...");
            for (Task task : listOfItems) {
                System.out.println(task.toFileString());
            }
        } catch (IOException e) {
            System.out.println("Error loading tasks: " + e.getMessage());
        }
        return listOfItems;
    }

    /**
     * Saves the current task list to the file.
     *
     * @param taskList The {@code TaskList} contains the tasks to be saved.
     */
    public void saveTask(TaskList taskList) {
        try {
            File file = new File(filePath);
            file.getParentFile().mkdirs();
            PrintWriter writer = new PrintWriter(new FileWriter(file));
            List<Task> listOfItems = taskList.getList();
            for (Task task : listOfItems) {
                writer.println(task.toFileString());
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving tasks: " + e.getMessage());
        }
    }


}
