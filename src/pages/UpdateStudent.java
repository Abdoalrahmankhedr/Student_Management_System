package pages;

import pages.components.Header;
import windows.MainWindow;

import javax.swing.*;
import java.awt.*;

public class UpdateStudent extends JPanel {
    public UpdateStudent(){
        setBackground(new Color(225, 225, 225));
        setLayout(new BorderLayout());

        Header header = new Header("Update Student", true);
        header.setPreferredSize(new Dimension(800, 50));
        header.backButton.addActionListener(e-> {
            MainWindow.goTo("StudentsList");
            //after the user press back button  and the data of the update is valid show message that if he wants to update or not
        });
        add(header, BorderLayout.NORTH);
        //Write the code of the form here
    }
}