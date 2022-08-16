package cliente;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.net.ServerSocket;
import java.net.Socket;

//CLASE QUE IMPLEMENTA EL HILO ENCARGADO DE EMITIR EL FICHERO EN LA COMUNICACION P2P

public class Emisor extends Thread{
	private Socket socket;
	private String nombreFichero;
	private ServerSocket serverSocket;
	private DataOutputStream dos;
	private FileInputStream fis;
	
	public Emisor(ServerSocket serverSocket,String nombreFichero) {
		this.serverSocket = serverSocket;
		this.nombreFichero = nombreFichero;
	}
	
	public void run() {
		try {			
			this.socket = serverSocket.accept();//ESPERAMOS AL RECEPTOR PARA QUE SE CREE UN CANAL
			
			//CANALES PARA COMUNICARSE CON EL RECEPTOR
			dos = new DataOutputStream(socket.getOutputStream());
	        fis = new FileInputStream(nombreFichero);
			int count;
			
			byte[] bytes = new byte[8 * 1024];
			while((count = fis.read(bytes)) > 0) {//LEEMOS EL CONTENIDO DEL FICHERO POR EL CANAL
				dos.write(bytes, 0, count);//ENVIAMOS EL CONTENIDO DEL FICHERO POR EL CANAL
			}
				
			//CERRAMOS LOS CANALES DE CONEXION
			fis.close();
			dos.close();
			socket.close();
			
		}
		catch (Exception e) {
			System.out.println("Error al crear conexion (Emisor)");
			e.printStackTrace();
			return;
		}
	}
}
