package sp;

import java.net.*;
import java.io.*;

public class Server {
    private ServerSocket serverSocket;

    public Server(int port) {
        try {
            serverSocket = new ServerSocket(port);
            new Thread(this::start).start();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public synchronized Socket accept() throws IOException {
        return serverSocket.accept();
    }

    public void start(){
        while (true) {
            try {
                Socket socket;
                try {
                    socket = accept();
                } catch (IOException ex) {
                    return;
                }
                if (socket != null)
                    new Thread(() -> workWithSocket(socket)).start();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void workWithSocket(Socket socket) throws IOException {
        DataInputStream in;
        DataOutputStream out;

        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());

        String line = null;

        while (true) {
            line = in.readUTF();
            out.writeUTF(line);
            out.flush();
        }
    }


    public void stop() {
        try {
            serverSocket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}