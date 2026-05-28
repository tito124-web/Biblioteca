package Usuario;

public class User {
	
// Creación de atributos en la clase padre
    protected String name;
    protected String id;
    protected int activeLoans;
    
// Creación de contructor, validando setter en el contructor para validaciones 
    public User(String name, String id) {
        setName(name);
        setId(id);
        this.activeLoans = 0;
    }

    // creacion de metodo de prestamo 
    public boolean canBorrow(int maxLoans) {
        return activeLoans < maxLoans;
    }

 // Aumenta el contador cuando se presta un material
    public void increaseLoan() { activeLoans++; }
    
 // Disminuye el contador cuando se devuelve un material
    // El if evita que baje de 0
    public void decreaseLoan() {
        if (activeLoans > 0) activeLoans--;
    }

    // Getters y Setters
    // En cada setters se validan los atributos 
    public String getName() { return name; }
    public void setName(String name) {
        if (name == null || name.trim().isEmpty())
            throw new IllegalArgumentException("El campo no puede estar vacio");
        this.name = name.trim();
    }

    public String getId() { return id; }
    public void setId(String id) {
        if (id == null || id.trim().isEmpty())
            throw new IllegalArgumentException("El campo no puede estar vacio");
        this.id = id.trim();
    }

    public int getActiveLoans() { return activeLoans; }

    // Convierte el objeto a texto para guardar en CSV
    // Formato: name;id;activeLoans
    public String toFile() {
        return name + ";" + id + ";" + activeLoans;
    }

    @Override
    public String toString() {
        return "User [name=" + name + ", id=" + id +
               ", loans=" + activeLoans + "]";
    }
}