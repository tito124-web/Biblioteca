package Bitacora;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class GestorBitacora {

    private static final String ARCHIVO = "bitacora.csv";

    // Registrar acciones del sistema
    public static void registrar(String accion, String detalle) {

        try (BufferedWriter bw =
                new BufferedWriter(new FileWriter(ARCHIVO, true))) {

            bw.write(LocalDateTime.now()
                    + ";" + accion
                    + ";" + detalle);

            bw.newLine();

        } catch (IOException e) {

            System.out.println("Error al guardar bitácora.");

        }
    }
}