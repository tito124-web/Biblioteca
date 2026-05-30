package material;

// Clase abstracta  no se puede instanciar directamente
// Solo sirve como base para Book y Magazine
public abstract class Material {

    // Atributos protegidos para que los hijos puedan accederlos directamente
    protected String code;
    protected String year;
    protected String title;
    protected String author;
    protected boolean available;

    // Constructor al crear cualquier material empieza disponible
    // Usa los setters para validar cada campo
    public Material(String code, String year, String title, String author) {
        setCode(code);
        setTitle(title);
        setAuthor(author);
        setYear(year);
        this.available = true; // todo material inicia disponible
    }

    // Método abstracto  cada hijo lo implementa a su manera
    // Book cobra Q2.50 por día, Magazine cobra Q1.50 por día
    public abstract double calcularmulta(int diasRetraso);

    public String getCode() {
        return code;
    }

    // Valida que el código no esté vacío antes de asignarlo
    public void setCode(String code) {
        if (code == null || code.trim().isEmpty())
            throw new IllegalArgumentException("Lo sentimos el campo debe estar lleno");
        this.code = code.trim();
    }

    public String getYear() {
        return year;
    }

    // Valida que el año no esté vacío
    public void setYear(String year) {
        if (year == null || year.trim().isEmpty())
            throw new IllegalArgumentException("Lo sentimos el campo debe de estar lleno");
        this.year = year.trim();
    }

    public String getTitle() {
        return title;
    }

    // Valida que el título no esté vacío
    public void setTitle(String title) {
        if (title == null || title.trim().isEmpty())
            throw new IllegalArgumentException("Lo sentimos el campo debe de estar lleno");
        this.title = title.trim();
    }

    public String getAuthor() {
        return author;
    }

    // Valida que el autor no esté vacío
    public void setAuthor(String author) {
        if (author == null || author.trim().isEmpty())
            throw new IllegalArgumentException("Lo sentimos el campo debe de estar lleno ");
        this.author = author.trim();
    }

    // Devuelve true si está disponible, false si está prestado
    public boolean isDisponible() {
        return available;
    }

    // Cambia el estado — false cuando se presta, true cuando se devuelve
    public void setDisponible(boolean available) {
        this.available = available;
    }

    // Muestra la información del material como texto
    @Override
    public String toString() {
        return "Material [codigo=" + code + ",año=" + year + ", titulo=" + title + ", autor=" + author + ", disponible="
                + available + "]";
    }
}