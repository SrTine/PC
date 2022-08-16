package mensajes;

public class AnadirFichero extends Mensaje {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String nombreFichero;
	private String rutaFichero;
	
	public AnadirFichero(String origen, String destino, String nombreFichero, String rutaFichero, String usuarioID){
		super(origen, destino,"ANADIR FICHERO");
		this.nombreFichero = nombreFichero;
		this.usuarioID = usuarioID;
		this.rutaFichero = rutaFichero;
	}

	public String getNombreFichero() {
		return this.nombreFichero;
	}

	public String getRutaFichero() {
		return rutaFichero;
	}
}
