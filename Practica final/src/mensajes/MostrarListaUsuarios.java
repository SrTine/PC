package mensajes;

public class MostrarListaUsuarios extends Mensaje {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public MostrarListaUsuarios(String usuarioID, String origen, String destino){
		super(origen, destino, "MOSTRAR LISTA USARIOS");
		this.usuarioID = usuarioID;
	}
}
