# Version Control System (Linked List & Stack)

A Java-based **Version Control System (VCS)** that tracks file changes, stores multiple versions of a file using a **linked list**, and supports **undo/redo** operations using **stacks**. This project was developed as a **group assignment** for **Ontario Tech University** in the **Data Structures** course.

## Project Information

- **School:** Ontario Tech University
- **Course:** Data Structures
- **Project Type:** Group Work

## Collaborators

This project was completed collaboratively by:

- **Gaetano Etaborunu**
- **Michael Gharoro**
- **Nolan Clark**

## Project Overview

This system monitors a file, automatically saves new versions when changes are detected, and allows users to:

- View saved version history
- Restore a file to a previous version
- Undo recent changes
- Redo previously undone changes

The implementation directly reflects the project objectives by using a **linked list** to store file versions and **stacks** to manage undo/redo operations.

## Project Objectives

The main goals of the project were to:

- Implement linked lists to store different versions of a file
- Use a stack to support undo/redo functionalities
- Track file changes and allow users to revert to previous versions

## Features

- **Automatic File Monitoring**  
  Detects changes in a selected file and saves updated versions automatically.

- **Linked List Version Storage**  
  Uses a custom linked list to maintain the full version history of the file.

- **Undo and Redo Functionality**  
  Uses stack data structures to move backward and forward through saved file states.

- **Restore by Version ID**  
  Allows the user to restore a specific version from version history.

- **Command-Line Interface**  
  Provides a simple console-based interface for interacting with the system.

## Data Structures Used

### 1. Linked List
The linked list is used to permanently store the sequence of file versions.  
Each node contains:

- A `Version` object
- A reference to the next node

This ensures that all versions are preserved in order.

### 2. Stack
Two stacks are used in the system:

- **Undo Stack** — stores versions for reversing recent changes
- **Redo Stack** — stores undone versions so they can be reapplied

## Code Structure

The project includes the following main classes:

- `Version` — stores the version ID and file contents
- `Node` — represents a node in the linked list
- `VersionLinkedList` — manages the version history
- `Vcs` — contains the main logic, file watcher, and command system

## How the Program Works

1. The user enters the path of the file to watch.
2. A background thread continuously checks the file contents.
3. When a change is detected, a new version is created.
4. The version is added to the linked list history.
5. The version is pushed onto the undo stack.
6. The redo stack is cleared after a new change.
7. The user can then use commands to manage file history.

## Available Commands

- `history` — displays all saved version IDs
- `restore` — restores the file to a chosen version ID
- `undo` — reverts the file to the previous version
- `redo` — reapplies the last undone version
- `exit` — closes the program

## Technologies Used

- **Java**
- `java.util.Stack`
- `java.util.Scanner`
- `java.nio.file.Files`
- `java.nio.file.Paths`
- Java `Thread` for background file monitoring

## How to Compile and Run

### Compile
```bash
javac Vcs.java
```

### Run
```bash
java Vcs
```

### Example File Path
```text
C:\Users\YourName\Documents\test.txt
```

## Example Usage

After running the program, the console will display:

```text
Welcome to the file watcher! What file would you like to watch?
OK! Here are some commands:
history
restore
undo
redo
exit
```

When file changes are detected, the system saves new versions such as:

```text
Saved version 0
Saved version 1
Saved version 2
```

## Strengths of the Project

- Demonstrates practical use of **linked lists** and **stacks**
- Applies data structures to a real file versioning problem
- Provides both version history and undo/redo support
- Clear and organized object-oriented structure

## Possible Improvements

- Add timestamps to each saved version
- Save version history permanently to disk
- Improve handling of multiple watched files
- Add a graphical user interface
- Enhance restore and redo behavior further

## Academic Note

This repository was created for academic purposes as part of a **Data Structures** project at **Ontario Tech University**. It demonstrates the application of fundamental data structures in building a simple version control system.

## Source Basis

The README content is based on the uploaded project code and the provided project objective brief. fileciteturn0file0
