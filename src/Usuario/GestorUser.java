package Usuario;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;

public class GestorUser {

    // Lista que guarda todos los usuarios en memoria mientras el programa corre
    // Puede guardar Student y Teacher porque ambos son hijos de User (polimorfismo)
    private ArrayList<User> users;

    // Ruta del archivo CSV donde se guardan los usuarios
    private static final Path FILE = Paths.get("users.csv");

    // Constructor — inicia la lista vacía
    public GestorUser() {
        users = new ArrayList<>();
    }

    // OPERACIONES DE LISTA

    // Agrega un usuario a la lista en memoria
    public void add(User u) {
        users.add(u);
    }

    // Devuelve toda la lista de usuarios
    public ArrayList<User> getUsers() {
        return users;
    }

    // Recorre la lista buscando el usuario por su ID
    // equalsIgnoreCase ignora mayúsculas — "A001" es igual a "a001"
    // Devuelve null si no lo encuentra
    public User findById(String id) {
        for (User u : users) {
            if (u.getId().equalsIgnoreCase(id)) {
                return u;
            }
        }
        return null;
    }

    // Busca usuarios cuyo nombre contenga el texto buscado
    // toLowerCase permite buscar sin importar mayúsculas
    // Devuelve una lista con todos los que coincidan
    public ArrayList<User> findByName(String name) {
        ArrayList<User> result = new ArrayList<>();
        for (User u : users) {
            if (u.getName().toLowerCase().contains(name.toLowerCase())) {
                result.add(u);
            }
        }
        return result;
    }

    // Verifica si ya existe un usuario con ese ID
    // Reutilizamos findById para no duplicar código
    public boolean existsId(String id) {
        return findById(id) != null;
    }

    // Elimina un usuario de la lista buscándolo por ID
    // Devuelve true si lo encontró y eliminó, false si no existe
    public boolean remove(String id) {
        User u = findById(id);
        if (u != null) {
            users.remove(u);
            return true;
        }
        return false;
    }

    //  PERSISTENCIA CSV 

    // Guarda todos los usuarios de la lista en el archivo CSV
    // El try() cierra el archivo automáticamente aunque haya error
    public void save() throws IOException {
        try (BufferedWriter out = Files.newBufferedWriter(FILE, StandardCharsets.UTF_8)) {

            // Primera línea del CSV — describe las columnas
            out.write("TYPE;NAME;ID;ACTIVE_LOANS;EXTRA");
            out.newLine();

            // Recorre la lista y escribe cada usuario en una línea
            // toFile() es polimórfico — llama Student.toFile() o Teacher.toFile() según corresponda
            for (User u : users) {
                out.write(u.toFile());
                out.newLine();
            }
        }
    }

    // Carga los usuarios del CSV a la lista en memoria
    public void load() throws IOException {

        // Limpia la lista para no duplicar datos
        users.clear();

        // Si el archivo no existe termina sin error
        // Esto pasa la primera vez que se ejecuta el programa
        if (!Files.isRegularFile(FILE)) return;

        try (BufferedReader in = Files.newBufferedReader(FILE, StandardCharsets.UTF_8)) {
            String line;
            boolean firstLine = true;

            // Lee línea por línea hasta que no haya más
            while ((line = in.readLine()) != null) {

                // Salta la primera línea porque es el encabezado
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                // Salta líneas vacías
                if (line.isBlank()) continue;

                // Revisa cómo empieza la línea para saber qué objeto crear
                if (line.startsWith("STUDENT")) {
                    users.add(Student.fromFile(line)); // reconstruye un Student
                } else if (line.startsWith("TEACHER")) {
                    users.add(Teacher.fromFile(line)); // reconstruye un Teacher
                }
            }
        }
    }

    //  REPORTE 

    // Imprime todos los usuarios en la consola
    public void listAll() {
        if (users.isEmpty()) {
            System.out.println("Usuario no registrado.");
            return;
        }
        int i = 1;
        for (User u : users) {
            System.out.println(i + ". " + u); // llama toString() de cada usuario
            i++;
        }
    }
}