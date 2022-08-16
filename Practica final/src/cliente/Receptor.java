package cliente;

import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JOptionPane;

//CLASE QUE IMPLEMENTA EL HILO ENCARGADO DE RECIBIR EL FICHERO EN LA COMUNICACION P2P

public class Receptor extends Thread {
	
	private String modo;
	private String hostIP;
	private int puerto;
	private ArrayList<OSobserver> observers;
	private String extensionFichero;//Extension del archivo(.txt, .mp4 etc)
	private String nombreFichero;//nombre del archivo sin la extension
	
	public Receptor(ArrayList<OSobserver>  observers, String hostIP, int puerto, String modo, String nombreFichero) {
		this.puerto = puerto;
		this.hostIP = hostIP;
		this.observers = observers;
		this.modo = modo;
		String [] partes = new String[2];
		partes = nombreFichero.split("\\.");
		String extension = partes[1];
		this.nombreFichero = partes[0];
		this.extensionFichero = "."+ extension;
	}
	
	public void run() {
		try {
			//Creamos el canal de comunicación con hostIP por el puerto correspondiente
			System.out.println("El emisor esta esperando en el puerto "+ puerto);
			
			//Socket socket = new Socket(hostIP,puerto);//da fallo de asignacionn de ip
			Socket socket = new Socket(InetAddress.getLocalHost(),puerto);
			
			if(modo.equalsIgnoreCase("GUI"))descargaArchivoGUI(socket);
			else descargaArchivoBatch(socket);
			
			socket.close();
		} catch (Exception e) {
			System.out.println("Error al establecer onexion (Receptor)");
			
			e.printStackTrace();
		}
	}
	
	private void descargaArchivoGUI(Socket socket) throws IOException{
		String fichero = JOptionPane.showInputDialog("Introduzca como quiere guardar el fichero (sin la extension del fichero):");
		
		while(fichero.length()==0) {
			fichero = JOptionPane.showInputDialog("El nombre no puede estar vacio");
		}
		this.nombreFichero = fichero;
		
		//CANALES PARA COMUNICARSE CON EL EMISOR
		DataInputStream dis = new DataInputStream(socket.getInputStream());
		FileOutputStream fos = new FileOutputStream(nombreFichero + this.extensionFichero);
		
		int count;
	    byte[] bytes = new byte[8 * 1024];
	    long totalbytes = 0;
	    
	    while((count = dis.read(bytes)) > 0) {
	    	fos.write(bytes, 0, count);
	    	totalbytes += count;
	    	for(int i=0;i < observers.size();i++)observers.get(i).onDescargando(nombreFichero,totalbytes);
		}
	    
	    for(int i=0;i < observers.size();i++)observers.get(i).onDescargando(nombreFichero,totalbytes);
	    for(int i=0;i < observers.size();i++)observers.get(i).onFicheroDescargado(nombreFichero + this.extensionFichero,totalbytes/1000);
	    
		fos.close();
        dis.close();
	}
	
	private void descargaArchivoBatch(Socket socket) throws Exception {
		int complement = new Random().nextInt(1000); // generamos un numero aleatorio para que no sobreescriba al archivo original
		DataInputStream dis = new DataInputStream(socket.getInputStream());
		FileOutputStream fos = new FileOutputStream(nombreFichero + complement + this.extensionFichero);
		
		System.out.println("RECIBIENDO");
		
		int count;
	    byte[] bytes = new byte[8 * 1024];
	    long  totalbytes=0;
	    long temp=0;
	    
	    while((count = dis.read(bytes)) > 0) {
	    	fos.write(bytes, 0, count);
	    	totalbytes+=count;
	    	
	    	if(temp < totalbytes) {
	    		System.out.println("Se han descargado " + totalbytes +" Kbytes");
	    		
	    		temp = totalbytes * 4;
	    	}	
		}
	    
	    System.out.println("Se han descargado " + totalbytes +" KBytes");
		System.out.println("Fichero solicitado se ha descargado");
		
		fos.close();
        dis.close();
	}
}
