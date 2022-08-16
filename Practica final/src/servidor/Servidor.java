//PABLO MARTINEZ DOMINGO

package servidor;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.util.ArrayList;
import usuario.FlujosUsuario;
import usuario.Usuario;

public class Servidor extends Thread {
	private int puerto;
	private String servidorIP;
	private MonitorTablaFlujos monitorTF;
	private MonitorTablaInfo monitorTI;
	
	public Servidor(int puerto, String servidorIP, MonitorTablaFlujos monitorTF, MonitorTablaInfo monitorTI) {
		this.puerto = puerto;
		this.monitorTF = monitorTF;
		this.monitorTI = monitorTI;
		this.servidorIP = servidorIP;
		monitorTI.inicializaUsuariosRegistrados();//lee del fichero users.txt
	}
	
	public void run(){
		ServerSocket s;
		try {
			s = new ServerSocket(puerto);
			while(true) {
				System.out.println("Esperando nuevas conexiones...");
				
				new OyenteCliente(s.accept(),this).start();//Detenemos el servidor hasta que llega un cliente
				
				System.out.println("Se ha establecido una nueva conexión con el cliente");
			}		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getIpServer() {
		return this.servidorIP;
	}
	
	public boolean userAlreadyExists(String userID) {
		return monitorTI.userAlreadyExists(userID);
	}
	
	public void añadirUsuario(Usuario u) {
		monitorTI.addUser(u);
	}
	
	public void añadirFlujosUsuario(FlujosUsuario fu) {
		monitorTF.añadirFlujosUsuario(fu);		
	}
	
	public ArrayList<Usuario> getUsersInfo(){
		return monitorTI.getUsersInfo();
	}
	
	public  boolean addFile(String nombreFichero,String rutaFichero,String usuarioID) {
		return monitorTI.addFile(nombreFichero,rutaFichero,usuarioID);
	}
	
	public ArrayList<FlujosUsuario> getFlujosUsuarios(){
		return monitorTF.getFlujosUsuarios();
	}
	
	public void deleteInfoUsuario(String usuarioID) {
		monitorTI.deleteInfoUsuario(usuarioID);
	}
	
	public void deleteFlujosUsuario(String usuarioID) {
		monitorTF.deleteFlujosUsuario(usuarioID);
	}
	
	public String getOwnerFile(String nombreFichero) {
		return monitorTI.getOwnerFile(nombreFichero);
	}
	public ObjectOutputStream getOutputStreamOC(String usuarioID) {
		return monitorTF.getOutputStreamOC(usuarioID);
	}
}
