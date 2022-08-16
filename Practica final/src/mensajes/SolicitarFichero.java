package mensajes;

public class SolicitarFichero extends Mensaje {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String nombreFichero;
	
	public SolicitarFichero(String usuarioID, String origen, String destino, String nombreFichero){
		super(origen, destino, "SOLICITAR FICHERO");
		this.nombreFichero = nombreFichero;
		this.usuarioID = usuarioID;
	}
	
	public String getNombreFichero() {
		return nombreFichero;
	}
}
