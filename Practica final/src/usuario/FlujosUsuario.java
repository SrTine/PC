package usuario;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class FlujosUsuario implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String usuarioID;
	private ObjectOutputStream fOut;
	private ObjectInputStream fIn;
	
	public FlujosUsuario(String id, ObjectOutputStream fOut, ObjectInputStream fIn) {
		super();
		this.usuarioID = id;
		this.fOut = fOut;
		this.fIn = fIn;
	}

	public String getUsuarioID() {
		return usuarioID;
	}

	public ObjectOutputStream getFOut() {
		return fOut;
	}

	public ObjectInputStream getFIn() {
		return fIn;
	}
}
