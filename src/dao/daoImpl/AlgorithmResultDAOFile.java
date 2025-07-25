package dao.daoImpl;

import dao.AlgorithmResultDAO;
import models.AlgorithmResult;

import java.io.*;
import java.util.*;

// Implementación concreta del DAO utilizando archivo CSV
public class AlgorithmResultDAOFile implements AlgorithmResultDAO {
    private static final String ARCHIVO = "results.csv";

    @Override
    public void guardar(AlgorithmResult resultado) {
        List<AlgorithmResult> resultados = obtenerTodos();
        boolean actualizado = false;

        for (AlgorithmResult r : resultados) {
            if (r.getAlgorithm().equals(resultado.getAlgorithm())) {
                r.setPathLength(resultado.getPathLength());
                r.setTime(resultado.getTime());
                actualizado = true;
                break;
            }
        }

        if (!actualizado) resultados.add(resultado);

        try (PrintWriter pw = new PrintWriter(new FileWriter(ARCHIVO))) {
            for (AlgorithmResult r : resultados) {
                pw.println(r.getAlgorithm() + "," + r.getPathLength() + "," + r.getTime());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<AlgorithmResult> obtenerTodos() {
        List<AlgorithmResult> lista = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");
                lista.add(new AlgorithmResult(partes[0], Integer.parseInt(partes[1]), Long.parseLong(partes[2])));
            }
        } catch (IOException e) {
            // Archivo puede no existir todavía
        }
        return lista;
    }

    @Override
    public void limpiar() {
        try (PrintWriter pw = new PrintWriter(ARCHIVO)) {
            // Limpia el archivo sobrescribiéndolo vacío
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
