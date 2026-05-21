package Material;

public class Book extends Material{

	public Book(String code, String year, String title, String author) {
		super(code, year, title, author);
	}

	@Override
	public double calcularmulta( int diasRetraso) {
		return diasRetraso * 2.50;
	}

	@Override
	public String toString() {
		return "Book [code=" + code + ", year=" + year + ", title=" + title + ", author=" + author + ", available="
				+ available + "]";
	}
	
	
	public String toArchivo() {
	    return "BOOK;" + code + ";" + year + ";" + title + ";" + author + ";" + available;
	}
	
	// Reconstruye el objeto desde una línea del archivo
    public static Book fromArchivo(String linea) {
        String[] p = linea.split(";");
        Book b = new Book(p[1], p[2], p[3], p[4]);
        b.setDisponible(Boolean.parseBoolean(p[5]));
        return b;
    }


}
