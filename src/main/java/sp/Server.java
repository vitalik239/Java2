package sp;

import java.net.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Server {
    private ServerSocket serverSocket;
    private final Path rootPath;

    public Server(int port, Path rootPath) throws IOException {
        this.rootPath = rootPath;
        serverSocket = new ServerSocket(port);
    }

    private synchronized Socket accept() throws IOException {
        return serverSocket.accept();
    }

    public void start() {
        new Thread(() -> {
            while (true) {
                final Socket socket;
                try {
                    socket = accept();
                    new Thread(() -> workWithSocket(socket)).start();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    return;
                }
            }
        }).start();
    }

    private void workWithSocket(Socket socket) {
        DataInputStream in = null;
        DataOutputStream out = null;
        try {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException ex) {
            ex.printStackTrace();
            return;
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
                        Files.copy(file.toPath(), out);
                    } else {
                        out.writeLong(0);
                    }
                    out.flush();
                } else if (requestType == Contract.LIST) {
                    if (file.exists() && file.isDirectory()) {
                        File[] files = file.listFiles();
                        out.writeLong(files.length);
                        for (File f : files) {
                            out.writeUTF(f.getName());
                            out.writeBoolean(f.isDirectory());
                        }
                    } else {
                        out.writeLong(0);
                    }
                    out.flush();
                } else {
                     return;
                }
            } catch (Exception ex) {
                return;
            }
        }
    }

    public void stop() {
        if (serverSocket.isClosed()) {
            return;
        }
        try {
            serverSocket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
