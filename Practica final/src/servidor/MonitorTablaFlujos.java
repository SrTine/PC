package servidor;

import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import usuario.FlujosUsuario;

//CLASE QUE IMPLEMENTA LA TABLA CON LOS FLUJOS DE CADA USUARIO CONECTADO

public class MonitorTablaFlujos {
	private ArrayList<FlujosUsuario> tablaFlujosUsuarios = new ArrayList<FlujosUsuario>();//Contiene id, flujo de entrada y flujo de salida de los usuarios
	final Lock lockTablaFlujos = new ReentrantLock();//controla el acceso a la tablaFlujosUsuarios

	public MonitorTablaFlujos() {
	}
	
	//añade los canales del usuario a la tabla de flujos
	public void añadirFlujosUsuario(FlujosUsuario fU) {
		lockTablaFlujos.lock();
		tablaFlujosUsuarios.add(fU);
		lockTablaFlujos.unlock();
	}
	 
	//Devuelve la referencia al canal de salida del usuario con usuarioID
	public ObjectOutputStream getOutputStreamOC(String usuarioID) {
		lockTablaFlujos.lock();
		ObjectOutputStream fOut = null;
		boolean encontrado = false;
		
		for(int i=0; i < tablaFlujosUsuarios.size() && !encontrado;i++){
			if(usuarioID.equals(tablaFlujosUsuarios.get(i).getUsuarioID())) {
				encontrado = true;
				fOut = tablaFlujosUsuarios.get(i).getFOut();
			}
		}
		lockTablaFlujos.unlock();
		
		return fOut;
	}
	
	//Devuelve la tabla de flujos de los usuarios
	public ArrayList<FlujosUsuario> getFlujosUsuarios(){
		lockTablaFlujos.lock();
		ArrayList<FlujosUsuario> flujos = new ArrayList<FlujosUsuario>();
		
		for(int i=0; i < this.tablaFlujosUsuarios.size();i++) {
			FlujosUsuario flujosAux = new FlujosUsuario(tablaFlujosUsuarios.get(i).getUsuarioID(),
			tablaFlujosUsuarios.get(i).getFOut(), tablaFlujosUsuarios.get(i).getFIn());
			flujos.add(flujosAux);
		}
		lockTablaFlujos.unlock();
		
		return flujos;
	}
	
	//Borra la informacion del usuario con usuarioID de la tabla de flujos
	 public void deleteFlujosUsuario(String usuarioID) {
		lockTablaFlujos.lock();
		
		for(int i=0; i < tablaFlujosUsuarios.size();i++){
			if(tablaFlujosUsuarios.get(i).getUsuarioID().equals(usuarioID)) {
				tablaFlujosUsuarios.remove(i);
				lockTablaFlujos.unlock();
				
				return;
			}
		}
		lockTablaFlujos.unlock();
	}
}
