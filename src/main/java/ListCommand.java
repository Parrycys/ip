import java.util.List;

public class ListCommand extends Command {
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showLine();
        ui.showOutput("Here are the tasks in your list:");
        List<Task> list = tasks.getList();
        for (int i = 0; i < list.size(); i++) {
            ui.showOutput((i + 1) + ". " + list.get(i));
        }
        ui.showLine();
    }
}
