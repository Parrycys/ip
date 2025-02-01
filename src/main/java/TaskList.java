import java.util.ArrayList;
import java.util.List;

public class TaskList {
    private List<Task> listOfItems = new ArrayList<>();

    public TaskList(List<Task> listOfItems) {
        this.listOfItems = listOfItems;
    }

    public TaskList() {
        this.listOfItems = new ArrayList<>();
    }

    public List<Task> getList() {
        return listOfItems;
    }

    public void addTask(Task task) {
        listOfItems.add(task);
    }

    public List<Task> deleteTask(int index) {
        if (index >= 0 && index < listOfItems.size()) {
            Task removedTask = listOfItems.remove(index); // Remove task from the list
        } else {
            System.out.println("Invalid task number to delete.");
        }
        return listOfItems;
    }

    public void markTask(int index) {
        if (index >= 0 && index < listOfItems.size()) {
            listOfItems.get(index).markAsDone();
        } else {
            System.out.println("Invalid task number.");
        }
    }

    public void unmarkTask(int index) {
        if (index >= 0 && index < listOfItems.size()) {
            listOfItems.get(index).markAsUndone();
        } else {
            System.out.println("Invalid task number.");
        }
    }
}
