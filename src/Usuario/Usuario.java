package Usuario;

public class Usuario {

    protected String carnet;
    protected String nombre;
    protected int prestamosActivos;

   
    public Usuario(String carnet, String nombre) {
        this.carnet = carnet;
        this.nombre = nombre;
        this.prestamosActivos = 0;
    }

   
    public String getCarnet() {
        return carnet;
    }

    public String getNombre() {
        return nombre;
    }

    public int getPrestamosActivos() {
        return prestamosActivos;
    }

    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // Método pa prestar
    public boolean puedePrestar() {
        return true;
    }

    public void agregarPrestamo() {
        prestamosActivos++;
    }

    public void devolverPrestamo() {
        if (prestamosActivos > 0) {
            prestamosActivos--;
        }
    }

    @Override
    public String toString() {
        return "Carnet: " + carnet +
               ", Nombre: " + nombre +
               ", Prestamos Activos: " + prestamosActivos;
    }
}