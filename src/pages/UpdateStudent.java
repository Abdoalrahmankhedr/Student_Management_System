package pages;

import databases.StudentDatabase;
import models.Student;
import pages.components.Form;
import pages.components.Header;
import windows.MainWindow;

import javax.swing.*;
import java.awt.*;

public class UpdateStudent extends JPanel {
    public UpdateStudent(Student student) {
        // Background color
        setBackground(new Color(240, 240, 240));
        setLayout(new BorderLayout());

        // Header
        Header header = new Header("Update Student", true);
        header.setPreferredSize(new Dimension(800, 60));
        header.backButton.addActionListener(e -> MainWindow.goTo("StudentsList"));
        add(header, BorderLayout.NORTH);

        // Database connection
        StudentDatabase db = new StudentDatabase("src/resources/students.txt");

        Form form = new Form(db);
        form.setUpdateMode(student);

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
