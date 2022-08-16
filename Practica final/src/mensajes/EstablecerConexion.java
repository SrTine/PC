package mensajes;

public class EstablecerConexion extends Mensaje {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EstablecerConexion(String origen, String destino, String usuarioID){
		super(origen, destino, "ESTABLECER CONEXION");
		this.usuarioID = usuarioID;
	}
}
