package databases;

import models.User;

import java.util.ArrayList;
import java.util.List;

// Andrew :)

public class UserDatabase extends Database<User> {
    /* Members */
    // file location is constant to allow easy adding for users
    private static final String filename = "src/resources/users.txt";

    /* No args constructor */
    public UserDatabase() {
        super(filename);
        this.logName = "UserDatabase";

        /* Get all users */
        readFile();
    }

    @Override
    public User createRecordFrom(String line) {
        // Note:
        // User.lineRepresentation() returns:
        //      "{username},{password}"
        //        [0]          [1]
        if (line == null) return null; // Makes sure no null was passed

        String[] data = line.split("\\s*,\\s*");

        // If the given string doesn't represent EmployeeUser as expected
        if (data.length < 2 || line.isEmpty()) {
            // POSSIBLE: Unnecessary prints
            System.out.println("[UserDatabase]: Invalid line representation for User was given");
            return null;
        }

        String username = data[0];
        String password = data[1];

        return new User(username, password);
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
