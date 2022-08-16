package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.WindowConstants;

import cliente.Cliente;


public class VentanaPrincipalCliente extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Cliente ctrlClient;
	ControlPanel controlPanel;
	TablaUsuarios tablaUsuarios;
	TablaDescargas tablaDescargas;
		
	public VentanaPrincipalCliente(Cliente ctrlClient) {
		super("CONEXION " + ctrlClient.getUsuarioID());
		this.ctrlClient = ctrlClient;
		initGUI();
	}
	
	private void initGUI(){
		JPanel panelPrincipal = this.creaPanelPrincipal(); 
		this.addControlPanel(panelPrincipal);
		
		JPanel panelTablaUsuarios = new JPanel();
		JPanel panelTablaDownloads = new JPanel();
		this.createTable(panelTablaUsuarios);
		this.createTableDownloads(panelTablaDownloads);
		
		JSplitPane splitPaneV  = new JSplitPane( JSplitPane.VERTICAL_SPLIT );
		panelPrincipal.add(splitPaneV, BorderLayout.CENTER);
		splitPaneV.setLeftComponent(panelTablaUsuarios);
		splitPaneV.setRightComponent(panelTablaDownloads);
		
		this.setContentPane(panelPrincipal);
		this.pack();
		this.setVisible(true);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}
	
	private JPanel creaPanelPrincipal() {
		JPanel panelPrincipal = new JPanel();
		panelPrincipal.setLayout(new BorderLayout());
		panelPrincipal.setBackground(Color.gray);
		
		return panelPrincipal;
	}
	

	private void addControlPanel(JPanel panelPrincipal) {
		this.controlPanel= new ControlPanel(this.ctrlClient);
		panelPrincipal.add(this.controlPanel,BorderLayout.PAGE_START);
	}

	

	private void createTable(JPanel panelTabla) {
		//panelTabla.setLayout(new BoxLayout (panelTabla,BoxLayout.Y_AXIS));
		this.tablaUsuarios = new TablaUsuarios(ctrlClient);
		panelTabla.add(this.tablaUsuarios);
	}
	
	private void createTableDownloads(JPanel panelTablaDownloads) {
		//panelTablaDownloads.setLayout(new BoxLayout (panelTablaDownloads,BoxLayout.Y_AXIS));
		this.tablaDescargas=new TablaDescargas(ctrlClient);
		panelTablaDownloads.add(this.tablaDescargas);	
	}
}
