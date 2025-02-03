package doobert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.mockito.Mockito.*;
import java.util.List;


public class AddDeadlineCommandTest {
    private TaskList taskList;
    private Ui ui;
    private Storage storage;

    @BeforeEach
    public void setUp() {
        taskList = new TaskList();
        ui = mock(Ui.class); // Mock UI to capture printed output
        storage = mock(Storage.class); // Mock Storage to avoid actual file writing
    }
    @ParameterizedTest
    @ValueSource(strings = {
            "Meeting /by 2/12/2019 1800",
            "Project /by 22-12-2025 1400",
            "Meeting /by 2/2/2012 200",
            "return book /by 2019-1-1",
            "buy book /by 2019-10-15",
    })
    public void execute_validInput_success(String input) throws DoobertException {
        TaskList taskList = new TaskList();
        Ui ui = mock(Ui.class);
        Storage storage = mock(Storage.class);

        // Valid deadline addition
        AddDeadlineCommand command = new AddDeadlineCommand(input);
        command.execute(taskList, ui, storage);

        List<Task> tasks = taskList.getList();
        assertEquals(1, tasks.size()); // Verify task was added

        // Determine expected output dynamically
        String expectedOutput;
        if (input.contains("2/12/2019 1800")) {
            expectedOutput = "[D] [ ] Meeting (by: Dec 02 2019, 6:00 pm)";
        } else if (input.contains("22-12-2025 1400")) {
            expectedOutput = "[D] [ ] Project (by: Dec 22 2025, 2:00 pm)";
        } else if (input.contains("2/2/2012 200")) {
            expectedOutput = "[D] [ ] Meeting (by: Feb 02 2012, 2:00 am)";
        } else if (input.contains("2019-1-1")) {
            expectedOutput = "[D] [ ] return book (by: Jan 01 2019)";
        } else if (input.contains("2019-10-15")) {
            expectedOutput = "[D] [ ] buy book (by: Oct 15 2019)";
        } else {
            fail("Unexpected input encountered in parameterized test.");
            return;
        }
        assertEquals(expectedOutput, tasks.get(0).toString());

        // Verify storage and UI interactions
        verify(storage).saveTask(taskList);
        verify(ui, times(2)).showLine();
        verify(ui).showOutput(contains("Got it. I've added this task:"));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "deadline Return Book /by 2/12/201 1800", // Invalid year format
            "deadline Return Book /by 2/22/2019 1800", // Invalid month format
            "deadline Return Book /by 32/4/2019 1800", // Invalid day format
            "deadline Meeting /by 13:00 2025-12-01", // Wrong time placement
    })
    public void execute_invalidDate_parameterized_exceptionThrown(String input) throws DoobertException {
        try {
            TaskList taskList = new TaskList();
            Ui ui = mock(Ui.class);
            Storage storage = mock(Storage.class);

            // Invalid deadline addition
            System.out.println(input);
            AddDeadlineCommand command = new AddDeadlineCommand(input);
            command.execute(taskList, ui, storage);
            fail();
            // the test should not reach this line
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid deadline format: Use 'd/M/yyyy HHmm', 'yyyy-MM-dd', "
                    + "or a weekday name like 'Sunday'.", e.getMessage());
        }
    }

    @Test
    public void constructor_missingDescription_exceptionThrown() {
        try {
            // No description before "/by" (invalid format)
            new AddDeadlineCommand("deadline /by 22/12/2019 1800");
            fail(); // Debugging: If reached, no exception was thrown

        } catch (DoobertException e) {
            assertEquals("Invalid deadline task format. Use: deadline <description> /by "
                    + "<deadline>", e.getMessage());
        }
    }

    @Test
    public void constructor_missingSlash_exceptionThrown() {
        try {
            // No "/" after description (invalid format)
            new AddDeadlineCommand("deadline Meeting by 22/12/2019 1800");
            fail(); // Debugging: If reached, no exception was thrown

        } catch (DoobertException e) {
            assertEquals("Invalid deadline task format. Use: deadline <description> /by "
                    + "<deadline>", e.getMessage());
        }
    }


}
