import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

// Andrew :)

/*
    The layout of the window is structured as such:
    The main JFrame will be the window, housing the CardLayout

    CardLayout allows for switching between JPanels through page names
    Each JPanel would represent a page in the application, and is assigned a name
    If a JButton or Event redirect to a specific page, using the page name will change the visible 'card'

    This keeps track of all pages added, allows adding pages with names,
    removing pages using their set names, and allow for navigation to pages using their set names

    Note:
        Any panels made as a page must not be set visible before adding!
        Only by going to the page would it be visible (use: goTo)

    Any feedback is appreciated ;)
*/

public class MainWindow {
    /* App Icon Image */
    private static final ImageIcon icon = new ImageIcon("src/main/resources/static/icon.png");
    /* Window Name */
    private static final String title = "Student Management";

    /* Layout */
    private static final CardLayout cardLayout = new CardLayout();
    /* Main Panel */
    private static final JPanel cardPanel = new JPanel(cardLayout);
    /* Pages Index */
    private static final Map<String, Component> pages = new HashMap<>();

    private static JFrame main;

    public static void start() {
        if (main == null) {
            main = new JFrame();
            /* Set window dimensions to 800x600 */
            main.setSize(800, 600);
            main.setTitle(title);
            /* Window can not change size */
            main.setResizable(false);
            /* Close on exit behaviour */
            main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            /* Setting icon */
            main.setIconImage(icon.getImage());
            /* Layout won't be changed since pages should take up full space */
            // setLayout(null);

            /* Add Main Panel */
            cardPanel.setLayout(cardLayout);
            cardPanel.setVisible(true);
            main.add(cardPanel, BorderLayout.CENTER);
            main.setVisible(true);
        }
    }

    public static void addPage(String name, Component panel) {
        /* Add page to index */
        if (pages.getOrDefault(name, null) == null) {
            pages.put(name, panel);
            cardPanel.add(panel, name);
        }
    }

    public static void removePage(String name) {
        /* Remove unneeded page */
        Component page = pages.getOrDefault(name, null);
        if (page != null) {
            pages.remove(name);
            cardPanel.remove(page);
        }
    }

    public static void goTo(String name) {
        /* Navigate to specific page */
        Component page = pages.getOrDefault(name, null);
        if (page != null) {
            cardLayout.show(cardPanel, name);
        }
    }
}
