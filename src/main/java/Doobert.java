import java.util.*;
import java.io.*;

public abstract class Doobert {
    public abstract void store(String input);
    public abstract String printList();

    public static void main(String[] arg) throws IOException {
        Doobert doobert = new Storage();
        boolean exit = false;

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)));

        writer.print("   ____________________________________________________________\n" +
                "    Hello! I'm Doobert\n" +
                "    What can I do for you?\n" +
                "   ____________________________________________________________\n");
        writer.flush();


        while(!exit) {
            writer.print("Your input: "); // Prompt for input
            writer.flush();
            String input = reader.readLine();
                if (input.equals("list")) {
                    writer.print("   ____________________________________________________________\n" +
                            doobert.printList() +
                            "   ____________________________________________________________\n");
                } else if (input.equals("blah")) {
                    writer.print("   ____________________________________________________________\n" +
                            "   blah\n" +
                            "   ____________________________________________________________\n");
                } else if (input.equals("bye")) {
                    writer.print("   ____________________________________________________________\n" +
                            "   Bye. Hope to see you again soon!\n" +
                            "   ____________________________________________________________\n");
                    exit = true;
                } else {
                    doobert.store(input);
            }
        }



        writer.flush();
    }

}
