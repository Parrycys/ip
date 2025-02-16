package doobert;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@code Storage} class handles loading tasks from a file and saving tasks to a file.
 */
public class Storage {
    private final String FILE_PATH;

    /**
     * Constructs a {@code Storage} object with the specified file path.
     *
     * @param filePath The path to the file where tasks are stored.
     */
    public Storage(String filePath) {
        assert filePath != null : "File path should not be null.";
        this.FILE_PATH = filePath;

    }

    /**
     * Loads tasks from the file into a list.
     *
     * @return A list of tasks retrieved from the file.
     * @throws DoobertException If the file is not found or cannot be read.
     */
    public List<Task> loadTasks() throws DoobertException {
        List<Task> listOfItems = new ArrayList<>();
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            throw new DoobertException("No previous tasks found.");
        }

        StringBuilder response = new StringBuilder("Loaded tasks from file:\n");

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    Task task = Task.fromFileString(line.trim());
                    listOfItems.add(task);
                    response.append(task).append("\n"); // Append task to response
                } catch (IllegalArgumentException e) {
                    System.out.println("Skipping invalid task format: " + line);
                }
            }
        } catch (IOException e) {
            throw new DoobertException("Error loading tasks: " + e.getMessage());
        }

        // Return formatted string of loaded tasks
        return listOfItems;
    }


    /**
     * Saves the current task list to the file.
     *
     * @param taskList The {@code TaskList} contains the tasks to be saved.
     */
    public void saveTask(TaskList taskList) {
        try {
            File file = new File(FILE_PATH);
            file.getParentFile().mkdirs();
            PrintWriter writer = new PrintWriter(new FileWriter(file));
            List<Task> listOfItems = taskList.getList();
            for (Task task : listOfItems) {
                writer.println(task.toFileString());
            }
            writer.close();
            assert file.exists() : "Error: File doobert.txt was not created!";
        } catch (IOException e) {
            System.out.println("Error saving tasks: " + e.getMessage());
        }
    }


}
