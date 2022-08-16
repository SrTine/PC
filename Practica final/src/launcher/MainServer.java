//PABLO MARTINEZ DOMINGO

package launcher;

//Clase de creaccion e inicializacion del servidor con los argumentos dados

import servidor.MonitorTablaFlujos;
import servidor.MonitorTablaInfo;
import servidor.Servidor;

public class MainServer {

	public static void main(String[] args){
	
			if(args.length!=2) {
				System.out.println("Argumentos: sevidorIP servidorPuerto");
				System.out.println("Ejemplo: 242.169.0.21 7000");
				return;
			}
			String servidorIP = args[0];
			int puerto = Integer.parseInt(args[1]);
			MonitorTablaFlujos monitorTF = new MonitorTablaFlujos();
			MonitorTablaInfo monitorTI = new MonitorTablaInfo();

			Servidor server = new Servidor(puerto,servidorIP,monitorTF,monitorTI);
			server.start();
			try {
				server.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	}
}
