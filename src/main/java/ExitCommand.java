public class ExitCommand extends Command {
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showLine();
        ui.showOutput("Bye. Hope to see you again soon!");
        ui.showLine();
    }

    @Override
    public boolean isExit() {
        return true;
    }
}
