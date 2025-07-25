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
        model = new DefaultTableModel(new Object[]{"Algorithm", "Path Length", "Time (ms)"}, 0);
        JTable table = new JTable(model);
        cargarDatos();

        add(new JScrollPane(table), BorderLayout.CENTER);

        JButton limpiarBtn = new JButton("Limpiar Resultados");
        limpiarBtn.addActionListener(e -> {
            dao.limpiar();
            cargarDatos();
        });

        add(limpiarBtn, BorderLayout.SOUTH);

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
