package doobert;

import javafx.application.Platform;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

/**
 * Represents a command to exit the chatbot.
 */
public class ExitCommand extends Command {

    /**
     * Executes the bye command, closing the application after displaying a message.
     *
     * @param tasks   The task list (not used here).
     * @param ui      The UI instance (not used in JavaFX mode).
     * @param storage The storage instance (not used here).
     * @return A farewell message before exiting.
     */
    @Override
    public String execute(TaskList tasks, Ui ui, Storage storage) {
        // Schedule JavaFX to exit after a short delay
        PauseTransition delay = new PauseTransition(Duration.seconds(2)); // 2-seconds delay
        delay.setOnFinished(event -> Platform.exit()); // Exit JavaFX after delay
        delay.play();

        return "Goodbye! Hope to see you again soon!";
    }

    @Override
    public boolean isExit() {
        return true;
    }
}
