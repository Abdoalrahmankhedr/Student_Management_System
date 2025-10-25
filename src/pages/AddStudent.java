package pages;

import pages.components.Form;
import pages.components.Header;
import windows.MainWindow;
import databases.StudentDatabase;

import javax.swing.*;
import java.awt.*;

/**
 * AddStudent page that embeds the Form component.
 * Calls Form(db, null) so it launches in Add mode (empty fields + "Add+").
 */
public class AddStudent extends JPanel {
    public AddStudent() {
        setBackground(new Color(225, 225, 225));
        setLayout(new BorderLayout());

        Header header = new Header("Add Student", true);
        header.setPreferredSize(new Dimension(800, 50));
        header.backButton.addActionListener(e -> MainWindow.goTo("HomePage"));
        add(header, BorderLayout.NORTH);

        // Create or get your StudentDatabase instance:
        // Replace the filename with your actual file if different.
        StudentDatabase db = new StudentDatabase("students.txt");

        // Pass null so the form opens in Add mode with empty fields
        Form form = new Form(db, null);

        // Card-like white panel for styling (matches design idea)
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(24, 24, 24, 24),
                BorderFactory.createLineBorder(new Color(230, 230, 230))
        ));
        card.add(form, BorderLayout.CENTER);

        // Center wrapper to keep the card centered in the page
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);
        centerWrapper.add(card);
        add(centerWrapper, BorderLayout.CENTER);
    }
}
