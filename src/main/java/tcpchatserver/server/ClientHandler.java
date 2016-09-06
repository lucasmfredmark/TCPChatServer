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
                        if (!(cmd[1].contains(":") || cmd[1].contains(",")) && cmd[1].length() > 0) {
                            loggedIn = true;
                            clientName = cmd[1];
                            sendMessage("You have been logged in.");
                            printClientList();
                        } else {
                            sendMessage("You must specify a valid name.");
                            break;
                        }
                    } else {
                        break;
                    }
                } else {
                    if (cmd[0].equals(ProtocolStrings.MSG)) {
                        if (cmd[1].isEmpty()) {
                            server.sendMulticast(cmd[2]);
                        } else {
                            String[] usernames = cmd[1].split(",");

                            for (String u : usernames) {
                                for (ClientHandler c : server.getClientHandlers()) {
                                    if (u.equals(c.getClientName())) {
                                        c.sendMessage(cmd[2]);
                                        break;
                                    }
                                }
                            }
                        }
                    } else if (cmd[0].equals(ProtocolStrings.LOGOUT)) {
                        break;
                    }
                }
            }
        } finally {
            try {
                socket.close();
                server.removeHandler(this);
                server.sendMulticast(this.getClientName() + " has logged out.");
                System.out.println("Closed a connection.");
                printClientList();
                sendMessage("You have been logged out.");
            } catch (IOException ex) {
                Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void sendMessage(String message) {
        System.out.println("Sending " + message);
        writer.println(message);
        writer.flush();
    }

    public void printClientList() {
        ArrayList<String> clientList = new ArrayList<>();

        for (ClientHandler c : server.getClientHandlers()) {
            if (!c.getClientName().equals("null"))
                clientList.add(c.getClientName());
        }

        server.sendMulticast("CLIENTLIST:" + String.join(",", clientList));
    }

    public String getClientName() {
        return clientName;
    }
}
