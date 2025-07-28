package views;

import controllers.MazeController;
import javax.swing.*;
import java.awt.*;

public class MazeFrame extends JFrame {
    private final MazeController controlador;
    private final MazePanel panel;
    private final JComboBox<String> algorithmCombo;

    private static final String[] algorithmNames = {
        "Recursivo", "Recursivo Completo", "Recursivo BT",
        "BFS", "DFS", "Backtracking"
    };
    private static final String[] algorithmTypes = {
        "Recursive", "RecursiveComplete", "RecursiveBT",
        "BFS", "DFS", "Backtracking"
    };

    public MazeFrame(MazeController controlador) {
        this.controlador = controlador;
        setTitle("Maze Solver");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Menú
        JMenuBar mb = new JMenuBar();
        JMenu mArc = new JMenu("Archivo");

        JMenuItem mNuevo = new JMenuItem("Crear otro laberinto");
        mNuevo.addActionListener(e ->
            controlador.crearNuevoLaberinto(
                controlador.obtenerLaberinto().rows,
                controlador.obtenerLaberinto().cols
            )
        );
        mArc.add(mNuevo);

        JMenuItem mRes = new JMenuItem("Resultados");
        mRes.addActionListener(e ->
            new ResultadosDialog(this).setVisible(true)
        );
        mArc.add(mRes);
        mb.add(mArc);

        JMenu mAc = new JMenu("Acerca de");

        JMenuItem mAut = new JMenuItem("Desarrollado por");
        mAut.addActionListener(e ->
            JOptionPane.showMessageDialog(
                this,
                "Mateo Cordero\nAriel Badillo\nMichael Yumbla\nErick Bermeo",
                "Autores", JOptionPane.INFORMATION_MESSAGE
            )
        );
        mAc.add(mAut);

        JMenuItem mCon = new JMenuItem("Contacto");
        mCon.addActionListener(e ->
            JOptionPane.showMessageDialog(
                this,
                "mcorderoc1@est.ups.edu.ec\nabadillob@est.ups.edu.ec\nmyumblap@est.ups.edu.ec\nebermeol1@est.ups.edu.ec",
                "Correos", JOptionPane.INFORMATION_MESSAGE
            )
        );
        mAc.add(mCon);

        mb.add(mAc);
        setJMenuBar(mb);

        // Panel del laberinto
        panel = new MazePanel(controlador);
        add(panel, BorderLayout.CENTER);

        // Panel de edición
        JPanel pEd = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton bStart = new JButton("Añadir Inicio");
        bStart.addActionListener(e -> panel.setStartPositionMode());

        JButton bEnd = new JButton("Añadir Fin");
        bEnd.addActionListener(e -> panel.setEndPositionMode());

        JButton bWall = new JButton("Agregar Paredes");
        bWall.addActionListener(e -> panel.setWallMode());

        JButton bClearWall = new JButton("Quitar Paredes");
        bClearWall.addActionListener(e -> panel.setRemoveWallMode());

        pEd.add(bStart);
        pEd.add(bEnd);
        pEd.add(bWall);
        pEd.add(bClearWall);
        add(pEd, BorderLayout.NORTH);

        // Panel de controles
        JPanel pCtrl = new JPanel();
        pCtrl.setLayout(new BoxLayout(pCtrl, BoxLayout.X_AXIS));

        JLabel recorridoLabel = new JLabel("Recorridos:");
        algorithmCombo = new JComboBox<>(algorithmNames);

        JButton bSolve = new JButton("Resolver");
        bSolve.setMaximumSize(new Dimension(120, 40));
        bSolve.addActionListener(e -> {
            String tipo = algorithmTypes[algorithmCombo.getSelectedIndex()];
            controlador.resolverLaberinto(tipo);
        });

        JButton bStep = new JButton("Paso a Paso");
        bStep.setMaximumSize(new Dimension(120, 40));
        bStep.addActionListener(e -> {
            String tipo = algorithmTypes[algorithmCombo.getSelectedIndex()];
            controlador.pasoAPaso(tipo);
        });

        JButton bClear = new JButton("Limpiar");
        bClear.setMaximumSize(new Dimension(120, 40));
        bClear.addActionListener(e -> controlador.limpiarLaberinto());

        pCtrl.add(Box.createRigidArea(new Dimension(10, 0)));
        pCtrl.add(recorridoLabel);
        pCtrl.add(Box.createRigidArea(new Dimension(5, 0)));
        pCtrl.add(algorithmCombo);
        pCtrl.add(Box.createHorizontalGlue());
        pCtrl.add(bSolve);
        pCtrl.add(Box.createRigidArea(new Dimension(10, 0)));
        pCtrl.add(bStep);
        pCtrl.add(Box.createRigidArea(new Dimension(10, 0)));
        pCtrl.add(bClear);
        pCtrl.add(Box.createRigidArea(new Dimension(10, 0)));

        add(pCtrl, BorderLayout.SOUTH);

        setSize(600, 700);
        setLocationRelativeTo(null);
    }

    public MazePanel obtenerPanel() {
        return panel;
    }
}
