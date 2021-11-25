package utils;

import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AES {

	private static AES aesInstance;
	private SecretKeySpec encriptionKey;
	private IvParameterSpec ivspec;
	
	private AES(){
		//Definición de vector de inicialización, parametro util para encripcion y desencripcion con cipher
		byte[] iv = new byte[128/8];
		SecureRandom srandom = new SecureRandom();
		srandom.nextBytes(iv);
		ivspec = new IvParameterSpec(iv);
	}
	
	//Metodo get instance de patron singleton sobre instancia AES
	public  static AES getEncrypter() {
		//Si es invocado por primera vez, se instancia la clase AES
		if (aesInstance==null) {
			aesInstance = new AES();
		}
		//Se retorna la instancia unica de AES.
		return aesInstance;
	}
	
	//Metodo set para guardar clave secreta comun generada despues del handshake y agreement.
	public void setCurrentKey(SecretKeySpec key) {
		encriptionKey = key;
	}
	
	//Metodo para encriptar mensajes usando el algoritmo AES.
	public byte[] encryptMessage(String msg) {
		try {
			byte[] input = msg.getBytes("UTF-8");
			//Se define el cipher
			Cipher ci = Cipher.getInstance("AES/CBC/PKCS5Padding");
			//Se inicializa el cipher basado en la llave y el IV.
			ci.init(Cipher.ENCRYPT_MODE, encriptionKey,  new IvParameterSpec(new byte[16]));
			
			//Se construye el mensaje a una arreglo de bytes. Y se se encripta el arreglo de bytes.
			return ci.doFinal(input);
			
		} catch (Exception e) {
			System.out.println("The message couldn't be encrypted. Please try again.");
			e.printStackTrace();
		} 
		
		return null;
	}
	
	//Metodo para desencriptar mensajes usando algoritmo AES
	public String decryptMessage(byte[] encryptedMsg) {
		try {
			//Se define el cipher
			Cipher ci = Cipher.getInstance("AES/CBC/PKCS5Padding");
			//Se inicializa el cipher basado en la llave y el IV.
			ci.init(Cipher.DECRYPT_MODE, encriptionKey,  new IvParameterSpec(new byte[16]));
			//Se construye el mensaje a una cadena de caracteres. Y se se desencripta el arreglo de bytes.
			return new String(ci.doFinal(encryptedMsg), "UTF-8");
			
		} catch (Exception e) {
			System.out.println("The message couldn't be decrypted. Please try again.");
			e.printStackTrace();
		} 
		
		return null;
	}
	
}
