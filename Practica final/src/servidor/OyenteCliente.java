package servidor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import mensajes.AnadirFichero;
import mensajes.ConfirmacionAnadirFichero;
import mensajes.ConfirmacionConsultarUsuarios;
import mensajes.ConfirmacionEstablecerConexion;
import mensajes.ConfirmacionFinalizarConexion;
import mensajes.EnviarFichero;
import mensajes.ErrorConexion;
import mensajes.EstablecerConexion;
import mensajes.FinalizarConexion;
import mensajes.IntercambioCS;
import mensajes.IntercambioSC;
import mensajes.Mensaje;
import mensajes.MostrarListaUsuarios;
import mensajes.SolicitarFichero;
import usuario.Fichero;
import usuario.FlujosUsuario;
import usuario.Usuario;

public class OyenteCliente extends Thread {
	
	private Socket socket;
	private ObjectOutputStream fOut;//flujo de salida hacia cliente
	private ObjectInputStream fIn; //flujo de entrada a servidor
	private Servidor servidor;
	
	public OyenteCliente(Socket s,Servidor servidor) {
		try {
			this.socket = s;
			this.fOut = new ObjectOutputStream(socket.getOutputStream());
			this.fIn = new  ObjectInputStream(socket.getInputStream());
			this.servidor = servidor;
		} catch (IOException e) {
			System.out.println("Error en la contructora de OyenteCliente");
			
			e.printStackTrace();
		}
	}
	
	public void run() {
		Mensaje m = null;
		
		while(true) {
			try {
				m = (Mensaje) fIn.readObject();//parseo del objeto recibido
				
				switch(m.getTipo()) {
					case "ESTABLECER CONEXION":{
						realizarConexion( (EstablecerConexion) m);
						
						break;
					}
					case "MOSTRAR LISTA USARIOS":{
						System.out.println("Cliente "+ ((MostrarListaUsuarios) m).getUsuarioID()+" ha solicitado info usuarios");
						
						fOut.writeObject(new ConfirmacionConsultarUsuarios(m.getDestino(), m.getOrigen(),servidor.getUsersInfo()));
						
						break;
					}
					case "ANADIR FICHERO":{
						añadirArchivo((AnadirFichero)m);
						
						break;
					}
					case "FINALIZAR CONEXION":{
						cerrarConexion((FinalizarConexion) m);
						
						return;
					}
					case "SOLICITAR FICHERO":{
						avisarPeerEmisor((SolicitarFichero) m);
						break;
					}
					case "INTERCAMBIO CLIENTE-SERVIDOR":{
						avisarPeerReceptor((IntercambioCS) m);
						
						break;
					}
				}
								
			} catch (Exception e) {
				System.out.println("Conexion con el cliente interrumpida (oyenteCliente)");
				
				servidor.deleteInfoUsuario(m.getUsuarioID());
				servidor.deleteFlujosUsuario(m.getUsuarioID());
				e.printStackTrace();
				
				return;
			}
		}
	}
	
	private void añadirArchivo(AnadirFichero msg) throws IOException {
		if(servidor.addFile(msg.getNombreFichero(),msg.getRutaFichero(), msg.getUsuarioID()))
			fOut.writeObject(new ConfirmacionAnadirFichero(servidor.getIpServer(),msg.getOrigen()));
	}
	
	private void realizarConexion(EstablecerConexion msg) throws IOException {
		if(servidor.userAlreadyExists(msg.getUsuarioID())) {
			fOut.writeObject(new ErrorConexion(servidor.getIpServer(),msg.getOrigen()));
		}
		else {
			Usuario u = new Usuario(msg.getUsuarioID(),msg.getOrigen());
			//id y canales usuario
			FlujosUsuario fu = new FlujosUsuario(msg.getUsuarioID(),fOut,fIn);
			
			servidor.añadirUsuario(u);
			servidor.añadirFlujosUsuario(fu);
			
			//enviamos mensaje confirmacion
			fOut.writeObject(new ConfirmacionEstablecerConexion(msg.getDestino(),msg.getOrigen()));//del servidor al cliente
		}
	}
	
	private void cerrarConexion(FinalizarConexion msg) throws IOException {
		System.out.println("Servidor cerrando conexion con " + msg.getUsuarioID());
		
		fOut.writeObject(new ConfirmacionFinalizarConexion(msg.getDestino(),msg.getOrigen(),msg.getUsuarioID()));
		servidor.deleteInfoUsuario(msg.getUsuarioID());
		servidor.deleteFlujosUsuario(msg.getUsuarioID());
		fOut.close();
	}
	
	private void avisarPeerEmisor(SolicitarFichero msg) throws IOException {
		String rutaFichero = new String();
		String userID = servidor.getOwnerFile(msg.getNombreFichero());
		if(userID == null) {
			
			System.out.println(msg.getNombreFichero());
			System.out.println("Problema en OyenteCliente,fichero no encontrado");
			
			return;
		}
		
		//seleccionames la ruta correspondiente al archivo que tiene que enviar
		for(Usuario u: servidor.getUsersInfo()) {
			if(u.getUsuarioID().equalsIgnoreCase(userID)) {
				for(Fichero f : u.getFicheros()) {
					if(f.getNombreFichero().equalsIgnoreCase(msg.getNombreFichero())) {
						rutaFichero = f.getRutaFichero();
						
						break;
					}
				}
				break;
			}
		}
		//enviamos el mensaje de emitir fichero al emisor pasandole la ruta y el nombre de archivo
		ObjectOutputStream fOut2 = servidor.getOutputStreamOC(userID);
		fOut2.writeObject(new EnviarFichero(msg.getDestino(),msg.getOrigen(),rutaFichero,msg.getNombreFichero(),msg.getUsuarioID()));
	}
	
	private void avisarPeerReceptor(IntercambioCS msg) throws IOException {
		ObjectOutputStream fOut1 = servidor.getOutputStreamOC(msg.getUsuarioID());
		fOut1.writeObject(new IntercambioSC(msg.getDireccionIP(),msg.getPuerto(),msg.getNombreFichero()));
	}
}
