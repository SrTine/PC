package usuario;

import java.io.Serializable;

public class Fichero implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String nombreFichero;
	private String rutaFichero;
	
	public Fichero(String nombreFichero,String rutaFichero) {
		this.nombreFichero = nombreFichero;
		this.rutaFichero = rutaFichero;
	}
	
	public String getNombreFichero() {
		return nombreFichero;
	}
	
	public String getRutaFichero() {
		return rutaFichero;
	}
}
