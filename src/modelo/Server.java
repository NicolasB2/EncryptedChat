package modelo;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

	private ArrayList<Socket> sockets; //Arreglo de los socket cliente
	private static ServerSocket serverSocket; //Socket del servidor
	
	/**
	 * Constructor del servidor
	 * @param port
	 * @param number_clients
	 */
	public Server(int port, int number_clients) {

		try {

			System.out.println("Server on line");
			serverSocket = new ServerSocket(port);

			sockets = new ArrayList<>();
					
			for (int i = 0; i < number_clients; i++) { // se conectan los clientes (2)
				Socket s = serverSocket.accept();
				sockets.add(s);
				System.out.println("Client was connected");	
			}

			System.out.println("start handShake"); // inicio del handshake
			
			// inicio del handshake
			
			ServerThread st1 = new ServerThread(sockets.get(0), sockets.get(1),true); //hilo del primer usuario
			st1.start();
			
			
			ServerThread st2 = new ServerThread(sockets.get(1), sockets.get(0),false); //hilo del segundo usuario
			st2.start();
			

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	//metodo para enviar objetos por medio del socket
	public void sendObject(Object messageObj, ObjectOutputStream outputStream) throws IOException {
		outputStream.writeObject(messageObj);
		outputStream.flush();
	}

	public ArrayList<Socket> getSockets() {
		return sockets;
	}

	public static ServerSocket getServerSocket() {
		return serverSocket;
	}

	public static void main(String[] args) {

		//BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int port = 8080;
		
		try {
			//System.out.println("please enter server port");
			//port = Integer.parseInt(br.readLine());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		new Server(port, 2);

	}
}
