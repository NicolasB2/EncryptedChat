package modelo;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.sound.midi.Receiver;

public class ServerThread extends Thread {

	private Server server;
	private Socket reciveSocket;
	private Socket sendSocket;
	private int number;
	private boolean handshake;

	public ServerThread(Socket resiveSocket, Socket sendSocket, boolean handshake) {
		this.reciveSocket = resiveSocket;
		this.sendSocket = sendSocket;
		this.handshake = handshake;
	}

	public void run() {

		try {
			
			ObjectOutputStream out = new ObjectOutputStream(sendSocket.getOutputStream());
			ObjectInputStream in = new ObjectInputStream(reciveSocket.getInputStream());
			
			System.out.println("start server thread");
			
			while (true) {
				
				if(handshake) {
					
			//		Object received = in.readObject();
			//		byte[] publickey = (byte[]) received;
				    String messageHandshake = "startHandshake";
					out.writeObject(messageHandshake);
					out.flush();
					
//					for (int i = 0; i < server.getSockets().size(); i++) {
//						
//						if (i != number) {
//							Object received = in.readObject();
//							byte[] publickey = (byte[]) received;
//							out.writeObject(publickey);
//							out.flush();
//						}
//					}
					
					handshake = false;
				}else {
					
					Object message =  in.readObject();
					out.writeObject(message);
					out.flush();					
//					for (int i = 0; i < server.getSockets().size(); i++) {
//						
//						
//						if (i != number) {
//							Socket actual = server.getSockets().get(i);
//							out = new ObjectOutputStream(actual.getOutputStream());
//							server.sendObject(message.getBytes(), out);
//							out.flush();
//						}
//						
//					}
					
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
