package models;

// Clase para almacenar resultados detallados de la resolución del laberinto
public class SolveResults {
    private String algorithmName;
    private int stepsTaken;
    private long executionTimeMillis;

    public SolveResults(String algorithmName, int stepsTaken, long executionTimeMillis) {
        this.algorithmName = algorithmName;
        this.stepsTaken = stepsTaken;
        this.executionTimeMillis = executionTimeMillis;
    }

    public String getAlgorithmName() {
        return algorithmName;
    }

    public int getStepsTaken() {
        return stepsTaken;
    }

    public long getExecutionTimeMillis() {
        return executionTimeMillis;
    }

    public void setAlgorithmName(String algorithmName) {
        this.algorithmName = algorithmName;
    }

    public void setStepsTaken(int stepsTaken) {
        this.stepsTaken = stepsTaken;
    }

    public void setExecutionTimeMillis(long executionTimeMillis) {
        this.executionTimeMillis = executionTimeMillis;
    }
}
