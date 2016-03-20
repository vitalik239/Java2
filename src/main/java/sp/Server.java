package sp;

import java.net.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Server {
    private ServerSocket serverSocket;
    private Path rootPath;

    public Server(int port, Path rootPath) {
        this.rootPath = rootPath;
        try {
            serverSocket = new ServerSocket(port);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public synchronized Socket accept() throws IOException {
        if ((serverSocket == null) || (serverSocket.isClosed())) {
            return null;
        }
        return serverSocket.accept();
    }

    public void start() {
        new Thread(() -> {
            while (true) {
                try {
                    Socket socket = accept();
                    if (socket != null) {
                        new Thread(() -> workWithSocket(socket)).start();
                    }
                } catch (IOException ex) {
                    return;
                }
            }
        }).start();
    }

    public void workWithSocket(Socket socket) {
        if (socket.isClosed()) {
            return;
        }
        DataInputStream in = null;
        DataOutputStream out = null;
        try {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        while (!socket.isClosed()) {
            int requestType;
            try {
                requestType = in.readInt();
                Path path = Paths.get(rootPath.toString() + in.readUTF());

                File file = path.toFile();

                if (requestType == Contract.GET) {
                    if (file.exists() && !file.isDirectory()) {
                        out.writeLong(file.length());
                        out.flush();
                        Files.copy(file.toPath(), out);
                    } else {
                        out.writeLong(0);
                    }
                } else if (requestType == Contract.LIST) {
                    if (file.exists() && file.isDirectory()) {
                        File[] files = file.listFiles();
                        out.writeLong(files.length);
                        for (File f : files) {
                            out.writeUTF(f.getName());
                            out.writeBoolean(f.isDirectory());
                            out.flush();
                        }
                    } else {
                        out.writeLong(0);
                    }
                } else {
                     return;
                }
            } catch (Exception ex) {
                return;
            }
        }
    }

    public void stop() {
        if (serverSocket == null) {
            return;
        }
        try {
            serverSocket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        serverSocket = null;
    }
}
