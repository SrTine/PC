package mensajes;

public class IntercambioCS extends Mensaje {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int puerto;
	private String direccionIP;
	private String nombreFichero;

	
	public IntercambioCS(String origen, String destino, String direccionIP, int puerto, String nombreFichero){
		super(origen, destino,"INTERCAMBIO CLIENTE-SERVIDOR");
		this.usuarioID = origen;
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
