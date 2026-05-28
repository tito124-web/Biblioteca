package Usuario;

public class Teacher extends User {

    // Docente puede tener hasta 5 préstamos activos
    private static final int MAX_LOANS = 5;
    private String department; // departamento del docente

    // Constructor — llama al constructor de User con super()
    // Se validan los setters en el contructor 
    public Teacher(String name, String id, String department) {
        super(name, id);
        setDepartment(department);
    }

    // Devuelve el máximo de préstamos permitidos para docente
    public int getMaxLoans() { return MAX_LOANS; }

    // Verifica si puede pedir prestado usando el máximo de docente
    public boolean canBorrow() {
        return super.canBorrow(MAX_LOANS);
    }

    public String getDepartment() { return department; }

    // Valida que el departamento no esté vacío
    public void setDepartment(String department) {
        if (department == null || department.trim().isEmpty())
            throw new IllegalArgumentException("El campo no puede estar vacio");
        this.department = department.trim();
    }

    // Convierte el objeto a texto para guardar en CSV
    // Agrega TEACHER al inicio para identificarlo al cargar
    // Formato: TEACHER;name;id;activeLoans;department
    @Override
    public String toFile() {
        return "TEACHER;" + super.toFile() + ";" + department;
    }

    // Reconstruye un Teacher desde una línea del CSV
    // p[0]=TEACHER, p[1]=name, p[2]=id, p[3]=activeLoans, p[4]=department
    public static Teacher fromFile(String line) {
        String[] p = line.split(";");
        Teacher t = new Teacher(p[1], p[2], p[4]);
        t.activeLoans = Integer.parseInt(p[3]); // restaura los préstamos activos
        return t;
    }

    // Muestra la información del docente como texto
    @Override
    public String toString() {
        return "Teacher [name=" + name + ", id=" + id +
               ", department=" + department +
               ", loans=" + activeLoans + "/" + MAX_LOANS + "]";
    }
}