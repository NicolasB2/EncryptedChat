package modelo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.KeyAgreement;
import javax.crypto.SecretKey;

public class Client {

	private String serverIp;// Server's IP address
	private int port;// Free port to establish connection
	private String nickname; //user's nickname
	
	private static Socket socket; // Socket which allowing connection
	private boolean isClientConected; //control boolean
	
	private ClientReceiveThread receive;//thread for receive Strings 
	private ClientSendThread send;//thread which for Strings 
	
	private KeyPair key;
	private PrivateKey privateKey;
	private PublicKey publicKey;
	private KeyAgreement keyAgreement;
	private SecretKey secretKey;
	
	/**
	 * constructor del cliente
	 * @param serverIp
	 * @param port
	 * @param nickname
	 */
	public Client(String serverIp, int port, String nickname) {
		this.port = port;
		this.serverIp = serverIp;
		this.nickname = nickname;
		try {

			socket = new Socket(serverIp, port);//se crea el socket a partir de los parametros
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream()); // stream de salida
			ObjectInputStream in = new ObjectInputStream(socket.getInputStream()); // stream de entrada
			System.out.println("_________________________________");
			isClientConected = true;  // cambio de estado a conectado
			
			receive = new ClientReceiveThread(this,in,out); // hilo de lectura de mensajes
			receive.start(); 
			send = new ClientSendThread(this,out); // hilo de escritura de mensajes 
  		    send.start();
//			handshake(socket);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public String getServerIp() {
		return serverIp;
	}

	public int getPort() {
		return port;
	}

	public String getNickname() {
		return nickname;
	}

	public boolean isClientConected() {
		return isClientConected;
	}

	public Socket getSocket() {
		return socket;
	}

	public KeyPair getKey() {
		return key;
	}

	public void setKey(KeyPair key) {
		this.key = key;
	}

	public PrivateKey getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(PrivateKey privateKey) {
		this.privateKey = privateKey;
	}

	public PublicKey getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(PublicKey publicKey) {
		this.publicKey = publicKey;
	}

	public KeyAgreement getKeyAgreement() {
		return keyAgreement;
	}

	public void setKeyAgreement(KeyAgreement keyAgreement) {
		this.keyAgreement = keyAgreement;
	}

	public SecretKey getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(SecretKey secretKey) {
		this.secretKey = secretKey;
	}

	public static void main(String[] args) {

		//BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		try {
			//System.out.println("please enter serverIp");
			//String ip = br.readLine();
			//System.out.println("please enter serverPort");
			//int port= Integer.parseInt(br.readLine());
			//System.out.println("please enter your nickname");
			//String nickname = br.readLine();
			
			String ip = "127.0.0.1";
			int port = 8080;
			String nickname = "test";
			
			new Client(ip, port , nickname);

		} catch (Exception e) {
			
		}
	
	}
	
	//metodo para enviar objetos por medio del socket
	public void sendObject(Object messageObj, ObjectOutputStream outputStream) throws IOException {
		outputStream.writeObject(messageObj);
		outputStream.flush();
	}

}
