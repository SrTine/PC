package mensajes;

public class EnviarFichero extends Mensaje {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String rutaFichero;
	private String nombreFichero;
	
	public EnviarFichero(String origen, String destino, String rutaFichero, String nombreFichero, String usuarioID){
		super(origen, destino, "ENVIAR FICHERO");
		this.rutaFichero = rutaFichero;
		this.usuarioID = usuarioID;
		this.nombreFichero = nombreFichero;
	}

	public String getRutaFichero() {
		return this.rutaFichero;
	}
	
	public String getNombreFichero() {
		return this.nombreFichero;
	}
}
