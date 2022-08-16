package mensajes;

public class IntercambioSC extends Mensaje{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String direccionIP;
	private int puerto;
	private String nombreFichero;
	
	public IntercambioSC(String direccionIP, int puerto, String nombreFichero){
		super(null, null,"INTERCAMBIO SERVIDOR-CLIENTE");
		this.direccionIP = direccionIP;
		this.puerto = puerto;
		this.nombreFichero = nombreFichero;
	}
	
	public int getPuerto() {
		return this.puerto;
	}
	
	public String getDireccionIP() {
		return this.direccionIP;
	}
	
	public String getNombreFichero() {
		return this.nombreFichero;
	}
}
