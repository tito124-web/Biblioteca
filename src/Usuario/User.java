package Usuario;

public class User {

    protected String name;
    protected String id;
    protected int activeLoans;

    public User(String name, String id) {
        setName(name);
        setId(id);
        this.activeLoans = 0;
    }

    public boolean canBorrow(int maxLoans) {
        return activeLoans < maxLoans;
    }

    public void increaseLoan() { activeLoans++; }
    public void decreaseLoan() {
        if (activeLoans > 0) activeLoans--;
    }

    // Getters y Setters
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

    // Para CSV
    public String toFile() {
        return name + ";" + id + ";" + activeLoans;
    }

    @Override
    public String toString() {
        return "User [name=" + name + ", id=" + id +
               ", loans=" + activeLoans + "]";
    }
}