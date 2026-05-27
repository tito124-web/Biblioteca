package prestamos;





public class Loan {

    private String materialCode;
    private String userId;
    private String loanDate;
    private String dueDate;

    public Loan(String materialCode, String userId, String loanDate, String dueDate) {
        this.materialCode = materialCode;
        this.userId = userId;
        this.loanDate = loanDate;
        this.dueDate = dueDate;
    }

    
    

    
    

    // Getters  
	public String getMaterialCode() {
		return materialCode;
	}







	public String getUserId() {
		return userId;
	}







	public String getLoanDate() {
		return loanDate;
	}







	public String getDueDate() {
		return dueDate;
	}


	// Para guardar en CSV
    public String toFile() {
        return materialCode + ";" + userId + ";" + loanDate + ";" + dueDate;}




	// Para cargar desde CSV
    public static Loan fromFile(String line) {
        String[] p = line.split(";");
        return new Loan(p[0], p[1], p[2], p[3]);
    }

    @Override
    public String toString() {
        return "Loan [material=" + materialCode +
               ", user=" + userId +
               ", loanDate=" + loanDate +
               ", dueDate=" + dueDate + "]";
    }
}
