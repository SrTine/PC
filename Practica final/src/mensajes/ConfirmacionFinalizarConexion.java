package mensajes;

public class ConfirmacionFinalizarConexion extends Mensaje{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ConfirmacionFinalizarConexion(String origen, String destino, String usuarioID){
		super(origen, destino,"CONFIRMACION FINALIZAR CONEXION");
		this.usuarioID = usuarioID;
	}
}
