package Usuario;

public class Student extends User {

    private static final int MAX_LOANS = 3;
    private String major;

    public Student(String name, String id, String major) {
        super(name, id);
        setMajor(major);
    }

    public int getMaxLoans() { return MAX_LOANS; }

    public boolean canBorrow() {
        return super.canBorrow(MAX_LOANS);
    }

    public String getMajor() { return major; }
    public void setMajor(String major) {
        if (major == null || major.trim().isEmpty())
            throw new IllegalArgumentException("El campo no puede estar vacio");
        this.major = major.trim();
    }

    @Override
    public String toFile() {
        return "STUDENT;" + super.toFile() + ";" + major;
    }

    public static Student fromFile(String line) {
        String[] p = line.split(";");
        Student s = new Student(p[1], p[2], p[4]);
        s.activeLoans = Integer.parseInt(p[3]);
        return s;
    }

    @Override
    public String toString() {
        return "Student [name=" + name + ", id=" + id +
               ", major=" + major +
               ", loans=" + activeLoans + "/" + MAX_LOANS + "]";
    }
}