package tcpchatserver.client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import tcpchatserver.shared.ProtocolStrings;

public class EchoClient {

    Socket socket;
    private int port;
    private InetAddress serverAddress;
    private Scanner input;
    private PrintWriter output;

    public void connect(String address, int port) throws UnknownHostException, IOException {
        this.port = port;
        serverAddress = InetAddress.getByName(address);
        socket = new Socket(serverAddress, port);
        input = new Scanner(socket.getInputStream());
        output = new PrintWriter(socket.getOutputStream(), true);  //Set to true, to get auto flush behaviour
    }

    public boolean isClosed() {
        return socket.isClosed();
    }

    public void send(String msg) {
        output.println(msg);
    }

    public void stop() throws IOException {
        output.println(ProtocolStrings.LOGOUT);
    }

    public String receive() {
        String msg = input.nextLine();
        String[] cmd = msg.split(":");
        
        if (cmd[0].equals(ProtocolStrings.CLIENTLIST) || cmd[0].equals(ProtocolStrings.MSGRES)) {
            return msg;
        }
        
        return null;
    }

    public static void main(String[] args) {
        int port = 7777;
        String ip = "localhost";
        if (args.length == 2) {
            ip = args[0];
            port = Integer.parseInt(args[1]);
        }
        try {
            EchoClient tester = new EchoClient();
            tester.connect(ip, port);
            tester.send("LOGIN:Hello");
            System.out.println("Received: " + tester.receive()); //Important Blocking call
            tester.send("MSG::Hello");
            System.out.println("Received: " + tester.receive()); //Important Blocking call
            tester.stop();   
        } catch (UnknownHostException ex) {
            Logger.getLogger(EchoClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(EchoClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
