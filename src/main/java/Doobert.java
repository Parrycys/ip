import java.util.*;
import java.io.*;

public class Doobert {
    public static void main(String[] arg) throws IOException {
        // For
        boolean exit = false;
        enum Message {
            list,
            blah,
            bye
        }

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
            try {
                Message message = Message.valueOf(input);
                switch (message) {
                    case list:
                        writer.print("   ____________________________________________________________\n" +
                                "   list\n" +
                                "   ____________________________________________________________\n");
                        break;
                    case blah:
                        writer.print("   ____________________________________________________________\n" +
                                "   blah\n" +
                                "   ____________________________________________________________\n");
                        break;
                    case bye:

                        writer.print("   ____________________________________________________________\n" +
                                "   Bye. Hope to see you again soon!\n" +
                                "   ____________________________________________________________\n");
                        exit = true;
                        break;
                }
            } catch (IllegalArgumentException e) {
                writer.print("   ____________________________________________________________\n" +
                        "   I didn't understand what you just typed. Please type again.\n" +
                        "   ____________________________________________________________\n");
            }
        }



        writer.flush();
    }

}
