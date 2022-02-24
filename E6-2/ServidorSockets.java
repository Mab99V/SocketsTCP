import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ServidorSockets {
    private static final String _IP = "192.168.1.69";
   // private static final String _IP = "25.7.227.46";
    private static final int _PUERTO = 1234;
    private static final int _BACKLOG = 50;

    public static void main(String args[]) throws UnknownHostException{
        InetAddress ip = InetAddress.getByName(_IP);

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss:SS");

        System.out.println("\nEscuchando en: ");
        System.out.println("IP Host = " + ip.getHostAddress());
        System.out.println("Puerto = " + _PUERTO);
        ServerSocket serverSocket = null;
        try{
            serverSocket = new ServerSocket(_PUERTO,_BACKLOG,ip);
        }catch (IOException ioe){
            System.err.println("Error aal abrir el socket de servidor : " + ioe);
            System.exit(-1);
        }
        while (true){
            try{
                Socket socketPeticion = serverSocket.accept();

                DataInputStream datosEntrada = new DataInputStream( new BufferedInputStream(socketPeticion.getInputStream()));
                DataOutputStream datosSalida = new DataOutputStream(socketPeticion.getOutputStream());

                int puertoRemitente = socketPeticion.getPort();

                InetAddress ipRemitente = socketPeticion.getInetAddress();

                char tipoDato = datosEntrada.readChar();

                int longitud = datosEntrada.readInt();

                if (tipoDato == 's'){
                    byte[] bytesDatos = new byte[longitud];

                    boolean finData = false;

                    StringBuilder dataEnMensaje = new StringBuilder(longitud);

                    int totalBytesLeidos = 0;

                    while (!finData){
                        int bytesActualesLeidos = datosEntrada.read(bytesDatos);

                        totalBytesLeidos = bytesActualesLeidos + totalBytesLeidos;

                        if (totalBytesLeidos <= longitud){
                            dataEnMensaje.append(new String(bytesDatos, 0, bytesActualesLeidos, StandardCharsets.UTF_8));
                        } else {
                            dataEnMensaje.append(new String(bytesDatos, 0, longitud - totalBytesLeidos + bytesActualesLeidos, StandardCharsets.UTF_8));
                        }
                        if (dataEnMensaje.length() >= longitud)
                        finData = true;
                    }
                    System.out.println(formatter.format(new Date()) + "\tCliente = " + ipRemitente + ":" + puertoRemitente 
                                       + "\tEntrada = " + dataEnMensaje.toString() + "\tSalida = " + "OK");
                }
                datosSalida.writeUTF("OK");

                datosEntrada.close();
                datosSalida.close();
                socketPeticion.close();
            } catch (Exception e){
                System.err.println("Se ha producido la excepcion : " + e);
            }
        }
    }
}
