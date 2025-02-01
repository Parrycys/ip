package doobert;

public abstract class Command {

    public abstract void execute(TaskList tasks, Ui ui, Storage storage) throws DoobertException;

    public boolean isExit() {
        return false;
    }
}
