package Usuario;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;

public class GestorUser {

    private ArrayList<User> users;
    private static final Path FILE = Paths.get("users.csv");

    public GestorUser() {
        users = new ArrayList<>();
    }

    // ─── OPERACIONES DE LISTA ───

    // Agregar usuario
    public void add(User u) {
        users.add(u);
    }

    // Obtener lista completa
    public ArrayList<User> getUsers() {
        return users;
    }

    // Buscar por ID
    public User findById(String id) {
        for (User u : users) {
            if (u.getId().equalsIgnoreCase(id)) {
                return u;
            }
        }
        return null;
    }

    // Buscar por nombre
    public ArrayList<User> findByName(String name) {
        ArrayList<User> result = new ArrayList<>();
        for (User u : users) {
            if (u.getName().toLowerCase().contains(name.toLowerCase())) {
                result.add(u);
            }
        }
        return result;
    }

    // Verificar si ID ya existe
    public boolean existsId(String id) {
        return findById(id) != null;
    }

    // Eliminar usuario por ID
    public boolean remove(String id) {
        User u = findById(id);
        if (u != null) {
            users.remove(u);
            return true;
        }
        return false;
    }

    // ─── PERSISTENCIA CSV ──

    // Guardar ArrayList en CSV
    public void save() throws IOException {
        try (BufferedWriter out = Files.newBufferedWriter(FILE, StandardCharsets.UTF_8)) {
            // Encabezado
            out.write("TYPE;NAME;ID;ACTIVE_LOANS;EXTRA");
            out.newLine();
            // Datos
            for (User u : users) {
                out.write(u.toFile());
                out.newLine();
            }
        }
    }

    // Cargar CSV al ArrayList
    public void load() throws IOException {
        users.clear();
        if (!Files.isRegularFile(FILE)) return;

        try (BufferedReader in = Files.newBufferedReader(FILE, StandardCharsets.UTF_8)) {
            String line;
            boolean firstLine = true;
            while ((line = in.readLine()) != null) {
                // Salta el encabezado
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                if (line.isBlank()) continue;
                if (line.startsWith("STUDENT")) {
                    users.add(Student.fromFile(line));
                } else if (line.startsWith("TEACHER")) {
                    users.add(Teacher.fromFile(line));
                }
            }
        }
    }

    // ─── REPORTE ──

    // Listar todos en consola
    public void listAll() {
        if (users.isEmpty()) {
            System.out.println("No users registered.");
            return;
        }
        int i = 1;
        for (User u : users) {
            System.out.println(i + ". " + u);
            i++;
        }
    }
}
