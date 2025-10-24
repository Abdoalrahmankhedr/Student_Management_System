package pages.components;
import windows.MainWindow;

import javax.swing.*;
import java.awt.*;

public class Header extends JPanel {
    private JLabel titleLabel;
    public JButton backButton = new JButton("← Back");
    //Constructure
    //This will set the titlePage with the value of the parameter "title"
    //And if showBackButton is false this will not show the backButton
    public Header(String title, boolean showBackButton){

        setLayout(new BorderLayout());
        setBackground(Color.white);
        setSize(800,70);
        setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));


        titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));


        if(showBackButton){
            backButton.setBackground(new Color(37, 99, 235));
            backButton.setForeground(Color.WHITE);
            backButton.setFont(new Font("Arial", Font.PLAIN, 20));
            add(backButton, BorderLayout.WEST);
        }

        add(titleLabel, BorderLayout.CENTER);
    }

}