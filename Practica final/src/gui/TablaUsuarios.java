package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import cliente.Cliente;
import mensajes.SolicitarFichero;

public class TablaUsuarios extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JTable tabla;
	
	public TablaUsuarios(Cliente ctrlCliente) {
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 2), "INFO", TitledBorder.LEFT, TitledBorder.TOP)); 
		
		TablaUsuariosModel tablaUsuarios = new TablaUsuariosModel(ctrlCliente);
		this.tabla = new JTable(tablaUsuarios);
		add(new JScrollPane(tabla));
		this.setMaximumSize(new Dimension(800,500));
		this.setPreferredSize(new Dimension(800,500));
		
		tabla.addMouseListener(new MouseAdapter() {
	        public void mousePressed(MouseEvent eventMouse) {
	        	
	            JTable tabla2 = (JTable) eventMouse.getSource();
	            Point p = eventMouse.getPoint();
	            int row = tabla2.rowAtPoint(p);
	            String nombreFichero = (String) tabla2.getValueAt(row, 0);
	            
	            if(nombreFichero !=null) {
	            	int n = JOptionPane.showConfirmDialog(null, "¿DESCARGAR FICHERO "+ nombreFichero + "?", "DESCARGA", JOptionPane.YES_NO_OPTION);
		            if (n == JOptionPane.YES_OPTION) {
		            	ctrlCliente.sendMensaje(new SolicitarFichero(ctrlCliente.getUsuarioID(),ctrlCliente.getClienteIP(),ctrlCliente.getHostIP(), nombreFichero));
		            }
	            }       
	        }
	    });  
	}
}

