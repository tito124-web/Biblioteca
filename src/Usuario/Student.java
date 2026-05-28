package Usuario;

public class Student extends User {

    // Estudiante solo puede tener 3 préstamos activos
    private static final int MAX_LOANS = 3;
    private String major; // carrera del estudiante

    // Constructor — llama al constructor de User con super()
    public Student(String name, String id, String major) {
        super(name, id);
        setMajor(major);
    }

    // Devuelve el máximo de préstamos permitidos para estudiante
    public int getMaxLoans()
    { return MAX_LOANS; }

    // Verifica si puede pedir prestado usando el máximo de estudiante
    public boolean canBorrow() {
        return super.canBorrow(MAX_LOANS);
    }

    public String getMajor() 
    { return major; }

    // Valida que la carrera no esté vacía
    public void setMajor(String major) {
        if (major == null || major.trim().isEmpty())
            throw new IllegalArgumentException("El campo no puede estar vacio");
        this.major = major.trim();
    }

    // Convierte el objeto a texto para guardar en CSV
    // Agrega STUDENT al inicio para identificarlo al cargar
    // Formato: STUDENT;name;id;activeLoans;major
    @Override
    public String toFile() {
        return "STUDENT;" + super.toFile() + ";" + major;
    }

    // Reconstruye un Student desde una línea del CSV
    // p[0]=STUDENT, p[1]=name, p[2]=id, p[3]=activeLoans, p[4]=major
    public static Student fromFile(String line) {
        String[] p = line.split(";");
        Student s = new Student(p[1], p[2], p[4]);
        s.activeLoans = Integer.parseInt(p[3]); // restaura los préstamos activos
        return s;
    }

    // Muestra la información del estudiante como texto
    @Override
    public String toString() {
        return "Student [name=" + name + ", id=" + id +
               ", major=" + major +
               ", loans=" + activeLoans + "/" + MAX_LOANS + "]";
    }
}