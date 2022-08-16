package mensajes;

import java.util.ArrayList;

import usuario.Usuario;

public class ConfirmacionConsultarUsuarios extends Mensaje {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<Usuario> usuariosInfo;
	
	public ConfirmacionConsultarUsuarios(String origen, String destino, ArrayList<Usuario> usuariosInfo){
		super(origen, destino, "CONFIRMACION CONSULTAR USUARIOS");
		this.usuariosInfo = usuariosInfo;
	}
	public ArrayList<Usuario> getUsuariosInfo() {
		return this.usuariosInfo;
	}
}
