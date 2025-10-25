package models;

// Andrew :)

/*
    Object representation for user record
    Made for login credentials, verified in the application's login page
*/

public class User implements Model {
    private final String username;
    private final String password;

    public User(String username, String password) {
        this.password = password;
        this.username = username;
    }

    @Override
    public String lineRepresentation() {
        return String.format(
                "%s,%s",
                this.username, this.password
        );
    }

    /* Getters */
    public String getUsername() {return this.username;}
    public String getPassword() {return this.password;}
}
