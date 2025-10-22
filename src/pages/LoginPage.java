package pages;

import pages.components.LoginButton;

import javax.swing.*;
import java.awt.*;

// Andrew :)

public class LoginPage extends JPanel {
    public LoginPage() {
        /* Set background to gray */
        setBackground(new Color(224, 225, 223));
        /* Set Layout */
        /* Allows centering without automatically filling all available space */
        setLayout(new GridBagLayout());

        /* White panel */
        JPanel whitePanel = new JPanel();
        whitePanel.setLayout(new BorderLayout());
        whitePanel.setBackground(Color.white);
        whitePanel.setBorder(BorderFactory.createLineBorder(Color.black, 2, true));
        whitePanel.setPreferredSize(new Dimension(300,300));

        /* Form panel */
        JPanel form = new JPanel();
        form.setBackground(Color.white);
        form.setLayout(new GridBagLayout());
        form.setSize(200, 400);

        /* Setting up grid placement */
        GridBagConstraints constraints = new GridBagConstraints();

        /* Main title */
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(10, 10, 20, 10);
        constraints.anchor = GridBagConstraints.CENTER;
        JLabel titleLabel = new JLabel("SMS");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        form.add(titleLabel, constraints);

        /* Add padding of 10px in all directions */
        constraints.insets = new Insets(10, 10, 0, 10);

        /* Add username label in row 1 col 1 */
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.LINE_START;
        JLabel userLabel = new JLabel("Username");
        form.add(userLabel, constraints);

        /* Add username field in row 2 col 1 */
        JTextField userInput = new JTextField();
        userInput.setPreferredSize(new Dimension(200, 30));
        /* Add padding between text and field borders */
        userInput.setMargin(new Insets(5, 5, 5, 5));
        constraints.gridy = 2;
        form.add(userInput, constraints);

        /* Add password label in row 3 col 1 */
        JLabel pwdLabel = new JLabel("Password");
        constraints.gridy = 3;
        constraints.anchor = GridBagConstraints.LINE_START;
        form.add(pwdLabel, constraints);

        /* Add password field in row 4 col 1 */
        JPasswordField pwdInput = new JPasswordField();
        pwdInput.setPreferredSize(new Dimension(200, 30));
        pwdInput.setMargin(new Insets(5, 5, 5, 5));
        constraints.gridy = 4;
        form.add(pwdInput, constraints);

        /* Add login button in row 5 col 1 */
        JButton loginBtn = new LoginButton(userInput, pwdInput);
        constraints.gridy = 5;
        /* Push the login button further than input fields */
        constraints.insets = new Insets(50, 10, 0, 10);
        form.add(loginBtn, constraints);

        /* Add form */
        whitePanel.add(form);

        /* Add panel */
        add(whitePanel);
    }
}
