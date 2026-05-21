package Material;

public class Magazine extends Material {

	public Magazine(String code, String year, String title, String author) {
		super(code, year, title, author);
	}

	@Override
	public double calcularmulta(int diasRetraso) {
		return diasRetraso * 1.50;
	}

	
	
	@Override
	public String toString() {
		return "Magazine [code=" + code + ", year=" + year + ", title=" + title + ", author=" + author + ", available="
				+ available + "]";
	}
	
	  // Convierte el objeto a texto para guardar
    public String toArchivo() {
        return "MAGAZINE;" + code + ";" + year + ";" + title + ";" + author + ";" + available;
    }

	 // Reconstruye el objeto desde una línea del archivo
    public static Magazine fromArchivo(String linea) {
        String[] p = linea.split(";");
        Magazine m = new Magazine(p[1], p[2], p[3], p[4]);
        m.setDisponible(Boolean.parseBoolean(p[5]));
        return m;
    }
	
}
