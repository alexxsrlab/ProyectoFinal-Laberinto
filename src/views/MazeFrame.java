package views;

import controllers.MazeController;

import javax.swing.*;
import java.awt.*;

// Ventana principal que contiene el panel del laberinto y controles
public class MazeFrame extends JFrame {
    private MazePanel panel;
    private MazeController controller;

    public MazeFrame(MazeController controller) {
        this.controller = controller;
        setTitle("Maze Solver");
        setSize(600, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        panel = new MazePanel(controller);
        add(panel, BorderLayout.CENTER);

        // Panel inferior con botones
        JPanel buttonsPanel = new JPanel();

        JButton dfsBtn = new JButton("Solve DFS");
        dfsBtn.addActionListener(e -> controller.resolverConDFS());

        JButton bfsBtn = new JButton("Solve BFS");
        bfsBtn.addActionListener(e -> controller.resolverConBFS());

        JButton recBtn = new JButton("Recursive");
        recBtn.addActionListener(e -> controller.resolverRecursivo());

        JButton recCompBtn = new JButton("Recursive Complete");
        recCompBtn.addActionListener(e -> controller.resolverRecursivoCompleto());

        JButton recBTBtn = new JButton("Recursive BT");
        recBTBtn.addActionListener(e -> controller.resolverRecursivoBT());

        buttonsPanel.add(dfsBtn);
        buttonsPanel.add(bfsBtn);
        buttonsPanel.add(recBtn);
        buttonsPanel.add(recCompBtn);
        buttonsPanel.add(recBTBtn);

        add(buttonsPanel, BorderLayout.SOUTH);
    }

    public MazePanel obtenerPanel() {
        return panel;
    }
}
