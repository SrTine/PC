package gui;

import java.util.ArrayList;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;
import cliente.Cliente;
import cliente.OSobserver;
import mensajes.ErrorConexion;
import usuario.Usuario;

public class TablaDescargasModel extends AbstractTableModel implements OSobserver {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String[] nombreColumnas = {"FICHERO","KB DESCARGADOS"};
	private ArrayList<Descarga> tablaInfDescargas;
	
	public TablaDescargasModel(Cliente ctrlClient) {
		this.tablaInfDescargas= new ArrayList<Descarga>();
		ctrlClient.addObserver(this);
	}
	
	@Override
	public int getColumnCount() {
		return this.nombreColumnas.length;
	}

	@Override
	public int getRowCount() {
		return this.tablaInfDescargas.size();
	}

	@Override
	public String getColumnName(int column) { 
		String s = new String();
		switch(column) {
			case 0: s = nombreColumnas[0];break;
			case 1: s = nombreColumnas[1];break;
		}
		
		return s;
	} 
	
	@Override
	public Object getValueAt(int row, int col) {
		Object ob = new Object();
		switch (col) {
		case 0: 
			ob = this.tablaInfDescargas.get(row).getNombreFichero();
			
			break;
		case 1:
			ob = this.tablaInfDescargas.get(row).getKbytes();
			
			break;
		}
		
		return ob;
	}
	
	private void update(String nombreFichero,long kbytesDescargados) {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				boolean encontrado = false;
				for(int i =0; i < tablaInfDescargas.size() && encontrado == false;i++) {
					if(tablaInfDescargas.get(i).getNombreFichero().equalsIgnoreCase(nombreFichero)) {
						tablaInfDescargas.get(i).setKbytes(kbytesDescargados);
						encontrado = true;						
					}
				}
				if(!encontrado) {
					tablaInfDescargas.add(new Descarga(nombreFichero,kbytesDescargados));
				}
				
				fireTableStructureChanged();
			}
		});		
		
	}

	@Override
	public void onCambiarNombreUsuario(ErrorConexion msg) {
	}

	@Override
	public void onListaUsuariosRecibida(ArrayList<Usuario> usuarios) {
	}

	@Override
	public void onFicheroDescargado(String nombreFichero, long size) {
	}

	@Override
	public void onDescargando(String nombreFichero,long kbytesDescargados) {
		update(nombreFichero, kbytesDescargados);
	}

	@Override
	public void onClienteConectado(String name) {
	}

	@Override
	public void onFicheroAnadido() {
	}
}
