package databases;

import models.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UserDatabase {
    /* Members */
    private final List<User> records = new ArrayList<>();
    // file location is constant to allow easy adding for users
    private final String filename = "src/resources/users.txt";

    /* No args constructor */
    public UserDatabase() {}

    public void readFile() {
        File file = new File(this.filename);

        if (!file.exists() || !file.getName().endsWith(".txt")) {
            System.out.println("[UserDatabase]: Unable to read file!");
            return;
        }

        try (Scanner reader = new Scanner(file)) {
            // Create Scanner resource and cleanup

            // Reset records before reading
            this.records.clear();
            // As long as the file has lines,
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                User record = createRecordFrom(line);
                insertRecord(record);
            }
            System.out.println("[UserDatabase]: Successfully read all records!");
        } catch (FileNotFoundException err) {
            System.out.println("[UserDatabase]: File was not found");
        }
    }

    public void saveToFile() {
        File file = new File(this.filename);
        try {
            if (!file.exists() && file.getName().endsWith(".txt")) {
                if (file.createNewFile()) System.out.println("[UserDatabase]: Successfully created new file!");
                else {
                    System.out.println("[UserDatabase]: Failed to create new file");
                    return;
                }
            }
        }
        catch (IOException err) {
            System.out.println("[UserDatabase]: Failed to create new file");
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(this.filename))) {
            // Create BufferedWriter resource and cleanup

            StringBuilder sb = new StringBuilder();
            // Build a string from all record line representations
            // and write that to file
            for (User record : this.records) {
                sb.append(record.lineRepresentation()).append("\n");
            }
            writer.write(sb.toString());
            System.out.println("[UserDatabase]:  Successfully wrote to file!");
        } catch (IOException err) {
            System.out.println("[UserDatabase]:  File can not be accessed, perhaps it is being used by another program..");
        }
    }

    public User createRecordFrom(String line) {
        // Note:
        // User.lineRepresentation() returns:
        //      "{username},{password}"
        //        [0]          [1]
        if (line == null) return null; // Makes sure no null was passed

        String[] data = line.split("\\s*,\\s*");

        // If the given string doesn't represent EmployeeUser as expected
        if (data.length < 6 || line.isEmpty()) {
            // POSSIBLE: Unnecessary prints
            System.out.println("[UserDatabase]: Invalid line representation for User was given");
            return null;
        }

        String username = data[0];
        String password = data[1];

        return new User(username, password);
    }

    private void insertRecord(User user) {
        if (user != null) {
            this.records.add(user);
        }
    }

    public User getUserByName(String username) {
        /* Returns user if found by username */
        for (User record : this.records) {
            if (record.getUsername().equals(username)) return record;
        }

        return null;
    }

    public boolean validatePassword(String username, String password) {
        /* Verify credibility of login credentials */
        User user = getUserByName(username);
        return user != null && user.getPassword().equals(password);
    }
}
