

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ServidorTCP {
    //Se configura la IP y el puerto para el servidor
    private static final String _IP = "192.168.1.69";
    private static final int _PUERTO = 1234;
    private static final int _BACKLOG = 50;
    
    public static void main(String args[]) throws UnknownHostException{

        // Se crea instancia de clase InetAddress para indicar el host donde se inicia el servidor
        InetAddress ip = InetAddress.getByName(_IP);

        //Se usa un manejador de formato para el log del servidor
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        //uso de una ip
        System.out.println("\nEscuchando en: ");
        System.out.println("IP Host = "+ ip.getHostAddress());
        System.out.println("Puerto = " + _PUERTO);
        // Se abre un socket de servidor TCP en el puerto 1234
        ServerSocket serverSocket = null;
        try{
            serverSocket = new ServerSocket(_PUERTO,_BACKLOG,ip);
        }catch (IOException ioe){
            System.err.println("Error al abrir el socket de servidor : " + ioe);
            System.exit(-1);
        }
        int entrada;
        int salida;

        //Bucle infinito
        while(true){
            try{
                //Se espera a que alguien se conecte a nuestro a socket
                Socket socketPeticion = serverSocket.accept();

                //Se extrae los flujos de entrada y salida
                DataInputStream datosEntrada = new DataInputStream(socketPeticion.getInputStream());
                DataOutputStream datosSalida = new DataOutputStream(socketPeticion.getOutputStream());

                //Se puede extraer informacion del socket N° de puerto remoto
                int puertoRemitente = socketPeticion.getPort();
                //Direccion de internet remota
                InetAddress ipRemitente = socketPeticion.getInetAddress();

                //Leemos datos de la peticion
                entrada = datosEntrada.readInt();

                //Hacemos el calculo correspondiente
                salida = (int) ((long) entrada * (long) entrada);

                //Escribimos el resultado
                datosSalida.writeLong(salida);

                //Se cirran los flujos
                datosEntrada.close();
                datosSalida.close();
                socketPeticion.close();

                // se registra la salida en log
                System.out.println(formatter.format(new Date()) + "Cliente = " + ipRemitente +
                                   ":" + puertoRemitente + "\tEntrada = " + entrada + "\tSalida = " + salida);
            } catch (Exception e){
                System.err.println("Se ha producido la excepcion : " + e);
            }
        }
    }
}
