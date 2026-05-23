package Usuario;

public class Teacher extends User {

    private static final int MAX_LOANS = 5;
    private String department;

    public Teacher(String name, String id, String department) {
        super(name, id);
        setDepartment(department);
    }

    public int getMaxLoans() { return MAX_LOANS; }

    public boolean canBorrow() {
        return super.canBorrow(MAX_LOANS);
    }

    public String getDepartment() { return department; }
    public void setDepartment(String department) {
        if (department == null || department.trim().isEmpty())
            throw new IllegalArgumentException("El campo no puede estar vacio");
        this.department = department.trim();
    }

    @Override
    public String toFile() {
        return "TEACHER;" + super.toFile() + ";" + department;
    }

    public static Teacher fromFile(String line) {
        String[] p = line.split(";");
        Teacher t = new Teacher(p[1], p[2], p[4]);
        t.activeLoans = Integer.parseInt(p[3]);
        return t;
    }

    @Override
    public String toString() {
        return "Teacher [name=" + name + ", id=" + id +
               ", department=" + department +
               ", loans=" + activeLoans + "/" + MAX_LOANS + "]";
    }
}