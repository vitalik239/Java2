package sp;

import org.junit.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static org.junit.Assert.assertEquals;

public class Tests {
    public static final int PORT = 65535;
    public static final String ROOT = "src/test/resources/testfolder";
    public static final String ADDRESS = "127.0.0.1";

    @Test
    public void testList() {
        int port = new Random().nextInt(PORT);

        Server server = new Server(port, Paths.get(ROOT));
        server.start();

        Client client = new Client(ADDRESS, port);
        client.connect();

        List<FileInfo> list = new ArrayList<>();
        list.add(new FileInfo("file1.txt", false));
        list.add(new FileInfo("file3.txt", false));
        list.add(new FileInfo("folder", true));

        List<FileInfo> answer = client.sendList("");

        assertEquals(list.size(), answer.size());
        for (int i = 0; i < answer.size(); i++) {
            assertEquals(list.get(i), answer.get(i));
        }

        client.disconnect();
        server.stop();
    }

    @Test
    public void testGet() {
        int port = new Random().nextInt(PORT);
        Server server = new Server(port, Paths.get(ROOT));
        server.start();

        Client client = new Client(ADDRESS, port);
        client.connect();

        Path finalPath = Paths.get(ROOT + "/file4.txt");
        Path startPath = Paths.get(ROOT + "/folder/file2.txt");
        client.sendGet("/folder/file2.txt", finalPath);
        try {
            assertEquals(Files.readAllLines(finalPath), Files.readAllLines(startPath));
            Files.deleteIfExists(finalPath);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        client.disconnect();
        server.stop();
    }
}