package doobert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.mockito.Mockito.*;
import java.util.List;

public class AddEventCommandTest {
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
            "project meeting /from 2019-12-22 1400 /to 1600",
            "meeting /from 2019-1-2 500 /to 700",
            "groceries /from 2019/1/2 1400 /to 1600",
            "assignment /from 2019/12/22 500 /to 700",
    })
    public void execute_validInput_success(String input) throws DoobertException {
        TaskList taskList = new TaskList();
        Ui ui = mock(Ui.class);
        Storage storage = mock(Storage.class);

        AddEventCommand command = new AddEventCommand(input);
        command.execute(taskList, ui, storage);

        List<Task> tasks = taskList.getList();
        assertEquals(1, tasks.size()); // Verify task was added

        // Determine expected output dynamically
        String expectedOutput;
        if (input.contains("2019-12-22")) {
            expectedOutput = "[E] [ ] project meeting (from: Dec 22 2019, 2:00 pm - 4:00 pm)";
        } else if (input.contains("2019-1-2")) {
            expectedOutput = "[E] [ ] meeting (from: Jan 02 2019, 5:00 am - 7:00 am)";
        } else if (input.contains("2019/1/2")) {
            expectedOutput = "[E] [ ] groceries (from: Jan 02 2019, 2:00 pm - 4:00 pm)";
        } else if (input.contains("2019/12/22")) {
            expectedOutput = "[E] [ ] assignment (from: Dec 22 2019, 5:00 am - 7:00 am)";
        }else {
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
            "event project /by 2/12/201 1800", // Invalid year format
            "event project meeting /by 2/22/2019 1800", // Invalid month format
            "event Return Book /by 32/4/2019 1800", // Invalid day format
            "event Meeting /by 13:00 2025-12-01", // Wrong time placement
    })
    public void execute_invalidDate_parameterized_exceptionThrown(String input) throws DoobertException {
        try {
            TaskList taskList = new TaskList();
            Ui ui = mock(Ui.class);
            Storage storage = mock(Storage.class);

            // Invalid deadline addition
            System.out.println(input);
            AddEventCommand command = new AddEventCommand(input);
            command.execute(taskList, ui, storage);
            fail();
            // the test should not reach this line
        } catch (DoobertException e) {
            assertEquals("Invalid event format: Use <description> /from <time format of:> 'd/M/yyyy HHmm',"
                    + " 'yyyy-MM-dd', "
                    + "or a weekday name like 'Sunday'. /to <end>", e.getMessage());
        }
    }

    @Test
    public void constructor_invalidFromToTime_exceptionThrown() throws DoobertException {
        try {
            TaskList taskList = new TaskList();
            Ui ui = mock(Ui.class);
            Storage storage = mock(Storage.class);
            // From time is after to time (Invalid format)
            AddEventCommand command = new AddEventCommand("event project meeting /from "
                    + "2019-12-22 1600 /to 1300");
            command.execute(taskList, ui, storage);
            fail(); // Debugging: If reached, no exception was thrown

        } catch (DoobertException e) {
            assertEquals("Invalid event time: The start time ('from') cannot be after the end time ('to')."
                    , e.getMessage());
        }
    }

    @Test
    public void constructor_missingDescription_exceptionThrown() {
        try {
            // No description before "/by" (invalid format)
            new AddEventCommand("event /by 22/12/2019 1800");
            fail(); // Debugging: If reached, no exception was thrown

        } catch (DoobertException e) {
            assertEquals("Invalid event format: Use <description> /from <time format of:> 'd/M/yyyy HHmm', "
                    + "'yyyy-MM-dd', "
                    + "or a weekday name like 'Sunday'. /to <end>", e.getMessage());
        }
    }
}

