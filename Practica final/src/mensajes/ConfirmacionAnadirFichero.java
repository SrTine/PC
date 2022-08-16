package mensajes;

public class ConfirmacionAnadirFichero extends Mensaje {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ConfirmacionAnadirFichero(String origen, String destino){
		super(origen, destino, "CONFIRMACION ANADIR FICHERO");
	}
}
