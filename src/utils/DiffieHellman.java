/*
 * Basado en: https://stackoverflow.com/questions/21081713/diffie-hellman-key-exchange-in-java
 */

package utils;

import java.security.*;
import java.security.spec.*;
import javax.crypto.*;
import javax.crypto.interfaces.*;
import javax.crypto.spec.*;

import modelo.Client;

public class DiffieHellman {

	/*
	 * Retorna el publicKEyEncode (Se usa cuando Es ALICE Inicia la conexión)
	 * 
	 */
	public static KeyPair generarPublicKeyEncode(Client client) {
		KeyPair keypair = null;
		try {
			// GENERA KEYPAIR
			KeyPairGenerator keypairG = KeyPairGenerator.getInstance("DH");
			keypairG.initialize(2048);
			keypair = keypairG.generateKeyPair();

			// Genera el keyagreement
			KeyAgreement agreement = KeyAgreement.getInstance("DH");
			agreement.init(keypair.getPrivate());

			client.setKeyAgreement(agreement);
			client.setPublicKey(keypair.getPublic());
			// arreglo de bytes de la public key encode
			byte[] publicKeyEncode = keypair.getPublic().getEncoded();

			// Aki deberiamos llamar el enviar(?) ¿Cambiamos el parametro? :'v
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}
		return keypair;
	}

	/*
	 * Crea una public Key a partir de una obtenida (Se usa cuando es Bob, recibe la
	 * conexión) 
	 * @param client: El cliente que actualmente está ejecutando las acciónes
	 *
	 * @param publicKeyInEncode: Llave pública entrante en formato encode
	 *
	 * @return retorna el KeyPair
	 */

	public static KeyPair generarPublicKeyApartirDeOtra(byte[] publicKeyInEncode, Client client) {
		KeyFactory keyfactor = null;
		KeyPair KeyPairG = null;
		try {
			keyfactor = KeyFactory.getInstance("DH");
			X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(publicKeyInEncode);
			// Se toma la llave en formato encode y se pasa a formato PublicKey a través del
			// KeyFactor y el X509Encoded
			PublicKey publicKeyEntrante = keyfactor.generatePublic(x509KeySpec);
			// Parametros del DiffiHellman obtenidos a partir de la llave publica recibida
			DHParameterSpec dhParamFromPubKeyEntrante = ((DHPublicKey) publicKeyEntrante).getParams();
			// Se Crea el KeyPairGenerator a partir de los parametros sacados de la llave
			// Publica entrante
			KeyPairGenerator KeyPairGen = KeyPairGenerator.getInstance("DH");
			KeyPairGen.initialize(dhParamFromPubKeyEntrante);
			// Se genera el KeyPair
			KeyPairG = KeyPairGen.generateKeyPair();

			// Se crea el Agreement en el B
			System.out.println("BOB: Initialization ...");
			KeyAgreement KeyAgreementB = KeyAgreement.getInstance("DH");
			KeyAgreementB.init(KeyPairG.getPrivate());
			client.setKeyAgreement(KeyAgreementB);

			// Aqui se debe enviar la clave a Alice
			byte[] publicKeyEnc = KeyPairG.getPublic().getEncoded();
			client.setPublicKey(KeyPairG.getPublic());
			// Llave a enviar
			PublicKey publicKeySend = KeyPairG.getPublic();
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return KeyPairG;
	}

	/*
	 * Siendo BOB, recibo la clave de Alice y genero el ScretKeySpec
	 * 
	 * @param El KeyAgreement debe ser el mismo generado arriba
	 * 
	 * @param El publicKeyAlice es la llave publica de Alice
	 * 
	 * @param bobSharedSecret Entra por parametro el arrglo de bytes del mismo tamaño que la SecretSharedKey de Alice
	 *
	 * @return retorna el SecretKeySpect generado por Bob
	 */

	public static SecretKeySpec getSecretKeyBob(KeyAgreement keyAgreement, PublicKey publicKeyAlice,byte[] bobSharedSecret) {
		SecretKeySpec bobAesKey = null;
		try {
			
			keyAgreement.doPhase(publicKeyAlice, true);

			int bobLen = keyAgreement.generateSecret(bobSharedSecret, 0); 
			bobAesKey = new SecretKeySpec(bobSharedSecret, 0, 16, "AES"); 
			AES.getEncrypter().setCurrentKey(bobAesKey);
			System.out.println(new String(bobAesKey.getEncoded()));
		} catch (InvalidKeyException | IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ShortBufferException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bobAesKey;
	}
/*
	 * Siendo Alice, recibo la clave de BOB y genero el ScretKeySpec
	 * 
	 * @param El KeyAgreement debe ser el generado por el que realiza el método
	 * 
	 * @param El PubliKeyEnc es la llave publica de Bob
	 * @return Retorna un arreglo de byte vacio, del tamaño de el SharedSecret Key de Alice
	 * 
	 */
	
	
	public static byte[] getSecretKey(byte[] publKeyEnc, KeyAgreement keyAgreement) {

		try {
			KeyFactory KeyFac = KeyFactory.getInstance("DH");
			X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(publKeyEnc);
			PublicKey PubKeyEntranteB = KeyFac.generatePublic(x509KeySpec);
			System.out.println("ALICE: Execute PHASE1 ...");

			keyAgreement.doPhase(PubKeyEntranteB, true);
		} catch (InvalidKeyException | IllegalStateException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		byte[] aliceSharedSecret = null;
		int aliceLen = 0;
		byte[] bobSharedSecret = new byte[aliceLen]; // Pasar a otro metodo/Hacer en el otro lado
		int bobLen;

		try {
			aliceSharedSecret = keyAgreement.generateSecret();
			aliceLen = aliceSharedSecret.length;
			bobSharedSecret = new byte[aliceLen]; 

		} catch (Exception e) {
			System.out.println(e.getMessage());
		} 

		SecretKeySpec aliceAesKey = new SecretKeySpec(aliceSharedSecret, 0, 16, "AES");
		
		System.out.println(new String(aliceAesKey.getEncoded()));

		AES.getEncrypter().setCurrentKey(aliceAesKey);

		return bobSharedSecret;
	}

}
