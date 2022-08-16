package gui;

public class Descarga {
	private String nombreFichero;
	private long kbytes;
	
	public Descarga(String nombreFichero, long kbytes) {
		this.nombreFichero = nombreFichero;
		this.kbytes = kbytes;
	}
	
	public String getNombreFichero() {
		return nombreFichero;
	}
	
	public long getKbytes() {
		return kbytes;
	}
	
	public void setKbytes(long kbytes) {
		this.kbytes = kbytes;
	}
}
