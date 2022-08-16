package gui;

import java.util.ArrayList;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;
import cliente.Cliente;
import cliente.OSobserver;
import mensajes.ErrorConexion;
import usuario.Fichero;
import usuario.Usuario;

public class TablaUsuariosModel extends AbstractTableModel implements OSobserver{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String[] nombreColumnas = {"FICHERO","USUARIO"};
	private ArrayList<TablaFicheros> tablaInfUsuarios;
	private Cliente ctrlCliente;
	
	public  TablaUsuariosModel(Cliente ctrlCliente) {
		this.tablaInfUsuarios = new ArrayList<TablaFicheros>();
		this.ctrlCliente = ctrlCliente;
		ctrlCliente.addObserver(this);
	}
	
	@Override
	public int getColumnCount() {
		return this.nombreColumnas.length;
	}

	@Override
	public int getRowCount() {
		return this.tablaInfUsuarios.size();
	}
	
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
			ob = this.tablaInfUsuarios.get(row).getNombreFichero();
			
			break;
		case 1:
			ob = this.tablaInfUsuarios.get(row).getUsuarioID();
			
			break;
		}
		return ob;
	}
	
	private void update(ArrayList<Usuario> usuarios) {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				tablaInfUsuarios = transformTable(usuarios);
				fireTableStructureChanged();
			}
		});		
		
	}
	
	private ArrayList<TablaFicheros> transformTable(ArrayList<Usuario> usuarios) {
		ArrayList<TablaFicheros> tabla = new ArrayList<TablaFicheros>();
		
		for(Usuario u: usuarios) {
			if(!u.getUsuarioID().equals(ctrlCliente.getUsuarioID())) {
				if(u.getFicheros().size()==0)
					tabla.add(new TablaFicheros(null,u.getUsuarioID()));
				for(Fichero file: u.getFicheros()) {
					tabla.add(new TablaFicheros(file.getNombreFichero(),u.getUsuarioID()));
				}
			}
			
		}
		return tabla;
	}

	@Override
	public void onCambiarNombreUsuario(ErrorConexion msg) {
	}

	@Override
	public void onListaUsuariosRecibida(ArrayList<Usuario> usuarios) {
		update(usuarios);
	}
	
	@Override
	public void onFicheroDescargado(String filename, long size) {
	}
	
	@Override
	public void onDescargando(String filename,long kbytes_downloaded) {
	}
	
	@Override
	public void onClienteConectado(String name) {
		//update(usuarios);
		fireTableStructureChanged();
	}
	
	@Override
	public void onFicheroAnadido() {
	}
}
