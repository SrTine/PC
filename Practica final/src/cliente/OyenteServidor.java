package cliente;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import mensajes.ConfirmacionConsultarUsuarios;
import mensajes.ConfirmacionFinalizarConexion;
import mensajes.EnviarFichero;
import mensajes.ErrorConexion;
import mensajes.EstablecerConexion;
import mensajes.IntercambioCS;
import mensajes.IntercambioSC;
import mensajes.Mensaje;
import usuario.Fichero;
import usuario.Usuario;

public class OyenteServidor extends Thread{
	
	private Socket socket;
	private ObjectInputStream fIn;//Flujo entrada a cliente
	private Cliente cliente;//referencia al cliente
	private ArrayList<OSobserver> observers;//lista de los observadores de los clientes que se conectan
	
	
	public OyenteServidor(Socket s,Cliente cliente) {
		try {
			this.cliente = cliente;
			this.socket = s;
			this.fIn = new ObjectInputStream(socket.getInputStream());//flujo de entrada
			this.observers= new ArrayList<OSobserver>();
		} catch (IOException e) {
			System.out.println("Problema en la construccion de un objeto OyenteServer");
			e.printStackTrace();
		}
		
	}
	
	public void run() {
		if(cliente.getModo().equalsIgnoreCase("GUI")) {
			runGUI();
		}
		else runBatch();
	}
	
	private void runBatch() {
		while(true) {
			Mensaje m;//mensaje recogido por el flujo de entrada
				try {
					 m = (Mensaje)fIn.readObject();//parseo del objeto recibido
					switch(m.getTipo()) {
						case "CONFIRMACION ESTABLECER CONEXION":{
							System.out.println("Conexion establecida con servidor");
							cliente.getSemaphore().release();
							
							break;
						}
						case "ERROR CONEXION":{
							reintentarConexion((ErrorConexion) m);
							
							break;
						}
						case "CONFIRMACION CONSULTAR USUARIOS":{
							System.out.println("Informacion de usuarios conectados recibida");
							ArrayList<Usuario> tabla = ((ConfirmacionConsultarUsuarios) m).getUsuariosInfo();
							printInfoUsuarios(tabla);
							
							break;
						}
						case "CONFIRMACION ANADIR FICHERO":{
							System.out.println("Fichero añadido correctamente");
							
							break;
						}
						case "ENVIAR FICHERO":{
							enviarArchivo((EnviarFichero) m);
							
							break;
						}
						case "INTERCAMBIO SERVIDOR-CLIENTE":{
							
							recibirArchivo((IntercambioSC) m);
							
							break;
						}
						case "CONFIRMACION FINALIZAR CONEXION":{
							System.out.println("Conexion finalizada con "+ ((ConfirmacionFinalizarConexion) m).getUsuarioID());
							socket.close();
							fIn.close();
							
							return;
						}
						default:
	                        System.out.println("No se reconoce el mensaje (oyenteServidor)");break;
					}
				}
				catch (Exception e) {
					System.out.println("Conexion con el servidor interrumpida (oyenteServidor)");
					
					try {
						socket.close();
						fIn.close();//cerramos el flujo en caso de error
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					e.printStackTrace();
					
					return;
				}
		}
	}
	
	private void runGUI() {
		while(true) {
			Mensaje m;		
				try {
					 m = (Mensaje)fIn.readObject();
					switch(m.getTipo()) {
						case "CONFIRMACION ESTABLECER CONEXION":{
							System.out.println("Conexion establecida con el servidor");
							
							for(int i=0; i <observers.size();i++)observers.get(i).onClienteConectado(this.cliente.getUsuarioID());
							
							break;
						}
						case "ERROR CONEXION":{
							for(int i=0; i <observers.size();i++)observers.get(i).onCambiarNombreUsuario((ErrorConexion) m);
							
							break;
						}
						case "CONFIRMACION CONSULTAR USUARIOS":{
							for(int i=0; i <observers.size();i++)observers.get(i).onListaUsuariosRecibida(((ConfirmacionConsultarUsuarios) m).getUsuariosInfo());
							
							break;
						}
						case "CONFIRMACION ANADIR FICHERO":{
							for(int i=0; i <observers.size();i++)observers.get(i).onFicheroAnadido();
							
							break;
						}
						case "ENVIAR FICHERO":{
							enviarArchivo((EnviarFichero) m);
							
							break;
						}
						case "INTERCAMBIO SERVIDOR-CLIENTE":{
							recibirArchivo((IntercambioSC) m);
							
							break;
						}
						case "CONFIRMACION FINALIZAR CONEXION":{
							System.out.println("Conexion finalizada con "+ ((ConfirmacionFinalizarConexion) m).getUsuarioID());
							
							socket.close();
							fIn.close();
							
							return;
						}
						default:
	                        System.out.println("No se reconoce el mensaje (oyenteServidor)");break;
					}
				}
				catch (Exception e) {
					System.out.println("Conexion con el servidor interrumpida (oyenteServidor)");
					
					try {
						socket.close();
						fIn.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					e.printStackTrace();
					
					return;
				}
		}
	}
	
	private void reintentarConexion(ErrorConexion msg) throws IOException {
		cliente.cambiarNombreCliente();
		cliente.sendMensaje(new EstablecerConexion(cliente.getClienteIP(),msg.getOrigen(), cliente.getUsuarioID()));
	}
	
	private void enviarArchivo(EnviarFichero msg) throws IOException {
		ServerSocket serverSocket = new ServerSocket(0); // Si metemos 0 como puerto, el socket se encarga de buscar un puerto abierto en el que establece su escucha
		serverSocket.setReuseAddress(true);
		Mensaje m = new IntercambioCS(msg.getUsuarioID(),msg.getOrigen(),cliente.getClienteIP(),serverSocket.getLocalPort(),msg.getNombreFichero());
		
		cliente.sendMensaje(m);
		
		new Emisor(serverSocket,msg.getRutaFichero()).start();//peerEmisor
	}
	
	private void recibirArchivo(IntercambioSC msg) {
		String destinoIP = msg.getDireccionIP();
		int  destinoPuerto = msg.getPuerto();
		
		new Receptor(this.observers,destinoIP,destinoPuerto,this.cliente.getModo(),msg.getNombreFichero()).start();//peerReceptor
	}
	
	private void printInfoUsuarios(ArrayList<Usuario> usuarios) {
		System.out.println("TABLA INFORMACION USUARIOS");
		for(Usuario u: usuarios) {
			
			System.out.println("ID: " + u.getUsuarioID());
			System.out.println("Ficheros:");
			
			for(Fichero fichero: u.getFicheros()) {
				System.out.println(fichero.getNombreFichero());
			}
		}
	}
	
	public void addObserver(OSobserver o) {
		this.observers.add(o);
	}
}
