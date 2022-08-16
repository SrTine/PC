package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import cliente.Cliente;

public class TablaDescargas extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JTable tabla;
	
	public TablaDescargas(Cliente ctrlCliente) {
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 2), "DESCARGAS", TitledBorder.LEFT,TitledBorder.TOP)); 
		
		TablaDescargasModel tablaDescargas = new TablaDescargasModel(ctrlCliente);
		this.tabla = new JTable(tablaDescargas);
		add(new JScrollPane(tabla));
		this.setMaximumSize(new Dimension(800,200));
		this.setPreferredSize(new Dimension(800,200));
	}
}
