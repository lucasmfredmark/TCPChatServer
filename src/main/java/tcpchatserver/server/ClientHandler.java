/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tcpchatserver.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import tcpchatserver.shared.ProtocolStrings;

/**
 *
 * @author jens
 */
public class ClientHandler implements Runnable {

    private final Socket socket;
    private final PrintWriter writer;
    private final EchoServer server;
    private final Scanner input;
    private boolean loggedIn;
    private String clientName;

    public ClientHandler(Socket socket, Scanner input, PrintWriter writer, EchoServer server) {
        this.socket = socket;
        this.writer = writer;
        this.server = server;
        this.input = input;
    }

    public static ClientHandler handle(Socket socket, EchoServer server) throws IOException {
        Scanner input = new Scanner(socket.getInputStream());
        PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
        return new ClientHandler(socket, input, writer, server);
    }

    @Override
    public void run() {
        try {
            while (true) {
                String message = input.nextLine();
                String[] cmd = message.split(":");

                if (!loggedIn) {
                    if (cmd[0].equals(ProtocolStrings.LOGIN)) {
                        loggedIn = true;
                        clientName = cmd[1];
                        printClientList();
                    } else {
                        break;
                    }
                } else {
                    if (cmd[0].equals(ProtocolStrings.MSG)) {
                        if (cmd[1].trim().isEmpty()) {
                            // send message to all
                            server.sendMulticast("MSGRES:" + clientName + ":" + cmd[2]);
                        } else {
                            // send message to recipients

                            String[] clientNames;

                            if (cmd[1].contains(",")) {
                                clientNames = cmd[1].split(",");
                            } else {
                                clientNames = new String[1];
                                clientNames[0] = cmd[1];
                            }

                            for (String u : clientNames) {
                                for (ClientHandler c : server.getClientHandlers()) {
                                    if (u.equals(c.getClientName())) {
                                        c.sendMessage("MSGRES:" + clientName + ":" + cmd[2]);
                                    }
                                }
                            }
                        }
                    } else if (cmd[0].equals(ProtocolStrings.LOGOUT)) {
                        // disconnect the client
                        break;
                    } else {
                        // invalid command
                        break;
                    }
                }
            }
        } finally {
            try {
                socket.close();
                server.removeHandler(this);
                printClientList();
                System.out.println("Closed a connection.");
            } catch (IOException ex) {
                Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void sendMessage(String message) {
        writer.println(message);
        writer.flush();
    }

    public void printClientList() {
        ArrayList<String> clientList = new ArrayList<>();

        for (ClientHandler c : server.getClientHandlers()) {
            if (c.getClientName() != null)
                clientList.add(c.getClientName());
        }

        server.sendMulticast("CLIENTLIST:" + String.join(",", clientList));
    }
    
    public boolean isLoggedIn() {
        return loggedIn;
    }
    
    public String getClientName() {
        return clientName;
    }
}
