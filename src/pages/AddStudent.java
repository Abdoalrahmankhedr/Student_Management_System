package pages;

import pages.components.Form;
import pages.components.Header;
import windows.MainWindow;
import databases.StudentDatabase;

import javax.swing.*;
import java.awt.*;


public class AddStudent extends JPanel {
    public AddStudent() {
        // Background color
        setBackground(new Color(240, 240, 240));
        setLayout(new BorderLayout());

        // Header
        Header header = new Header("Add Student", true);
        header.setPreferredSize(new Dimension(800, 60));
        header.backButton.addActionListener(e -> MainWindow.goTo("HomePage"));
        add(header, BorderLayout.NORTH);

        // Database connection
        StudentDatabase db = new StudentDatabase("src\\resources\\students.txt");


        Form form = new Form(db);


        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(32, 32, 32, 32),
                BorderFactory.createLineBorder(new Color(230, 230, 230), 1, true)
        ));
        card.setOpaque(true);


        card.add(form, BorderLayout.CENTER);


        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);
        centerWrapper.add(card);

        add(centerWrapper, BorderLayout.CENTER);
    }
}
