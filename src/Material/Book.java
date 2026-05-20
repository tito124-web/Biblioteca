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
		
		return super.toString();
	}
	
	

}
