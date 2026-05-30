package material;

// Book es un tipo de Material — hereda todos sus atributos y métodos
public class Book extends Material {

    // Constructor  llama al constructor de Material con super()
    public Book(String code, String year, String title, String author) {
        super(code, year, title, author);
    }

    // Implementa el método abstracto de Material
    // Libro cobra Q2.50 por cada día de retraso
    @Override
    public double calcularmulta(int diasRetraso) {
        return diasRetraso * 2.50;
    }

    // Muestra la información del libro como texto
    @Override
    public String toString() {
        return "Book [code=" + code + ", year=" + year + ", title=" + title + ", author=" + author + ", available="
                + available + "]";
    }

    // Convierte el objeto a texto para guardar en CSV
    // Agrega BOOK al inicio para identificarlo al cargar
    // Formato: BOOK;code;year;title;author;available
    public String toArchivo() {
        return "BOOK;" + code + ";" + year + ";" + title + ";" + author + ";" + available;
    }

    // Reconstruye un Book desde una línea del CSV
    // p[0]=BOOK, p[1]=code, p[2]=year, p[3]=title, p[4]=author, p[5]=available
    public static Book fromArchivo(String linea) {
        String[] p = linea.split(";");
        Book b = new Book(p[1], p[2], p[3], p[4]);
        b.setDisponible(Boolean.parseBoolean(p[5])); // restaura si está disponible o no
        return b;
    }
}