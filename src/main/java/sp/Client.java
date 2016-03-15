package sp;
import java.net.*;
import java.io.*;

public class Client {
    private DataInputStream in;
    private DataOutputStream out;
    private Socket socket;

    public Client(int serverPort) {

        String address = "127.0.0.1";

        try {
            InetAddress ipAddress = InetAddress.getByName(address);
            socket = new Socket(ipAddress, serverPort);

            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public String send(String line) {
        String newLine = null;
        try {
            out.writeUTF(line);
            out.flush();

            newLine = in.readUTF();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return newLine;
    }

    public void stop() {
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}