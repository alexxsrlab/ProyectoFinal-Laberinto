package views;

import controllers.MazeController;
import javax.swing.*;
import java.awt.*;

public class MazeFrame extends JFrame {
    private final MazePanel panel;
    private final MazeController controlador;
    private final JComboBox<String> combo;

    private static final String[] nombres = {
        "Recursivo","Recursivo Completo","Recursivo BT","BFS","DFS","Backtracking"
    };
    private static final String[] tipos = {
        "Recursive","RecursiveComplete","RecursiveBT","BFS","DFS","Backtracking"
    };

    public MazeFrame(MazeController controlador) {
        this.controlador = controlador;
        setTitle("Maze Solver");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        crearBarraMenu();
        panel = new MazePanel(controlador);
        add(panel, BorderLayout.CENTER);

        crearPanelEdicion();
        combo = new JComboBox<>(nombres);
        crearPanelControles();

        setSize(600, 700);
        setLocationRelativeTo(null);
    }

    private void crearBarraMenu() {
        JMenuBar mb = new JMenuBar();
        JMenu ma = new JMenu("Archivos");
        JMenuItem miNuevo = new JMenuItem("Crear otro laberinto");
        miNuevo.addActionListener(e -> controlador.crearNuevoLaberinto(
            controlador.obtenerLaberinto().rows,
            controlador.obtenerLaberinto().cols
        ));
        ma.add(miNuevo);
        JMenuItem miRes = new JMenuItem("Resultados");
        miRes.addActionListener(e -> new ResultadosDialog(this).setVisible(true));
        ma.add(miRes);
        mb.add(ma);

        JMenu mac = new JMenu("Acerca de");
        JMenuItem mauth = new JMenuItem("Desarrollado por");
        mauth.addActionListener(e -> JOptionPane.showMessageDialog(
            this,
            "Mateo Cordero\nAriel Badillo\nMichael Yumbla\nErick Bermeo",
            "Autores", JOptionPane.INFORMATION_MESSAGE
        ));
        JMenuItem mcon = new JMenuItem("Contacto");
        mcon.addActionListener(e -> JOptionPane.showMessageDialog(
            this,
            "mcorderoc1@est.ups.edu.ec\nbadillob@est.ups.edu.ec\nmyumblap@est.ups.edu.ec\nebermeol1@est.ups.edu.ec",
            "Emails", JOptionPane.INFORMATION_MESSAGE
        ));
        mac.add(mauth);
        mac.add(mcon);
        mb.add(mac);

        setJMenuBar(mb);
    }

    private void crearPanelEdicion() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton b1 = new JButton("Añadir Inicio");
        b1.addActionListener(e -> panel.setStartPositionMode());
        JButton b2 = new JButton("Añadir Fin");
        b2.addActionListener(e -> panel.setEndPositionMode());
        JButton b3 = new JButton("Agregar Paredes");
        b3.addActionListener(e -> panel.setWallMode());
        JButton b4 = new JButton("Quitar Paredes");
        b4.addActionListener(e -> panel.setRemoveWallMode());
        p.add(b1); p.add(b2); p.add(b3); p.add(b4);
        add(p, BorderLayout.NORTH);
    }

    private void crearPanelControles() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
        JPanel pc = new JPanel(new BorderLayout());
        pc.setBorder(BorderFactory.createTitledBorder("Recorridos"));
        pc.add(combo, BorderLayout.CENTER);
        pc.setMaximumSize(new Dimension(200, 60));

        JButton br = new JButton("Resolver");
        br.setMaximumSize(new Dimension(120, 40));
        br.addActionListener(e -> controlador.resolverLaberinto(tipos[combo.getSelectedIndex()]));

        JButton bp = new JButton("Paso a Paso");
        bp.setMaximumSize(new Dimension(120, 40));
        bp.addActionListener(e -> controlador.resolverPorPasos(tipos[combo.getSelectedIndex()]));

        JButton bl = new JButton("Limpiar");
        bl.setMaximumSize(new Dimension(120, 40));
        bl.addActionListener(e -> controlador.limpiarLaberinto());

        p.add(Box.createRigidArea(new Dimension(10, 0)));
        p.add(pc);
        p.add(Box.createHorizontalGlue());
        p.add(br);
        p.add(Box.createRigidArea(new Dimension(10, 0)));
        p.add(bp);
        p.add(Box.createRigidArea(new Dimension(10, 0)));
        p.add(bl);
        p.add(Box.createRigidArea(new Dimension(10, 0)));
        add(p, BorderLayout.SOUTH);
    }

    /**  
     * Este método coincide con lo que usa el controlador.  
     */
    public MazePanel obtenerPanel() {
        return panel;
    }
}