package models;

// Clase que representa los resultados de ejecución de un algoritmo
public class AlgorithmResult {
    private String algorithm;
    private int pathLength;
    private long time;

    public AlgorithmResult(String algorithm, int pathLength, long time) {
        this.algorithm = algorithm;
        this.pathLength = pathLength;
        this.time = time;
    }

    public String getAlgorithm() { return algorithm; }
    public int getPathLength() { return pathLength; }
    public long getTime() { return time; }

    public void setPathLength(int l) { this.pathLength = l; }
    public void setTime(long t) { this.time = t; }
}
