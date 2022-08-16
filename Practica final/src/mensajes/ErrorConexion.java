package mensajes;

public class ErrorConexion extends Mensaje {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ErrorConexion(String origen, String destino) {
		super(origen, destino, "ERROR CONEXION");		
	}
}
