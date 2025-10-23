package pages.components;

import databases.UserDatabase;
import javax.swing.*;
import java.awt.*;
import windows.MainWindow;
// Andrew :)

public class LoginButton extends JButton {
    private final JTextField username;
    private final JPasswordField password;
    /* Loads user database to verify credentials */
    private final UserDatabase db = new UserDatabase();

    public LoginButton(JTextField username, JPasswordField password) {
        this.username = username;
        this.password = password;

        /* Design Login Button */
        setText("Login");
        setPreferredSize(new Dimension(200, 30));
        setBackground(new Color(30, 203, 225));
        setForeground(Color.white);
        setFont(new Font("Arial", Font.BOLD, 16));
        /* Border */
        setBorder(BorderFactory.createLineBorder(Color.black, 2, true));

        /* On click event listener */
        addActionListener((_) -> verifyLogin());

        /* Disable button if not initialised correctly */
        if (username == null || password == null) {
            System.out.println("[LoginButton]: Must provide both username and password fields\n");
            setEnabled(false);
        }
    }

    private void verifyLogin() {
        /* Carry out login process */
        String currentUser = username.getText();

        /* Retrieve password */
        StringBuilder pwdSb = new StringBuilder();
        for (char c : password.getPassword()) pwdSb.append(c);
        String currentPwd = pwdSb.toString();

        /* Log in console */
        System.out.printf("[LoginButton]: Entered %s and %s as login credentials\n", currentUser, currentPwd);

        /* Verify & Display */
        if (currentUser.isEmpty() && currentPwd.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                                "Please enter username and password",
                                    "Invalid Credentials",
                                         JOptionPane.WARNING_MESSAGE,
                                    null);
        } else if (!currentUser.isEmpty() && currentPwd.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter your password",
                    "Missing Password",
                    JOptionPane.WARNING_MESSAGE,
                    null);
        } else if (currentUser.isEmpty() && !currentPwd.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter your username",
                    "Missing Username",
                    JOptionPane.WARNING_MESSAGE,
                    null);
        } else {
            if (db.validatePassword(currentUser, currentPwd)) {
                /* Go to next page */
                username.setText("");
                password.setText("");
                MainWindow.goTo("HomePage");
            } else {
                JOptionPane.showMessageDialog(this,
                        "Incorrect username or password",
                        "Invalid Credentials",
                        JOptionPane.ERROR_MESSAGE,
                        null);
            }
        }
    }
}
