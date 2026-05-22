package Material;

public abstract class Material {

	protected String code;
	protected String year;
	protected String title;
	protected String author;
	protected boolean available;
	
	public Material(String code, String year, String title, String author) {
	    setCode(code);
	    setTitle(title);
	    setAuthor(author);
	    setYear(year);
	    this.available = true;
	}
	
	//Es el metodo abstracto para que los hijos lo modifiquen a su manera 
	public abstract double calcularmulta(int diasRetraso);
	

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		if (code == null || code.trim().isEmpty()) 
			throw new IllegalArgumentException ("Lo sentimos el campo debe estar lleno");
			this.code = code.trim();
		}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		if(year == null || year.trim().isEmpty())
			throw new IllegalArgumentException ("Lo sentimos el campo debe de estar lleno");
		this.year = year.trim();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		if(title == null || title.trim().isEmpty())
			throw new IllegalArgumentException ("Lo sentimos el campo debe de estar lleno");
		this.title = title.trim();
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		if(author == null || author.trim().isEmpty())
			throw new IllegalArgumentException ("Lo sentimos el campo debe de estar lleno ");
		this.author = author.trim();
	}

	public boolean isDisponible() {
		return available;
	}

	public void setDisponible(boolean available) {
		this.available = available;
	}

	@Override
	public String toString() {
		return "Material [codigo=" + code + ",año=" + year + ", titulo=" + title + ", autor=" + author + ", disponible="
				+ available + "]";
	}
	
	
	
	
	
	
	
	
	
}
