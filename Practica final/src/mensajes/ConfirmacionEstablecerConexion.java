package mensajes;

public class ConfirmacionEstablecerConexion extends Mensaje {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ConfirmacionEstablecerConexion(String origen, String destino){
		super(origen, destino, "CONFIRMACION ESTABLECER CONEXION");
	}
}
