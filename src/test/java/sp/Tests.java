package sp;

import org.junit.Test;

import java.util.Random;

public class Tests {
    @Test
    public void test() {
        int port = new Random().nextInt(65000);

        //Server server = new Server(port);
        Server server = new Server(port);

        System.out.println("server started");
        Client client = new Client(port);
        System.out.println(client.send("hello"));
        System.out.println(client.send("bue"));
        client.stop();
        server.stop();
    }
}
