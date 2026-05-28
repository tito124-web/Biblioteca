	package prestamos;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;

import Material.GestorMaterial;
import Material.Material;
import Usuario.GestorUser;
import Usuario.Student;
import Usuario.Teacher;
import Usuario.User;

// Clase que maneja toda la lógica de préstamos y devoluciones
// Reutiliza GestorMaterial y GestorUser para no duplicar código
public class PrestamoDevolver {

    // Referencia al gestor de materiales para buscar y actualizar disponibilidad
    private GestorMaterial gestorMaterial;

    // Referencia al gestor de usuarios para buscar y actualizar préstamos activos
    private GestorUser gestorUser;

    // Lista de préstamos activos en memoria
    private ArrayList<Loan> loans;

    // Ruta del archivo CSV donde se guardan los préstamos
    private static final Path FILE = Paths.get("loans.csv");

    // Constructor — recibe los gestores ya creados en MainFrame
    // No crea nuevos gestores para no perder los datos ya cargados
    public PrestamoDevolver(GestorMaterial gestorMaterial, GestorUser gestorUser) {
        this.loans = new ArrayList<>();
        this.gestorMaterial = gestorMaterial;
        this.gestorUser = gestorUser;
    }

    // Registra un préstamo con todas sus validaciones
    public String Loand(String materialCode, String userId) {

        // Validación 1  verifica que el material exista
        Material material = gestorMaterial.findByCode(materialCode); // ← cambiado
        if (material == null)
            return "Lo sentimos  Material no encontrado";

        // Validación 2  verifica que el material esté disponible
        if (!material.isDisponible())
            return "Lo sentimos el material no esta disponible ";

        // Validación 3  verifica que el usuario exista
        User user = gestorUser.findById(userId);
        if (user == null)
            return "Lo sentimos usuario no encontrado";

        // Validación 4  obtiene el máximo según el tipo de usuario
        // Student puede tener 3, Teacher puede tener 5
        int Max = (user instanceof Student) ?
                ((Student) user).getMaxLoans() :
                ((Teacher) user).getMaxLoans();

        // Verifica que el usuario no haya alcanzado su límite
        if (!user.canBorrow(Max))
            return "Lo sentimos el usuario: " + user.getName() + " ya alcanso el maximo de " + Max + " de prestamos ";

        // Todo OK — marca el material como no disponible
        material.setDisponible(false);

        // Aumenta el contador de préstamos del usuario
        user.increaseLoan();

        // Registra la fecha de hoy y la fecha límite (15 días)
        String today   = LocalDate.now().toString();
        String dueDate = LocalDate.now().plusDays(15).toString();

        // Agrega el préstamo a la lista
        loans.add(new Loan(materialCode, userId, today, dueDate));

        return "OK: Préstamo registrado hasta " + dueDate;
    }

 // Registra la devolución de un material
    public String returnLoan(String materialCode, String userId) {

        // Validación 1  verifica que el material exista
        Material material = gestorMaterial.findByCode(materialCode); 
        if (material == null)
            return "Lo sentimos material no encontrado ";

        // Validación 2  verifica que el material esté prestado
        if (material.isDisponible())
            return "Lo sentimos el material no está prestado";

        // Busca el préstamo activo del material en la lista
        Loan active = null;
        for (Loan l : loans) {
            if (l.getMaterialCode().equals(materialCode)) {
                active = l;
                break; // encontrado, sale del ciclo
            }
        }

        // Si no encuentra el préstamo en la lista
        if (active == null)
            return "ERROR: Préstamo no encontrado";

        // CÓDIGO DE POLIMORFISMO PARA CALCULAR LA MULTA
        double multa = 0;
        try {
            LocalDate fechaVencimiento = LocalDate.parse(active.getDueDate());
            LocalDate fechaHoy = LocalDate.now();
            
            // Si la fecha actual supera el día límite, se genera retraso
            if (fechaHoy.isAfter(fechaVencimiento)) {
                long diferenciaDias = java.time.temporal.ChronoUnit.DAYS.between(fechaVencimiento, fechaHoy);
                // Polimorfismo puro: calcula según sea libro o revista
                multa = material.calcularmulta((int) diferenciaDias);
            }
        } catch (Exception e) {
            System.out.println("No se pudo procesar la fecha de la multa.");
        }

        // Marca el material como disponible de nuevo
        material.setDisponible(true);

        // Disminuye el contador de préstamos del usuario
        User user = gestorUser.findById(active.getUserId());
        if (user != null) user.decreaseLoan();

        // Elimina el préstamo de la lista
        loans.remove(active);

        // Si hay una multa acumulada, se lo advertimos a la interfaz visual
        if (multa > 0) {
            return "OK: Material devuelto. ¡Usuario tiene una multa acumulada de: Q" + String.format("%.2f", multa) + "!";
        }

        return "OK: Material devuelto correctamente";
    }

    // Devuelve todos los préstamos activos de un usuario específico
    // Se usa en VentanaUsuarios para mostrar qué tiene prestado
    public ArrayList<Loan> getLoansByUser(String userId) {
        ArrayList<Loan> result = new ArrayList<>();
        for (Loan l : loans) {
            if (l.getUserId().equals(userId)) {
                result.add(l);
            }
        }
        return result;
    }

    // Devuelve todos los préstamos activos
    // Se usa en VentanaCatalogo para mostrar quién tiene cada material
    public ArrayList<Loan> getLoans() {
        return loans;
    }

    // Guarda todos los préstamos de la lista en el archivo CSV
    public void save() throws IOException {
        try (BufferedWriter out = Files.newBufferedWriter(FILE, StandardCharsets.UTF_8)) {

            // Primera línea del CSV — describe las columnas
            out.write("MATERIAL_CODE;USER_ID;LOAN_DATE;DUE_DATE");
            out.newLine();

            // Escribe cada préstamo en una línea
            for (Loan l : loans) {
                out.write(l.toFile());
                out.newLine();
            }
        }
    }

    // Carga los préstamos del CSV a la lista en memoria
    public void load() throws IOException {

        // Limpia la lista para no duplicar datos
        loans.clear();

        // Si el archivo no existe termina sin error
        if (!Files.isRegularFile(FILE)) return;

        try (BufferedReader in = Files.newBufferedReader(FILE, StandardCharsets.UTF_8)) {
            String line;
            boolean first = true;

            // Lee línea por línea
            while ((line = in.readLine()) != null) {

                // Salta la primera línea porque es el encabezado
                if (first) { first = false; continue; }

                // Salta líneas vacías
                if (line.isBlank()) continue;

                // Reconstruye cada préstamo desde la línea del CSV
                loans.add(Loan.fromFile(line));
            }
        }
    }
}