package Usuario;

public class Docente extends Usuario {

    private static final int MAX_PRESTAMOS = 5;

    public Docente(String carnet, String nombre) {
        super(carnet, nombre);
    }

    @Override
    public boolean puedePrestar() {
        return prestamosActivos < MAX_PRESTAMOS;
    }

    @Override
    public String toString() {
        return "DOCENTE -> " + super.toString();
    }
}