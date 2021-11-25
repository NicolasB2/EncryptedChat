package modelo;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.PublicKey;

import javax.crypto.KeyAgreement;

import utils.AES;
import utils.DiffieHellman;

public class ClientReceiveThread extends Thread {

	public Client client;
	public boolean hasKey = false;
	ObjectInputStream in;
	ObjectOutputStream out;

	public ClientReceiveThread(Client client, ObjectInputStream in, ObjectOutputStream out) {

		this.client = client;
		this.in = in;
		this.out = out;
	}

	public void run() {
		try {
			
			while (client.isClientConected()) {
				
				
					Object message = in.readObject();
					if(message instanceof String && message.equals("startHandshake")) {
						//Se continua si el mensaje recibido indica que el cliente debe iniciar el handshake. Se inicia contactando el otro cliente
						//preparando para el intercambio de llaves
						out.writeObject("I want to start a handshake");
						out.flush();
						//Se genera la llave publica y privada de esta instancia. Se genera el agreement
						DiffieHellman.generarPublicKeyEncode(client);
						//Se envia la llave publica al otro cliente
						out.writeObject(client.getPublicKey());
						out.flush();
						//Se recibe la llave publica del cliente
						Object publicKeyObject = in.readObject();
						PublicKey publicKeyRecived = (PublicKey) publicKeyObject;
						//Se genera la llave secreta a partir del keyagreement de este cliente, y la llave publica recibida del otro
						byte[] sendBobSharedSecret = DiffieHellman.getSecretKey( publicKeyRecived.getEncoded(), client.getKeyAgreement());
						//Se envia un arreglo vacio con el tamaño que deberia tener la llave secreta del otro cliente para evitar
						//la diferencia o incompatibilidad de llaves
						out.writeObject(sendBobSharedSecret);
						out.flush();
					}else if(message instanceof String && message.equals("I want to start a handshake")) {
						//Se continua si el mensaje recibido indica que otro cliente quiere iniciar el handshake con esta intancia
						//Se recibe la llave publica del otro cliente que quizo iniciar el handshake
						Object publicKeyAliceObject = in.readObject();
						PublicKey publicKeyAlice = (PublicKey) publicKeyAliceObject;
						//Se genera la llave publica a partir de la recibida
						DiffieHellman.generarPublicKeyApartirDeOtra(publicKeyAlice.getEncoded(), client);
						//Se envia dicha llave publica al cliente que inicio el handshake
						out.writeObject(client.getPublicKey());
						out.flush();
						//Se recibe un arreglo de bytes de tamaño 2048 vacio en el cual se generara la llave secreta de este cliente
						Object bobSharedSecretObject = in.readObject();
						byte[] bobShared = (byte[]) bobSharedSecretObject;
						//Se genera la llave secreta de este cliente
						DiffieHellman.getSecretKeyBob(client.getKeyAgreement(), publicKeyAlice, bobShared);
						
					}else {
						//Cualquier otro objeto recibido se considera la comunicacion entre clientes
						System.out.println(AES.getEncrypter().decryptMessage((byte[]) message));
					}
				
				
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
