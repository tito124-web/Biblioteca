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
		
		return super.toString();
	}

	
	
}
