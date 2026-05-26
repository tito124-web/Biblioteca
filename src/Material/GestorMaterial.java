package Material;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;

public class GestorMaterial {

    private ArrayList<Material> materiales;
    private static final Path ARCHIVO = Paths.get("materiales.csv");

    public GestorMaterial() {
        materiales = new ArrayList<>();
    }

    // Agregar material
    public void agregar(Material m) {
        materiales.add(m);
    }

    // Obtener lista
    public ArrayList<Material> getMateriales() {
        return materiales;
    }

    // Buscar por codigo
    public Material buscarPorCodigo(String code) {
        for (Material m : materiales) {
            if (m.getCode().equalsIgnoreCase(code)) {
                return m;
            }
        }
        return null;
    }

    // Buscar por titulo
    public ArrayList<Material> buscarPorTitulo(String title) {
        ArrayList<Material> resultado = new ArrayList<>();
        for (Material m : materiales) {
            if (m.getTitle().toLowerCase().contains(title.toLowerCase())) {
                resultado.add(m);
            }
        }
        return resultado;
    }

    // Verificar duplicado
    public boolean existeCodigo(String code) {
        return buscarPorCodigo(code) != null;
    }
    
 // Eliminar material por codigo
    public boolean eliminar(String code) {
        Material m = buscarPorCodigo(code);
        if (m != null) {
            materiales.remove(m);
            return true;
        }
        return false;
    }

    // Guardar ArrayList en CSV
    public void guardar() throws IOException {
        try (BufferedWriter out = Files.newBufferedWriter(ARCHIVO, StandardCharsets.UTF_8)) {
            // Encabezado del CSV
            out.write("TIPO;CODIGO;AÑO;TITULO;AUTOR;DISPONIBLE");
            out.newLine();
            // Datos
            for (Material m : materiales) {
                if (m instanceof Book) {
                    out.write(((Book) m).toArchivo());
                } else if (m instanceof Magazine) {
                    out.write(((Magazine) m).toArchivo());
                }
                out.newLine();
            }
        }
    }

    // Cargar CSV al ArrayList
    public void cargar() throws IOException {
        materiales.clear();
        if (!Files.isRegularFile(ARCHIVO)) return;

        try (BufferedReader in = Files.newBufferedReader(ARCHIVO, StandardCharsets.UTF_8)) {
            String linea;
            boolean primeraLinea = true;
            while ((linea = in.readLine()) != null) {
                // Salta la primera linea que es el encabezado
                if (primeraLinea) {
                    primeraLinea = false;
                    continue;
                }
                if (linea.isBlank()) continue;
                if (linea.startsWith("BOOK")) {
                    materiales.add(Book.fromArchivo(linea));
                } else if (linea.startsWith("MAGAZINE")) {
                    materiales.add(Magazine.fromArchivo(linea));
                }
            }
        }
    }

    // Listar todos 
    public void listarTodos() {
        if (materiales.isEmpty()) {
            System.out.println("No hay materiales registrados.");
            return;
        }
        int i = 1;
        for (Material m : materiales) {
            System.out.println(i + ". " + m);
            i++;
        }
    }
}