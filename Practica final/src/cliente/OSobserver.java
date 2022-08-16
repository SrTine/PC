package cliente;

//Interfaz con metodos de los observadores de los clientes para avisar a la GUI

import java.util.ArrayList;
import mensajes.ErrorConexion;
import usuario.Usuario;

public interface OSobserver {
	public void onCambiarNombreUsuario(ErrorConexion mensaje);
	public void onListaUsuariosRecibida(ArrayList<Usuario> usuarios);
	public void onFicheroDescargado(String nombreFichero,long size);
	public void onDescargando(String nombreFichero,long kbytesDescarga);
	public void onClienteConectado(String nombre);
	public void onFicheroAnadido();
}
