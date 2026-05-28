package Material;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;

public class GestorMaterial {

    // Lista que guarda todos los materiales en memoria
    // Puede guardar Book y Magazine porque ambos son hijos de Material (polimorfismo)
    private ArrayList<Material> materials;

    // Ruta del archivo CSV donde se guardan los materiales
    private static final Path FILE = Paths.get("materiales.csv");

    // Constructor — inicia la lista vacía
    public GestorMaterial() {
        materials = new ArrayList<>();
    }

    // Agrega un material a la lista en memoria
    public void add(Material m) {
        materials.add(m);
    }

    // Devuelve toda la lista de materiales
    public ArrayList<Material> getMaterials() {
        return materials;
    }

    // Recorre la lista buscando el material por su código
    // equalsIgnoreCase ignora mayúsculas — "LIB-001" es igual a "lib-001"
    // Devuelve null si no lo encuentra
    public Material findByCode(String code) {
        for (Material m : materials) {
            if (m.getCode().equalsIgnoreCase(code)) {
                return m;
            }
        }
        return null;
    }

    // Busca materiales cuyo título contenga el texto buscado
    // Útil para el buscador de la ventana Catálogo
    public ArrayList<Material> findByTitle(String title) {
        ArrayList<Material> result = new ArrayList<>();
        for (Material m : materials) {
            if (m.getTitle().toLowerCase().contains(title.toLowerCase())) {
                result.add(m);
            }
        }
        return result;
    }

    // Verifica si ya existe un material con ese código
    // Reutiliza findByCode para no duplicar código
    public boolean existsCode(String code) {
        return findByCode(code) != null;
    }

    // Elimina un material de la lista buscándolo por código
    // Devuelve true si lo encontró y eliminó, false si no existe
    public boolean remove(String code) {
        Material m = findByCode(code);
        if (m != null) {
            materials.remove(m);
            return true;
        }
        return false;
    }

    // Guarda todos los materiales de la lista en el archivo CSV
    // El try() cierra el archivo automáticamente aunque haya error
    public void save() throws IOException {
        try (BufferedWriter out = Files.newBufferedWriter(FILE, StandardCharsets.UTF_8)) {

            // Primera línea del CSV — describe las columnas
            out.write("TIPO;CODIGO;AÑO;TITULO;AUTOR;DISPONIBLE");
            out.newLine();

            // Recorre la lista y escribe cada material en una línea
            // instanceof detecta si es Book o Magazine para llamar su toArchivo()
            for (Material m : materials) {
                if (m instanceof Book) {
                    out.write(((Book) m).toArchivo());
                } else if (m instanceof Magazine) {
                    out.write(((Magazine) m).toArchivo());
                }
                out.newLine();
            }
        }
    }

    // Carga los materiales del CSV a la lista en memoria
    public void load() throws IOException {

        // Limpia la lista para no duplicar datos
        materials.clear();

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
                if (line.startsWith("BOOK")) {
                    materials.add(Book.fromArchivo(line)); // reconstruye un Book
                } else if (line.startsWith("MAGAZINE")) {
                    materials.add(Magazine.fromArchivo(line)); // reconstruye un Magazine
                }
            }
        }
    }

    // Imprime todos los materiales en la consola
    // Útil para depurar y verificar que los datos están bien
    public void listAll() {
        if (materials.isEmpty()) {
            System.out.println("No materials registered.");
            return;
        }
        int i = 1;
        for (Material m : materials) {
            System.out.println(i + ". " + m); // llama toString() de cada material
            i++;
        }
    }
}