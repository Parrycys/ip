package doobert;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Storage {
    private String filePath;

    public Storage(String filePath) {
        this.filePath = filePath;
    }

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
