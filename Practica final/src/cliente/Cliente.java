package cliente;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.Semaphore;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import gui.VentanaPrincipalCliente;
import mensajes.AnadirFichero;
import mensajes.EstablecerConexion;
import mensajes.FinalizarConexion;
import mensajes.Mensaje;
import mensajes.MostrarListaUsuarios;
import mensajes.SolicitarFichero;

public class Cliente extends Thread{
		
	private int puerto;//puerto de conexion al server
	private String clienteIP;//direccion ip del cliente
	private String modo;//modo de ejecuccion (gui o batch)
	private OyenteServidor OS;//hilo de escucha
	private ObjectOutputStream fOut;//flujo de salida del cliente
	private String hostIP;//direccion ip servidor
	private String usuarioID;//ID del cliente
	private Semaphore semaforo = new Semaphore(0);//semaforo utilizado para la espera del cliente hasta recibir confirmacion

	public Cliente(int puerto, String clienteIP, String hostIP,String modo) {
		this.puerto = puerto;
		this.clienteIP = clienteIP;
		this.hostIP = hostIP;
		this.modo = modo;
	}
		
	public void run() {
		if(modo.equalsIgnoreCase("GUI")) {
			runGUI();	
		}
		else if(modo.equalsIgnoreCase("BATCH")) 
			runBatch();	
	}
	
	private void runGUI() {
		try {			
			String nombreCliente = JOptionPane.showInputDialog("Introduzca su nombre porfavor");
			while(nombreCliente.length()==0) {
				nombreCliente = JOptionPane.showInputDialog("El nombre no puede estar vacio");
			}
			this.usuarioID = nombreCliente;
			
			//ESTABLECEMOS EL CANAL Y LOS FLUJOS PARA COMUNICARNOS
			
			//Socket socket = new Socket(clienteIP,puerto);
			Socket socket = new Socket(InetAddress.getLocalHost(),puerto);//creamos la conexion al puerto
			this.fOut= new ObjectOutputStream(socket.getOutputStream());
			this.OS = new OyenteServidor(socket,this);//proceso que se encarga de escuchar al oyente cliente
			Cliente cliente = this;
			
			SwingUtilities.invokeAndWait(new Runnable() {
				
				@Override
				public void run() {
					new VentanaPrincipalCliente(cliente);//ejecutamos la interfaz en segundo plano				
				}	
				
			});
		
			OS.start();
			Mensaje m = new EstablecerConexion(clienteIP,hostIP,usuarioID);//MENSAJE DE CONEXION
			fOut.writeObject(m);//ESCRIBIMOS EL MENSAJE POR EL FLUJO
		}catch(Exception e) {
			System.out.println("Error al ejecutar conexion del cliente " + usuarioID + " (cliente-runGUI)");
			e.printStackTrace();
			return;
		}	
	}

	
	private void runBatch() {
		try {
			//Socket socket = new Socket(clienteIP,puerto);
			Socket socket = new Socket(InetAddress.getLocalHost(),puerto);
			this.fOut= new ObjectOutputStream(socket.getOutputStream());
				
			Scanner sc = new Scanner(System.in);
			System.out.println("Introduzca su nombre porfavor ");
			String nombreCliente = sc.nextLine();//leemos el nombre del ususario
			
			while(nombreCliente.length() == 0) {
				System.out.println("Debe introducir su nombre");
				System.out.println("Introduzca su nombre porfavor");
				
				nombreCliente = sc.nextLine();
			}
			this.usuarioID = nombreCliente;
		
			new OyenteServidor(socket,this).start();
			Mensaje m = new EstablecerConexion(clienteIP,hostIP,usuarioID);
			fOut.writeObject(m);	
		
			semaforo.acquire();//Detenemos el proceso cliente hasta recibir confirmacion de conexion
			
			System.out.println("Bienvenido, " + this.usuarioID);
			
			int option;
			do{
				displayMenu();
				option = sc.nextInt();
				//Enviamos mensaje al servidor
				Mensaje msg = procesaOpcion(option,usuarioID,clienteIP,hostIP);
				if(msg!=null)
					fOut.writeObject(msg);
			}while(option!=0);
			sc.close();
		
		}catch(Exception e) {
			System.out.println("Error al ejecutar conexion del cliente " + usuarioID + " (cliente-runBATCH)");
			e.printStackTrace();
			return;
		
		}	
	}
	
	private Mensaje procesaOpcion(int opcion, String nombre, String clienteIP, String hostIP) {
		Mensaje m = null;
		try {
			switch(opcion) {			
				case 1://Enviar MENSAJE MOSTRAR LISTA USUARIOS
					m = new MostrarListaUsuarios(nombre,clienteIP,hostIP);
					
					break;
				case 2://Enviar MENSAJE SOLICITAR FICHERO
					System.out.println("Introduce el nombre fichero solicitado:");
					
					BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
					String nombreFichero = br.readLine();
					m = new SolicitarFichero(nombre,clienteIP, hostIP, nombreFichero);	
					
					break;
				case 3:
					System.out.println("Introduce nombre fichero que se desea anadir:");
					
					BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
					String nombreFichero2,rutaFichero;
					nombreFichero2 = br1.readLine();
					
					System.out.println("Introduce la ruta del fichero:");
					
					rutaFichero = br1.readLine();
					File fichero= new File(rutaFichero);
					if(fichero.exists())
						m = new AnadirFichero(clienteIP, hostIP, nombreFichero2,rutaFichero,usuarioID);
					else System.out.println("El fichero no existe");
					
					break;
				case 0://Enviar MENSAJE FINALIZAR CONEXION
					m = new FinalizarConexion(clienteIP,hostIP,nombre);
					
					break;
				default: System.out.println("Opcion inexistente");
			}
		}catch(Exception e) {
			e.printStackTrace();			
		}
		
		return m;	
	}
	
	private void displayMenu() {
		System.out.println("Menu:");
		System.out.println("==========");
		System.out.println("1-Consultar lista usuarios");
		System.out.println("2-Solicitar fichero");
		System.out.println("3-Anadir fichero");
		System.out.println("0-Salir");
		System.out.println("==========");
		System.out.println("Introduzca la opcion: ");		
	}
	
	public void cambiarNombreCliente() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		System.out.println("Introduzca otro nombre, este cliente ya esta conectado: ");
		
		String nombreUsuario = br.readLine();
		this.usuarioID = nombreUsuario;
	}
	
	public Semaphore getSemaphore() {
		return this.semaforo;
	}
	
	public String getModo() {
		return this.modo;
	}
	
	public String getHostIP() {
		return this.hostIP;
	}
	public String getClienteIP() {
		return clienteIP;
	}
	
	public String getUsuarioID() {
		return this.usuarioID;
	}
	
	public void setUsuarioID(String usuarioID) {
		this.usuarioID = usuarioID;
	}
	
	public void sendMensaje(Mensaje m) {
		try {
			fOut.writeObject(m);
		} catch (IOException e) {
			System.out.println("Error al enviar el mensaje (cliente)");
			e.printStackTrace();
		}
	}
	 
	public void addObserver(OSobserver o) {
		this.OS.addObserver(o);
	}
}
