package prestamos;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;

import Material.GestorMaterial;
import Material.Material;
import Usuario.GestorUser;
import Usuario.Student;
import Usuario.Teacher;
import Usuario.User;

public class PrestamoDevolver {
	
	
	private GestorMaterial gestorMaterial;
	private GestorUser gestorUser;


    private ArrayList<Loan> loans;
    private static final Path FILE = Paths.get("loans.csv");
	
	
	

    // ← CORRECCIÓN 1: quitado el tercer parámetro
	public PrestamoDevolver(GestorMaterial gestorMaterial, GestorUser gestorUser) {
		this.loans = new ArrayList<>();
        this.gestorMaterial = gestorMaterial;
        this.gestorUser = gestorUser;
	}

	public String Loand(String materialCode, String userId) {
		
		 Material material = gestorMaterial.buscarPorCodigo(materialCode);
	        if (material == null)
	            return "Lo sentimos  Material no encontrado";
	        
	        if(!material.isDisponible() )
	        	return "Lo sentimos el material no esta disponible ";
	        
	        User user = gestorUser.findById(userId);
	        
	        if(user == null)
	        	return "Lo sentimos usuario no encontrado";
	        
	        int Max = (user instanceof Student)?
	        		((Student)user).getMaxLoans():
	        			((Teacher)user).getMaxLoans();
	        
	        if(!user.canBorrow(Max))
	        	return "Lo sentimos el usuario: " + user.getName() + " ya alcanso el maximo de " + Max + " de prestamos " ;
	        
	        material.setDisponible(false);
	        user.increaseLoan(); // ← CORRECCIÓN 2: era decreaseLoan
	        
	        String today   = LocalDate.now().toString();
	        String dueDate = LocalDate.now().plusDays(15).toString();

	        loans.add(new Loan(materialCode, userId, today, dueDate));

	        return "OK: Préstamo registrado hasta " + dueDate;
	}
	
	
	public String  returnLoan (String materialCode, String userId) {
		
		  Material material = gestorMaterial.buscarPorCodigo(materialCode);
        if (material == null)
            return "Lo sentimos material no encontrado ";

        if (material.isDisponible())
            return "Lo sentimos el material no está prestado";

        Loan active = null;
        for (Loan l : loans) {
            if (l.getMaterialCode().equals(materialCode)) {
                active = l;
                break;
            }
        }

        if (active == null)
            return "ERROR: Préstamo no encontrado";

        material.setDisponible(true);

        User user = gestorUser.findById(active.getUserId());
        if (user != null) user.decreaseLoan();

        loans.remove(active);

        return "OK: Material devuelto correctamente";
    }

    public ArrayList<Loan> getLoansByUser(String userId) {
        ArrayList<Loan> result = new ArrayList<>();
        for (Loan l : loans) {
            if (l.getUserId().equals(userId)) {
                result.add(l);
            }
        }
        return result;
    }

    public ArrayList<Loan> getLoans() {
        return loans;
    }

    public void save() throws IOException {
        try (BufferedWriter out = Files.newBufferedWriter(FILE, StandardCharsets.UTF_8)) {
            out.write("MATERIAL_CODE;USER_ID;LOAN_DATE;DUE_DATE");
            out.newLine();
            for (Loan l : loans) {
                out.write(l.toFile());
                out.newLine();
            }
        }
    }

    public void load() throws IOException {
        loans.clear();
        if (!Files.isRegularFile(FILE)) return;

        try (BufferedReader in = Files.newBufferedReader(FILE, StandardCharsets.UTF_8)) {
            String line;
            boolean first = true;
            while ((line = in.readLine()) != null) {
                if (first) { first = false; continue; }
                if (line.isBlank()) continue;
                loans.add(Loan.fromFile(line));
            }
        }
    }
}