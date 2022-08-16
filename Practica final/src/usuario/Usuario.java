//PABLO MARTINEZ DOMINGO

package usuario;

import java.io.Serializable;
import java.util.ArrayList;

public class Usuario implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String usuarioID;
	private String usuarioIP;
	private ArrayList<Fichero> ficheros;
	
	public Usuario(String id, String ip) {
		this.usuarioID = id;
		this.usuarioIP = ip;
		this.ficheros =new ArrayList<Fichero>();
	}
	
	public Usuario(Usuario u) {
		 this.usuarioID = u.usuarioID;
	     this.usuarioIP= u.usuarioIP;
	     this.ficheros = new ArrayList<Fichero>(u.ficheros);
	}

	public String getUsuarioID() {
		return usuarioID;
	}
	
	public String getUsuarioIP() {
		return usuarioIP;
	}
	
	public void setFicheros(ArrayList<Fichero> array) {
		this.ficheros = array;
	}
	
	public ArrayList<Fichero> getFicheros() {
		return this.ficheros;
		
	}

	public void addFichero(String nombreFichero, String rutaFichero) {
		this.ficheros.add(new Fichero(nombreFichero,rutaFichero));
	}
}
