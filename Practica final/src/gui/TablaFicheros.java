package gui;

public class TablaFicheros {
	private String nombreFichero;
	private String usuarioID;
	
	public TablaFicheros(String nombreFichero, String usuarioID) {
		this.nombreFichero = nombreFichero;
		this.usuarioID = usuarioID;
	}

	public String getNombreFichero() {
		return nombreFichero;
	}

	public String getUsuarioID() {
		return usuarioID;
	}
}
