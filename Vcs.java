import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.Stack;

class Version {
    public int id;
    public String contents;

    public Version(int id, String contents) {
        this.id = id;
        this.contents = contents;
    }
}

class Node {
    public Version data;
    public Node next;

    public Node(Version data) {
        this.data = data;
        this.next = null;
    }
}

class VersionLinkedList {
    private Node head;

    public boolean isEmpty() {
        return head == null;
    }

    public void add(Version version) {
        Node newNode = new Node(version);

        if (head == null) {
            head = newNode;
            return;
        }

        Node current = head;
        while (current.next != null) {
            current = current.next;
        }
        current.next = newNode;
    }

    public Version getLast() {
        if (head == null) {
            return null;
        }

        Node current = head;
        while (current.next != null) {
            current = current.next;
        }
        return current.data;
    }

    public void displayHistory() {
        if (head == null) {
            System.out.println("No saved versions yet!");
            return;
        }

        Node current = head;
        while (current != null) {
            System.out.println("Version ID: " + current.data.id);
            current = current.next;
        }
    }

    public Version findById(int id) {
        Node current = head;

        while (current != null) {
            if (current.data.id == id) {
                return current.data;
            }
            current = current.next;
        }

        return null;
    }
}

public class Vcs {

    static String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    static void writeFile(String path, String contents, Charset encoding) throws IOException {
        Files.write(Paths.get(path), contents.getBytes(encoding));
    }

    public Vcs() {
        Scanner scanner = new Scanner(System.in);

        VersionLinkedList history = new VersionLinkedList();
        Stack<Version> undoStack = new Stack<>();
        Stack<Version> redoStack = new Stack<>();

        final int[] nextHistoryId = {0};

        System.out.println("Welcome to the file watcher! What file would you like to watch?");
        String filePath = scanner.nextLine();

        System.out.println("OK! Here are some commands:");
        System.out.println("history: Displays file history");
        System.out.println("restore: Reverts the file to a point in history");
        System.out.println("undo: Reverts to the previous version");
        System.out.println("redo: Reapplies an undone version");
        System.out.println("exit: Closes the program");

        Thread watcher = new Thread(() -> {
            while (true) {
                try {
                    String contents = readFile(filePath, Charset.defaultCharset());

                    synchronized (history) {
                        if (history.isEmpty()) {
                            Version newVersion = new Version(nextHistoryId[0], contents);
                            history.add(newVersion);
                            undoStack.push(newVersion);
                            redoStack.clear();
                            System.out.println("Saved version " + nextHistoryId[0]);
                            nextHistoryId[0]++;
                        } else if (!contents.equals(history.getLast().contents)) {
                            Version newVersion = new Version(nextHistoryId[0], contents);
                            history.add(newVersion);
                            undoStack.push(newVersion);
                            redoStack.clear();
                            System.out.println("Saved version " + nextHistoryId[0]);
                            nextHistoryId[0]++;
                        }
                    }

                    Thread.sleep(1000);
                } catch (IOException e) {
                    System.out.println("Your file does not exist!");
                    break;
                } catch (InterruptedException e) {
                    break;
                }
            }
        });

        watcher.setDaemon(true);
        watcher.start();

        while (true) {
            String input = scanner.nextLine().toLowerCase();

            switch (input) {
                case "history":
                    synchronized (history) {
                        history.displayHistory();
                    }
                    break;

                case "restore":
                    synchronized (history) {
                        if (history.isEmpty()) {
                            System.out.println("No saved versions available to restore!");
                            break;
                        }
                    }

                    System.out.println("Which version would you like to restore? (Enter the version ID)");
                    String restoreInput = scanner.nextLine();

                    try {
                        int restoreId = Integer.parseInt(restoreInput);

                        synchronized (history) {
                            Version restoredVersion = history.findById(restoreId);

                            if (restoredVersion != null) {
                                writeFile(filePath, restoredVersion.contents, Charset.defaultCharset());
                                undoStack.push(restoredVersion);
                                redoStack.clear();
                                System.out.println("Version " + restoreId + " restored successfully!");
                            } else {
                                System.out.println("Version " + restoreId + " not found!");
                            }
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid version ID! Please enter a valid number.");
                    } catch (IOException e) {
                        System.out.println("An error occurred while restoring the file.");
                    }
                    break;

                case "undo":
                    if (undoStack.size() <= 1) {
                        System.out.println("Nothing to undo!");
                        break;
                    }

                    try {
                        Version currentVersion = undoStack.pop();
                        redoStack.push(currentVersion);

                        Version previousVersion = undoStack.peek();
                        writeFile(filePath, previousVersion.contents, Charset.defaultCharset());

                        System.out.println("Undo successful! Restored version " + previousVersion.id);
                    } catch (IOException e) {
                        System.out.println("An error occurred while undoing the file change.");
                    }
                    break;

                case "redo":
                    if (redoStack.empty()) {
                        System.out.println("Nothing to redo!");
                        break;
                    }

                    try {
                        Version redoVersion = redoStack.pop();
                        undoStack.push(redoVersion);
                        writeFile(filePath, redoVersion.contents, Charset.defaultCharset());

                        System.out.println("Redo successful! Restored version " + redoVersion.id);
                    } catch (IOException e) {
                        System.out.println("An error occurred while redoing the file change.");
                    }
                    break;

                case "exit":
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid command! Please try again!");
            }
        }
    }

    public static void main(String[] args) {
        new Vcs();
    }
}
