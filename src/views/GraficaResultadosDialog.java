package views;

import models.AlgorithmResult;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GraficaResultadosDialog extends JDialog {
    private List<AlgorithmResult> resultados;

    public GraficaResultadosDialog(JFrame parent, List<AlgorithmResult> resultados) {
        super(parent, "Gráfica", true);
        this.resultados = resultados;
        setSize(600, 400);
        setLocationRelativeTo(parent);
        add(new GraficaPanel());
    }

    class GraficaPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            setBackground(Color.WHITE);

            if (resultados.isEmpty()) return;

            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            // Márgenes dinámicos
            int left = 80, right = 40, top = 60, bottom = 80;
            int width = w - left - right;
            int height = h - top - bottom;

            // Título centrado
            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Arial", Font.BOLD, 20));
            String titulo = "Tiempos de Ejecución por Algoritmo";
            FontMetrics fmTitulo = g2.getFontMetrics();
            int tituloWidth = fmTitulo.stringWidth(titulo);
            g2.drawString(titulo, left + (width - tituloWidth) / 2, top - 25);

            // Ejes
            g2.setStroke(new BasicStroke(2));
            g2.drawLine(left, top, left, top + height); // Y
            g2.drawLine(left, top + height, left + width, top + height); // X

            // Eje Y label (horizontal, arriba de los números)
            g2.setFont(new Font("Arial", Font.BOLD, 13));
            String yLabel = "Tiempo (ns)";
            FontMetrics fm = g2.getFontMetrics();
            int yLabelWidth = fm.stringWidth(yLabel);
            // Dibuja el título justo encima del primer número del eje Y, más a la derecha
            g2.setColor(Color.BLACK);
            g2.drawString(yLabel, left - yLabelWidth -5, top - 10);

            // Eje Y (tiempo)
            long max = resultados.stream().mapToLong(AlgorithmResult::getTime).max().orElse(1);
            int yTicks = 5;
            g2.setFont(new Font("Arial", Font.PLAIN, 12));
            for (int i = 0; i <= yTicks; i++) {
                int y = top + height - (int) (height * i / (double) yTicks);
                g2.setColor(Color.LIGHT_GRAY);
                g2.drawLine(left, y, left + width, y);
                g2.setColor(Color.BLACK);
                long valor = (long) (max * i / (double) yTicks);
                String valorStr = String.format("%,d", valor);
                int valorWidth = g2.getFontMetrics().stringWidth(valorStr);
                // Dibuja los números bien a la izquierda, fuera del área de la gráfica
                g2.drawString(valorStr, left - valorWidth - 10, y + 5);
            }

            // Eje X (algoritmos)
            int n = resultados.size();
            int step = (n > 1) ? width / (n - 1) : width;
            int[] xs = new int[n];
            int[] ys = new int[n];

            for (int i = 0; i < n; i++) {
                xs[i] = left + i * step;
                ys[i] = top + height - (int) (height * resultados.get(i).getTime() / (double) max);
            }

            // Línea
            g2.setColor(new Color(255, 100, 150, 180));
            g2.setStroke(new BasicStroke(2));
            for (int i = 0; i < n - 1; i++) {
                g2.drawLine(xs[i], ys[i], xs[i + 1], ys[i + 1]);
            }

            // Puntos y etiquetas
            g2.setColor(Color.RED);
            for (int i = 0; i < n; i++) {
                g2.fillOval(xs[i] - 4, ys[i] - 4, 8, 8);
            }

            // Nombres de algoritmos (más pequeños)
            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Arial", Font.PLAIN, 11));
            for (int i = 0; i < n; i++) {
                String nombre = resultados.get(i).getAlgorithm();
                int strWidth = g2.getFontMetrics().stringWidth(nombre);
                g2.drawString(nombre, xs[i] - strWidth / 2, top + height + 20);
            }

            // Eje X label
            g2.setFont(new Font("Arial", Font.BOLD, 13));
            g2.drawString("Algoritmo", left + width / 2 - 30, top + height + 45);

            // Leyenda
            g2.setFont(new Font("Arial", Font.PLAIN, 12));
            g2.setColor(new Color(255, 100, 150, 180));
            g2.drawString("Tiempo(ns)", left + width - 80, top + 20);
        }
    }
}
