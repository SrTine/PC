//PABLO MARTINEZ DOMINGO

package launcher;

//Clase que crea e inicializa el cliente y el modo de ejecuccion con los argumentos dados y 

import cliente.Cliente;

public class MainClient {
	
	public static void main(String[] args){
		if(args.length!=4) {
			System.out.println("Argumentos: servidorIP, servidorPuerto, clienteIP, GUI o BATCH");
			System.out.println("Ejemplo: 242.169.0.21 7000 242.168.0.21 GUI");
			return;
		}
		
		String modo = args[3];//modo de ejecuccion
		String hostIP = args[0];//direccion ip servidor
		int puerto = Integer.parseInt(args[1]);//puerto de conexion (>3000 y <64000, para que no este en uso)
		String clienteIP = args[2];//direccion ip cliente
		
		if(modo.equalsIgnoreCase("GUI") || modo.equalsIgnoreCase("BATCH")) {
			Cliente cliente = new Cliente(puerto,clienteIP,hostIP,modo);
			cliente.start();	
			try {
				cliente.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		else {
			System.out.println("Introduzca un modo valido de ejecuccion (GUI o BATCH)");
			return;
		}
	}
}
