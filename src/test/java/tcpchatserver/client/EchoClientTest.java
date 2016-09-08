/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tcpchatserver.client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Scanner;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static sun.net.www.protocol.http.AuthCacheValue.Type.Server;
import tcpchatserver.server.EchoServer;

/**
 *
 * @author Staal
 */
public class EchoClientTest {
    private Socket socket;
    public EchoClientTest() {
    }
   String[] info = new String[] {"localhost","9002"};

    @BeforeClass
    public static void setUpClass() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                EchoServer.main(null);
            }
        }).start();
    }

    @AfterClass
    public static void tearDownClass() {
        EchoServer.stopServer();
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of connect method, of class EchoClient.
     */
    @Test
    public void testConnect() throws Exception {
        System.out.println("connect");
        String address = "localhost";
        int port = 9002;
        EchoClient instance = new EchoClient();
        instance.connect(address, port);
        
      
    }

    /**
     * Test of isClosed method, of class EchoClient.
     */
    @Test
    public void testIsClosed() throws IOException {
        System.out.println("isClosed");
        EchoClient instance = new EchoClient();
        instance.connect("localhost", 9002);
        SocketAddress localsocket = instance.socket.getLocalSocketAddress();
        boolean expResult = localsocket.equals(instance.isClosed());
        boolean result = instance.isClosed();
        assertEquals(expResult, result);
   
       
    }

    /**
     * Test of send method, of class EchoClient.
     */
    @Test
    public void testSend() throws IOException {
        System.out.println("send");
        String msg = "";
        EchoClient instance = new EchoClient();
        instance.connect("localhost", 9002);
        instance.socket.getLocalSocketAddress();
        instance.send(msg);
        
        
    }

    /**
     * Test of stop method, of class EchoClient.
     */
    @Test
    public void testStop() throws Exception {
        System.out.println("stop");
        EchoClient instance = new EchoClient();
        instance.connect("localhost", 9002);
        instance.socket.getLocalSocketAddress();
        instance.stop();
       
       
    }

    /**
     * Test of receive method, of class EchoClient.
     */
    @Test
    public void testReceive() throws IOException {
        System.out.println("receive");
        EchoClient instance = new EchoClient();    
        instance.connect("localhost", 9002);
        instance.send("LOGIN:Staal");
        String expResult = instance.receive();
        assertTrue(expResult.equals("CLIENTLIST:Staal"));
    }

    /**
     * Test of main method, of class EchoClient.
     */
    @Test
    public void testMain() {
        System.out.println("main");
        
        EchoClient.main(info);
       
        
    }

}
