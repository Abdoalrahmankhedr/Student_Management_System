package pages;

import pages.components.Header;
import windows.MainWindow;

import javax.swing.*;
import java.awt.*;

public class HomePage extends JPanel {
    public HomePage() {
        setBackground(new Color(225, 225, 225));
        setLayout(new BorderLayout());
        //View the header with the title "Home Page"
        Header header = new Header("Home Page", false);
        header.setPreferredSize(new Dimension(600, 50));
        add(header, BorderLayout.NORTH);

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setBackground(Color.WHITE);
        buttonsPanel.setLayout(new GridLayout(3, 1, 10, 10));

        JButton btn1 = new JButton("Add A Student");
        JButton btn2 = new JButton("Show Students List");
        JButton btn3 = new JButton("Log Out");

        Font font = new Font("Arial", Font.BOLD, 25);
        Dimension btnSize = new Dimension(300, 40);
        JButton[] buttons = {btn1, btn2, btn3};
        for (JButton b : buttons) {
            b.setForeground(Color.white);
            b.setFont(font);
            b.setBackground(new Color(37, 99, 235));
            b.setFocusPainted(false);
            b.setPreferredSize(btnSize);
            buttonsPanel.add(b);
        }
        btn3.setBackground(new Color(239,68,68));

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(new Color(225, 225, 225));
        centerPanel.add(buttonsPanel);

        add(centerPanel, BorderLayout.CENTER);
        btn3.addActionListener(e-> {
            MainWindow.goTo("login");
        });
        btn2.addActionListener(e-> {
            MainWindow.goTo("StudentsList");
        });
        btn1.addActionListener(e->{
            MainWindow.goTo("AddStudent");
        });
    }
}