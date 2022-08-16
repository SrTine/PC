package mensajes;

import java.io.Serializable;

//Clase abstracta de la que extienden todos los possibles tipos de mensajes de la aplicacion

public abstract class Mensaje implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String usuarioID;
	protected String origen;
	protected String destino;
	protected String tipo;
	
	public Mensaje(String origen, String destino, String tipo){
		this.origen = origen;
		this.destino = destino;
		this.tipo = tipo;
	}
	
	public String getOrigen() {
		return this.origen;		
	}
	
	public String getDestino() {
		return this.destino;
	}
	
	public String getTipo() {
		return this.tipo;
	}
	
	public String getUsuarioID(){
		return this.usuarioID;
	}
}
