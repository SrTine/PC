package servidor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import usuario.Fichero;
import usuario.Usuario;

//CLASE QUE IMPLEMENTA LA TABLA CON LA INFORMACION DE CADA USUARIO CONECTADO

public class MonitorTablaInfo {
	//Tabla que para cada usuario ya registrado lleva una lista de los ficheros que posee
	private Map<String, ArrayList<Fichero>> infoUsuariosDelSistema = new HashMap<>();//mapa de usuarios ya registrados con sus ficheros
	
	private ArrayList<Usuario> tablaInfUsuarios= new ArrayList<Usuario>();;//Contiene id, ip y ficheros de los usuarios conectados
	final Lock lockTablaInfo = new ReentrantLock();//controla el acceso a la tabla informacion de usuarios conectados

	public MonitorTablaInfo() {
	}
	
	//Devuelve true si el usuarioID esta en el servidor
	public boolean userAlreadyExists(String usuarioID) {
		lockTablaInfo.lock();
		
		for(int i=0; i < tablaInfUsuarios.size();i++){
			if(tablaInfUsuarios.get(i).getUsuarioID().equals(usuarioID)) {
				lockTablaInfo.unlock();
				
				return true;
			}
		}
		lockTablaInfo.unlock();
		
		return false;
	}
		
	//Devuelve el id del usuario del fichero en cuestion
	 public String getOwnerFile(String nombreFichero) {
		lockTablaInfo.lock();
		String ownerID = null;
		boolean encontrado = false;
		
		for(int i=0; i < tablaInfUsuarios.size() && !encontrado;i++){
			for(Fichero f:tablaInfUsuarios.get(i).getFicheros()) {
				if(f.getNombreFichero().equals(nombreFichero)) {
					ownerID = tablaInfUsuarios.get(i).getUsuarioID();
					encontrado = true;
					break;					
				}
			}
		}
		lockTablaInfo.unlock();
		
		return ownerID;
	}
	
	 //Añade al usuario a la base de datos y deberia guardar sus ficheros si es un usuario que ya ha sido registrado
	 public void addUser(Usuario u) {
		lockTablaInfo.lock();
		
		if(infoUsuariosDelSistema.containsKey(u.getUsuarioID())) {
			u.setFicheros(infoUsuariosDelSistema.get(u.getUsuarioID()));
		}
		tablaInfUsuarios.add(u);
		
		lockTablaInfo.unlock();
	}
	
	 //Añade el fichero a la lista  del usuario en la base de datos.
	 //Devuelve true si lo consigue y false en otro caso
	 public boolean addFile(String nombreFichero,String rutaFichero,String usuarioID) {
		 lockTablaInfo.lock();
		 for(int i=0; i < tablaInfUsuarios.size();i++){
				if(tablaInfUsuarios.get(i).getUsuarioID().equals(usuarioID)) {
					this.tablaInfUsuarios.get(i).addFichero(new String(nombreFichero), new String(rutaFichero));
					lockTablaInfo.unlock();
					
					return true;
				}
			}
		 lockTablaInfo.unlock();
		 
		 return false;
	 }
	 
	 //Devuelve la tabla de informacion de los usuarios con sus ficheros
	 public ArrayList<Usuario> getUsersInfo(){
		 lockTablaInfo.lock();
		 ArrayList<Usuario> table_info = new ArrayList<Usuario>();
	        for(Usuario user : tablaInfUsuarios) {
	        	table_info.add(new Usuario(user));
	        }
	     lockTablaInfo.unlock();
	     
	     return table_info;
	}
	
	 //Borra la informacion del usuario con usuarioID de la tabla de informacion
	 public void deleteInfoUsuario(String usuarioID) {
		lockTablaInfo.lock();
		
		for(int i=0; i < tablaInfUsuarios.size();i++){
			if(tablaInfUsuarios.get(i).getUsuarioID().equals(usuarioID)) {
				tablaInfUsuarios.remove(i);
				lockTablaInfo.unlock();
				
				return;
			}
		}
		lockTablaInfo.unlock();
	}
	 
	 //inicializa el mapa de usuarioID,listaFicheros a partir del fichero users.txt que contiene los usuarios que ya se han registrado previamente en la aplicacion (con sus ficheros asociados)
	 public void inicializaUsuariosRegistrados () {
			File archivo = new File ("C:\\Users\\pablo\\OneDrive\\Documents\\Wkspace-PC\\Pr5-parte2-Final\\users.txt");//cambiar po direccion donde se encuentra el fichero users cuando se exporte la practica
			try {
				FileReader fr = new FileReader (archivo);
				@SuppressWarnings("resource")
				BufferedReader br = new BufferedReader(fr);
				try {
					String userID = br.readLine();
			
					while(userID != null) {
						String ficheros = br.readLine();
						String[] fich = ficheros.split(" ");
						ArrayList<Fichero> listaFicheros = new ArrayList<Fichero>();
						
						for(String f : fich) {
							String ruta = "C:\\Users\\Tine\\Documents\\Universidad\\Workspace-Eclipse\\Pr5-parte2-extra\\" + f;//se debe cambiar la ruta donde se encuentran los ficheros si se exporta el proyecto
				        	listaFicheros.add(new Fichero(userID,ruta));
				        }
						//infoUsuariosDelSistema.put(userID, listaFicheros);
						
						userID = br.readLine();
					}
					
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
}
