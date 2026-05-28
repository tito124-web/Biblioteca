package prestamos;

// Representa un préstamo — guarda solo los códigos para no duplicar datos
public class Loan {

    // Solo guarda referencias — no copia datos del material ni del usuario
    private String materialCode; // código del material prestado ej: "LIB-001"
    private String userId;       // carnet del usuario ej: "25-21930"
    private String loanDate;     // fecha en que se prestó ej: "2026-05-25"
    private String dueDate;      // fecha límite para devolver ej: "2026-06-09"

    // Constructor crea el préstamo con los 4 datos
    public Loan(String materialCode, String userId, String loanDate, String dueDate) {
        this.materialCode = materialCode;
        this.userId = userId;
        this.loanDate = loanDate;
        this.dueDate = dueDate;
    }

    // Getters  permiten leer los datos del préstamo
    public String getMaterialCode() {
        return materialCode;
    }

    public String getUserId() {
        return userId;
    }

    public String getLoanDate() {
        return loanDate;
    }

    public String getDueDate() {
        return dueDate;
    }

    // Convierte el préstamo a texto para guardar en CSV
    // Formato materialCode;userId;loanDate;dueDate
    public String toFile() {
        return materialCode + ";" + userId + ";" + loanDate + ";" + dueDate;
    }

    // Reconstruye un Loan desde una línea del CSV
    // p[0]=materialCode, p[1]=userId, p[2]=loanDate, p[3]=dueDate
    public static Loan fromFile(String line) {
        String[] p = line.split(";");
        return new Loan(p[0], p[1], p[2], p[3]);
    }

    // Muestra la información del préstamo como texto
    @Override
    public String toString() {
        return "Loan [material=" + materialCode +
               ", user=" + userId +
               ", loanDate=" + loanDate +
               ", dueDate=" + dueDate + "]";
    }
}