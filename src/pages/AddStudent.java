package pages;

import pages.components.Form;
import pages.components.Header;
import windows.MainWindow;
import databases.StudentDatabase;

import javax.swing.*;
import java.awt.*;

/**
 * AddStudent page styled like a modern card layout.
 * Always resets form when leaving or re-entering.
 */
public class AddStudent extends JPanel {
    private final Form form;

    public AddStudent() {
        setBackground(new Color(240, 240, 240));
        setLayout(new BorderLayout());

        // Header
        Header header = new Header("Add Student", true);
        header.setPreferredSize(new Dimension(800, 60));
        add(header, BorderLayout.NORTH);

        // Database
        StudentDatabase db = new StudentDatabase("students.txt");

        // Form
        form = new Form(db);

        // Back button behavior
        header.backButton.addActionListener(e -> {
            form.clearForm(); // Reset fields when going back
            MainWindow.goTo("HomePage");
        });

        // Card design
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(32, 32, 32, 32),
                BorderFactory.createLineBorder(new Color(230, 230, 230), 1, true)
        ));
        card.add(form, BorderLayout.CENTER);

        // Center card
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);
        centerWrapper.add(card);

        add(centerWrapper, BorderLayout.CENTER);
    }
}
