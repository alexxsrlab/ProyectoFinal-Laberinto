package views;

import dao.AlgorithmResultDAO;
import dao.daoImpl.AlgorithmResultDAOFile;
import models.AlgorithmResult;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

// Diálogo que muestra resultados guardados con opción de limpiar
public class ResultadosDialog extends JDialog {
    private AlgorithmResultDAO dao;
    private DefaultTableModel model;

    public ResultadosDialog(Frame owner) {
        super(owner, "Resultados de Algoritmos", true);
        dao = new AlgorithmResultDAOFile();

        setLayout(new BorderLayout());
        model = new DefaultTableModel(new Object[]{"Algoritmo", "Tamaño del Camino", "Tiempo (ms)"}, 0);
        JTable table = new JTable(model);
        cargarDatos();

        add(new JScrollPane(table), BorderLayout.CENTER);

        JButton limpiarBtn = new JButton("Limpiar Resultados");
        limpiarBtn.addActionListener(e -> {
            int resp = JOptionPane.showConfirmDialog(this, "¿Está seguro de borrar los resultados?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (resp == JOptionPane.YES_OPTION) {
                dao.limpiar();
                cargarDatos();
            }
        });

        JButton graficarBtn = new JButton("Graficar Resultados");
        graficarBtn.addActionListener(e -> {
            new GraficaResultadosDialog((JFrame) getOwner(), dao.obtenerTodos()).setVisible(true);
        });

        JPanel btnPanel = new JPanel();
        btnPanel.add(limpiarBtn);
        btnPanel.add(graficarBtn);
        add(btnPanel, BorderLayout.SOUTH);

        setSize(400, 300);
        setLocationRelativeTo(owner);
    }

    private void cargarDatos() {
        model.setRowCount(0);
        List<AlgorithmResult> results = dao.obtenerTodos();
        for (AlgorithmResult r : results) {
            model.addRow(new Object[]{r.getAlgorithm(), r.getPathLength(), r.getTime()});
        }
    }
}
