# Proyecto Final Seguridad
## Chat Encriptado

### Integrantes:
- *Nicolás Biojo*
- *David Bolivar*
- *David Erazo*
- *Carlos Díaz*

### Desarrollo del programa


Para el desarrollo del presente proyecto, se dividió la lógica del negocio en dos paquetes, uno de ellos llamado modelo el cual contiene la lógica referente a un chat peer to peer to peer, y el otro llamado utils referente a las herramientas para poder realizar cifrado sobre los mensajes. Por un lado, el paquete modelo cuenta con la arquitectura Cliente-Servidor para lograr el chat, así mismo se implementaron los respectivos Threads para lograr que el chat. Por otro lado, el paquete utils cuenta con la clase Diffie-Hellman la cual contiene el protocolo de establecimiento de claves entre partes que no han tenido un contacto previo, y la clase AES la cual contiene el algoritmo que implementa el esquema de cifrado por bloques.  

### Difícultades

Una de la principales dificultades que se encontraron al desarrollar el proyecto, se presentaron en el momento de la generación de las claves por medio del protocolo criptográfico Diffie-Hellman, debido a que cuando se realizaba la prueba del envio del mensaje, la persona que lo recibia no lo podía desencriptar ya que no contaba con la misma clave pública.  

### Conclusiones

Finalmente, se optuvo una gran experiencia al realizar este proyecto ya que todos los integrantes del grupo en algún momento de la carrera habíamos realizado la implemetación de distintos tipos de chats, como por ejemplo chats de conexión peer to peer o bradcasting, pero con la diferencia que no contaban con una protección en los mensajes que se enviaban o recibian, factor que dejo gran aprendizaje en el desarrollo del presente proyecto debido a la implementación de algúnas de las disintas maneras de encriptado de texto.
