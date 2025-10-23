package pages;

import pages.components.Header;

import javax.swing.*;
import java.awt.*;

public class UpdateStudent extends JPanel {
    public UpdateStudent(){
        setBackground(new Color(225, 225, 225));
        setLayout(new BorderLayout());

        Header header = new Header("Update Student", false);
        header.setPreferredSize(new Dimension(800, 50));
        add(header, BorderLayout.NORTH);
        //Write the code of the form here
    }
}