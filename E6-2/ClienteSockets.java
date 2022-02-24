import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class ClienteSockets {
    private static final int _PUERTO = 1234;

    public static void main(String args[]){
        InetAddress ipServidor = null;

        try{
            ipServidor = InetAddress.getByName(args[0]);
        } catch (UnknownHostException uhe){
            System.err.println("Host no encontrado : " + uhe);
            System.exit(-1); 
        }
        int noPaquetes = Integer.parseInt(args[1]);

        for (int n = 1; n <= noPaquetes; n++){
            Socket socketCliente = null;
            DataInputStream datosRecepcion = null;
            DataOutputStream datosEnvio = null;

            try{
                double temperatura = Math.round((Math.random()*40+16)*100.0)/100.0;
                int humedad = (int) Math.random()*1+99;
                double Co2 = Math.round((Math.random()*200+50000)*100.0)/100.0;

                char tipo = 's';
                String data = "$Temp|" + temperatura + "#Hum|" + humedad + "%#Co2|"+ Co2 + "$";
                byte[] dataInBytes = data.getBytes(StandardCharsets.UTF_8);

                socketCliente = new Socket(ipServidor, _PUERTO);

                datosRecepcion = new DataInputStream(socketCliente.getInputStream());
                datosEnvio = new DataOutputStream(socketCliente.getOutputStream());

                datosEnvio.writeChar(tipo);
                datosEnvio.writeInt(dataInBytes.length);
                datosEnvio.write(dataInBytes);

                String resultado = datosRecepcion.readUTF();

                System.out.println("Solicitud = "+ data + "\tResultado = " + resultado);

                datosRecepcion.close();
                datosEnvio.close();
            } catch (Exception e){
                System.err.println("Se ha producido la excepcion : " + e);
            }
            try{
                if(socketCliente != null)
                socketCliente.close();
            } catch (IOException ioe){
                System.err.println("Error al cerrar el socket : " + ioe);
            }
        }
    }
    
}
