package modelo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;

import utils.AES;

public class ClientSendThread extends Thread {

	public Client client;
	public boolean hasKey = false;
	ObjectOutputStream out;

	public ClientSendThread(Client client, ObjectOutputStream out) {
		this.client = client;
		this.out = out;
	}

	public void run() {

		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			String message = "";

			while (client.isClientConected()) {
				message = br.readLine();
				client.sendObject(AES.getEncrypter().encryptMessage(message), out);
			}
		} catch (Exception e) {

		}

	}

}
