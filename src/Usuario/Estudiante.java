package Usuario;

public class Estudiante extends Usuario {

    private static final int MAX_PRESTAMOS = 3;

    public Estudiante(String carnet, String nombre) {
        super(carnet, nombre);
    }

    @Override
    public boolean puedePrestar() {
        return prestamosActivos < MAX_PRESTAMOS;
    }

    @Override
    public String toString() {
        return "ESTUDIANTE -> " + super.toString();
    }
}