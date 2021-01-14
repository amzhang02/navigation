package MapClasses.Graphics;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControlPanel extends JPanel {
    JComboBox<String> chooseBox = new JComboBox<>();
    JLabel distance;
    JLabel numNodes;

    class FindPathButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            // nav code stuff hehe
        }
    }

    class ChooseBoxListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }

    class ClearButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }
    public ControlPanel() {
        JPanel actionPanel = new JPanel();
        actionPanel.setLayout(new FlowLayout());
        actionPanel.setBorder(new TitledBorder("Path Finding"));

        JButton findPathButton = new JButton("Find Path");
        findPathButton.addActionListener(new FindPathButtonListener());
        actionPanel.add(findPathButton);

        JPanel choosePanel = new JPanel();
        choosePanel.setLayout(new FlowLayout());
        choosePanel.setBorder(new TitledBorder("Choose"));

        chooseBox.addItem("Catlin1-allroads");
        chooseBox.addItem("Catlin2-allroads");
        chooseBox.addItem("Portland1-primary");
        chooseBox.addActionListener(new ChooseBoxListener());
        choosePanel.add(chooseBox);

        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(new ClearButtonListener());
        choosePanel.add(clearButton);

        JPanel resultsPanel = new JPanel();
        resultsPanel.setLayout(new FlowLayout());
        resultsPanel.setBorder(new TitledBorder("Results"));

        distance = new JLabel("Distance: 0.0");
        numNodes = new JLabel("Number of Nodes: 0");
        resultsPanel.add(distance);
        resultsPanel.add(numNodes);

        setLayout(new BorderLayout());
        add(actionPanel, BorderLayout.EAST);
        add(choosePanel, BorderLayout.WEST);
        add(resultsPanel, BorderLayout.CENTER);
    }
}
