public class AddTodoCommand extends Command {

    private String description;

    public AddTodoCommand(String description) {
        this.description = description;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws DoobertException  {
        DoobertException.validateTodoDescription(description);
        Todo todoTask = new Todo(description);
        tasks.addTask(todoTask);
        storage.saveTask(tasks);
        ui.showLine();
        ui.showOutput("Got it. I've added this task: \n" + "      " + todoTask.toString() + "\n" +
                "   Now you have " + tasks.getList().size() + " tasks in your list.");
        ui.showLine();
    }

}
