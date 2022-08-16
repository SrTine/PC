package mensajes;

public class FinalizarConexion extends Mensaje {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		
	public FinalizarConexion(String origen, String destino, String usuarioID){
		super(origen, destino, "FINALIZAR CONEXION");
		this.usuarioID = usuarioID;
	}
}
