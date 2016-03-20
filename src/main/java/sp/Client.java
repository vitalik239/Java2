package sp;

import java.net.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Client {
    private DataInputStream in;
    private DataOutputStream out;
    private Socket socket;
    private int serverPort;
    private InetAddress ipAddress;
    private static final int BUFFER_SIZE = 1024;

    public Client(String address, int serverPort) {
        this.serverPort = serverPort;
        try {
            ipAddress = InetAddress.getByName(address);
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
        }
    }

    public void connect() {
        try {
            socket = new Socket(ipAddress, serverPort);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void sendGet(String serverPath, Path savePath) {
        try {
            out.writeInt(Contract.GET);
            out.writeUTF(serverPath);
            out.flush();
            long size = in.readLong();
            if (size == 0) {
                System.out.println("File doesn't exist");
                return;
            }
            byte[] buffer = new byte[BUFFER_SIZE];
            int cnt;
            OutputStream output = Files.newOutputStream(savePath);
            try {
                while ((size > 0) && ((cnt = in.read(buffer)) > -1)) {
                    size -= cnt;
                    output.write(buffer, 0, cnt);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public List<FileInfo> sendList(String path) {
        List<FileInfo> list = new ArrayList<>();
        try {
            out.writeInt(Contract.LIST);
            out.writeUTF(path);
            out.flush();
            Long size = in.readLong();
            if (size == 0) {
                System.out.println("Folder doesn't exist");
                return Collections.emptyList();
            } else {
                for (int i = 0; i < size; i++) {
                    list.add(new FileInfo(in.readUTF(), in.readBoolean()));
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    public void disconnect() {
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
