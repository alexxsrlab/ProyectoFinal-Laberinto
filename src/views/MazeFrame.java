package views;

import controllers.MazeController;
import javax.swing.*;
import java.awt.*;

public class MazeFrame extends JFrame {
    private MazePanel panel;
    private MazeController controller;

    public MazeFrame(MazeController controller) {
        this.controller = controller;
        setTitle("Maze Solver");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // BARRA DE MENÚ
        JMenuBar menuBar = new JMenuBar();

        // Menú Archivos
        JMenu menuArchivos = new JMenu("Archivos");
        JMenuItem nuevoLabItem = new JMenuItem("Crear otro laberinto");
        nuevoLabItem.addActionListener(e -> {
            final int[] filas = {10};
            final int[] columnas = {10};
            boolean valido = false;
            while (!valido) {
                String filasStr = JOptionPane.showInputDialog(this, "Ingrese la cantidad de filas (mínimo 2):", "Filas", JOptionPane.QUESTION_MESSAGE);
                if (filasStr == null) return;
                String columnasStr = JOptionPane.showInputDialog(this, "Ingrese la cantidad de columnas (mínimo 2):", "Columnas", JOptionPane.QUESTION_MESSAGE);
                if (columnasStr == null) return;
                try {
                    filas[0] = Integer.parseInt(filasStr);
                    columnas[0] = Integer.parseInt(columnasStr);
                    if (filas[0] >= 2 && columnas[0] >= 2) {
                        valido = true;
                    } else {
                        JOptionPane.showMessageDialog(this, "Debe ingresar valores mayores o iguales a 2.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Ingrese solo números enteros.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            // Cerrar ventana actual y abrir una nueva
            dispose();
            SwingUtilities.invokeLater(() -> {
                controller.crearNuevoLaberinto(filas[0], columnas[0]);
            });
        });
        menuArchivos.add(nuevoLabItem);

        JMenuItem resultadosItem = new JMenuItem("Resultados");
        resultadosItem.addActionListener(e -> {
            ResultadosDialog dialog = new ResultadosDialog(this);
            dialog.setVisible(true);
        });
        menuArchivos.add(resultadosItem);

        // Menú Acerca de
        JMenu menuAcerca = new JMenu("Acerca de");
        JMenuItem desarrolladoPor = new JMenuItem("Desarrollado por");
        desarrolladoPor.addActionListener(e -> JOptionPane.showMessageDialog(this,
            "Mateo Cordero\nAriel Badillo\nMichael Yumbla\nErick Bermeo",
            "Desarrolladores", JOptionPane.INFORMATION_MESSAGE));
        JMenuItem contacto = new JMenuItem("Contacto");
        contacto.addActionListener(e -> JOptionPane.showMessageDialog(this,
            "mcorderoc1@est.ups.edu.ec\nabadillob@est.ups.edu.ec\nmyumblap@est.ups.edu.ec\nebermeol1@est.ups.edu.ec",
            "Correos de contacto", JOptionPane.INFORMATION_MESSAGE));
        menuAcerca.add(desarrolladoPor);
        menuAcerca.add(contacto);

        menuBar.add(menuArchivos);
        menuBar.add(menuAcerca);
        setJMenuBar(menuBar);

        setLayout(new BorderLayout());

        // Panel del laberinto (MazePanel) en el centro, se expandirá con la ventana
        panel = new MazePanel(controller);
        add(panel, BorderLayout.CENTER);

        // Panel superior para controles de edición (NORTE)
        JPanel editPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10)); // Centrado y con espacio entre botones
        JButton setStartBtn = new JButton("Añadir Inicio");
        setStartBtn.addActionListener(e -> panel.setStartPositionMode());
        JButton setEndBtn = new JButton("Añadir Fin");
        setEndBtn.addActionListener(e -> panel.setEndPositionMode());
        JButton addWallBtn = new JButton("Agregar Paredes");
        addWallBtn.addActionListener(e -> panel.setWallMode());
        JButton clearWallBtn = new JButton("Quitar Paredes");
        clearWallBtn.addActionListener(e -> panel.setRemoveWallMode());
        editPanel.add(setStartBtn);
        editPanel.add(setEndBtn);
        editPanel.add(addWallBtn);
        editPanel.add(clearWallBtn);
        add(editPanel, BorderLayout.NORTH);

        // Panel inferior SOLO con recorridos, resolver y limpiar (SUR)
        JPanel solvePanel = new JPanel();
        solvePanel.setLayout(new BoxLayout(solvePanel, BoxLayout.X_AXIS));

        String[] algorithms = {
            "Recursive",
            "Recursive Complete",
            "Recursive BT",
            "BFS",
            "DFS"
        };
        String[] algorithmTypes = {
            "Recursive",
            "RecursiveComplete",
            "RecursiveBT",
            "BFS",
            "DFS"
        };
        JComboBox<String> algorithmCombo = new JComboBox<>(algorithms);
        JPanel comboPanel = new JPanel();
        comboPanel.setBorder(BorderFactory.createTitledBorder("Recorridos"));
        comboPanel.setLayout(new BorderLayout());
        comboPanel.add(algorithmCombo, BorderLayout.CENTER);
        comboPanel.setMaximumSize(new Dimension(200, 60)); // Limita el tamaño máximo del panel del combo

        JButton solveBtn = new JButton("Resolver");
        solveBtn.setMaximumSize(new Dimension(120, 40));
        solveBtn.addActionListener(e -> {
            int idx = algorithmCombo.getSelectedIndex();
            controller.resolverLaberinto(algorithmTypes[idx]);
        });

        JButton clearBtn = new JButton("Limpiar");
        clearBtn.setMaximumSize(new Dimension(120, 40));
        clearBtn.addActionListener(e -> controller.limpiarLaberinto());


        solvePanel.add(Box.createRigidArea(new Dimension(10, 0))); // Espacio izquierdo
        solvePanel.add(comboPanel);
        solvePanel.add(Box.createHorizontalGlue()); // Empuja los siguientes a la derecha
        solvePanel.add(solveBtn);
        solvePanel.add(Box.createRigidArea(new Dimension(10, 0)));
        solvePanel.add(clearBtn);
        solvePanel.add(Box.createRigidArea(new Dimension(10, 0))); // Espacio derecho

        add(solvePanel, BorderLayout.SOUTH);

        setSize(600, 700);
        setLocationRelativeTo(null); 
    }

    public MazePanel obtenerPanel() {
        return panel;
    }
}