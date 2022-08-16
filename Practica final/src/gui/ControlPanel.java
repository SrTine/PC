package gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import cliente.Cliente;
import cliente.OSobserver;
import mensajes.AnadirFichero;
import mensajes.ErrorConexion;
import mensajes.EstablecerConexion;
import mensajes.FinalizarConexion;
import mensajes.MostrarListaUsuarios;
import usuario.Usuario;

public class ControlPanel extends JPanel implements OSobserver{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Cliente ctrlCliente;
	private JLabel userID;
	private JButton listaUsuarios;
	private JButton cerrar;
	private JButton anadirFichero;

	public ControlPanel(Cliente ctrlCliente) {
		this.ctrlCliente = ctrlCliente;
		initGUI();
		ctrlCliente.addObserver(this);
	}
	
	class GestorBoton implements ActionListener {
		
		public void actionPerformed(ActionEvent e) {
			JButton botonPulsado=(JButton)e.getSource();
			
			if(botonPulsado == listaUsuarios) {
				ctrlCliente.sendMensaje(new MostrarListaUsuarios(ctrlCliente.getUsuarioID(),ctrlCliente.getClienteIP(),ctrlCliente.getHostIP()));
			}
			else if(botonPulsado == cerrar) {
				int dialog = JOptionPane.YES_NO_OPTION;
				int dialogResult = JOptionPane.showConfirmDialog(getParent(),"¿Seguro que quiere cerrar conexion?","CERRAR CONEXION",dialog);
				if(dialogResult == JOptionPane.YES_OPTION) {
					ctrlCliente.sendMensaje(new FinalizarConexion(ctrlCliente.getClienteIP(), ctrlCliente.getHostIP(), ctrlCliente.getUsuarioID()));
					
					System.exit(0);
				}
			}
			else if(botonPulsado == anadirFichero) {
				String nombreFichero = "";
				String rutaFichero = "";
				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new File("C:\\Users\\pablo\\OneDrive\\Documents\\Wkspace-PC\\Pr5-parte2-Final"));//cambiar si se ejecuta en otro terminal
				int returnVal = chooser.showOpenDialog(getParent());
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					nombreFichero =  chooser.getSelectedFile().getName();
					rutaFichero = chooser.getSelectedFile().getAbsolutePath();
					ctrlCliente.sendMensaje(new AnadirFichero(ctrlCliente.getClienteIP(),ctrlCliente.getHostIP(), nombreFichero, rutaFichero, ctrlCliente.getUsuarioID()));
				}
			}
		}
	}
	
	private void initGUI() {
		JToolBar toolBar = new JToolBar();
		this.userID=new JLabel("Menu:");
		userID.setPreferredSize( new Dimension(50, 10) );
		userID.setMinimumSize( new Dimension(50, 10) );
		
		this.listaUsuarios = new JButton("LISTA USUARIOS");
		this.setToolTipText("Muestra ficheros disponibles y usuarios conectados");
		this.cerrar = new JButton("CERRAR");
		this.anadirFichero = new JButton("ANADIR FICHERO");
		
		toolBar.add(this.userID);
		toolBar.add(this.listaUsuarios);
		toolBar.add(this.cerrar);
		toolBar.add(this.anadirFichero);
	
		GestorBoton gestor = new GestorBoton();
		
		listaUsuarios.addActionListener(gestor);
		cerrar.addActionListener(gestor);
		anadirFichero.addActionListener(gestor);
		
		add(toolBar);
	}

	@Override
	public void onCambiarNombreUsuario(ErrorConexion msg) {
		String nombre = JOptionPane.showInputDialog(this.getParent(),"Este usuario ya esta conectado");
		
		while(nombre.equalsIgnoreCase("")) {
			nombre = JOptionPane.showInputDialog(this.getParent(),"El id no puede ser vacio");
		}
		ctrlCliente.setUsuarioID(nombre);
		ctrlCliente.sendMensaje(new EstablecerConexion(ctrlCliente.getClienteIP(),msg.getOrigen(), nombre));
	}

	@Override
	public void onListaUsuariosRecibida(ArrayList<Usuario> usuarios) {
	}

	@Override
	public void onFicheroDescargado(String nombreFichero, long size) {
		String msg = "Se ha descargado "+ nombreFichero;
		JOptionPane.showMessageDialog(this.getParent(),msg);		
	}

	@Override
	public void onDescargando(String nombreFichero, long kbytes_downloaded) {	
	}

	@Override
	public void onClienteConectado(String nombre) {
		JOptionPane.showMessageDialog(this.getParent(), "Bienvenido " + nombre);
	}

	@Override
	public void onFicheroAnadido() {
		String msg = "Se ha anadido el fichero";
		JOptionPane.showMessageDialog(this.getParent(),msg);
	}
}
