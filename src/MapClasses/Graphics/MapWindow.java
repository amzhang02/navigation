package MapClasses.Graphics;
import java.awt.*;

import javax.swing.*;

public class MapWindow extends JFrame

{
    public MapWindow(String title, Component viewPanel, Component controlPanel)
    {
        super(title);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(viewPanel, BorderLayout.CENTER);
        mainPanel.add(controlPanel, BorderLayout.SOUTH);

        getContentPane().add(mainPanel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        toFront();
    }




}
