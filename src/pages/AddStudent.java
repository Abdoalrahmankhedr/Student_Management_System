package pages;

import pages.components.Header;
import windows.MainWindow;

import javax.swing.*;
import java.awt.*;

public class AddStudent extends JPanel {
    public AddStudent(){
        setBackground(new Color(225, 225, 225));
        setLayout(new BorderLayout());

        Header header = new Header("Add Student", true);
        header.setPreferredSize(new Dimension(800, 50));
        header.backButton.addActionListener(e-> MainWindow.goTo("HomePage"));
        add(header, BorderLayout.NORTH);
        //Write the code of the form here
    }
}