import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.Stack;

class Version {
    public int id = 0;
    public String contents;

    public Version(int id, String contents) {
        this.contents = contents;
        this.id = id;
    }
}

/**
 * This class creates a loop that permanently watches for file changes and saves them in memory.
 * It also allows users to revert the file to a previous version
 */
public class Vcs {

    static String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    void sleep(float seconds) {
        try {
            wait((long) (seconds * 1000)); //convert to ms
        } catch (InterruptedException e) {
            //don't care.
        }
    }
    public Vcs() {
        Scanner scanner = new Scanner(System.in);
        int required_cycles_for_file_query = 60; //will only check the file every second.
        float poll_interval = 1 / 60; //60 polls per second to feel smoother.
        int cycles_since_last_file_query = 0;

        int next_history_id = 0;
        Stack<Version> history = new Stack<>();

        System.out.println("Welcome to the file watcher! What file would you like to watch?");

        String filePath = scanner.nextLine();

        System.out.println("OK! Here are some commands:\nhistory: Displays file history\nrestore: Reverts the file to a point in history");

        for (;;) {
            sleep(poll_interval);
            
            if (cycles_since_last_file_query >= required_cycles_for_file_query) {

                try {
                    String contents = readFile(filePath, Charset.defaultCharset());

                    if (history.empty()) {
                        history.add(new Version(next_history_id, contents));
                        next_history_id ++;
                    }
                    else if (!contents.equals(history.lastElement().contents)) {
                        history.add(new Version(next_history_id, contents));
                        next_history_id ++;
                    }
                    //if it is neither of these 2 cases, then it hasn't changed and we don't need to add it.
                }
                catch (IOException e) {
                    System.out.println("Your file does not exist!");
                    break;
                }

                cycles_since_last_file_query = 0;
            }
            else {
                cycles_since_last_file_query ++;
            }

            String input = scanner.nextLine().toLowerCase();

            switch (input) {
                case "history":
                    
                    break;
                case "restore":

                    break;

                default:
                    System.out.println("Invalid command! Please try again!");
            }
        }

        scanner.close();
    }
}