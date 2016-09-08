/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tcpchatserver.server;

import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static sun.net.www.protocol.http.AuthCacheValue.Type.Server;

/**
 *
 * @author Staal
 */
public class EchoServerTest {

    public EchoServerTest() {
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
     * Test of stopServer method, of class EchoServer.
     */
    @Test
    public void testStopServer() {
        System.out.println("stopServer");
        EchoServer.stopServer();
        // TODO review the generated test code and remove the default call to fail.
      
    }

    /**
     * Test of sendMulticast method, of class EchoServer.
     */
    @Test
    public void testSendMulticast() {
        System.out.println("sendMulticast");
        String message = "";
        EchoServer instance = new EchoServer();
        instance.sendMulticast(message);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of removeHandler method, of class EchoServer.
     */
    @Test
    public void testRemoveHandler() {
        System.out.println("removeHandler");
        ClientHandler handler = null;
        EchoServer instance = new EchoServer();
        instance.removeHandler(handler);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of getClientHandlers method, of class EchoServer.
     */
    @Test
    public void testGetClientHandlers() {
        System.out.println("getClientHandlers");
        EchoServer instance = new EchoServer();
        List<ClientHandler> expResult = instance.getClientHandlers();
        List<ClientHandler> result = instance.getClientHandlers();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
      
    }

    /**
     * Test of main method, of class EchoServer.
     */
    @Test
    public void testMain() {
        System.out.println("main");
        info = null;
        EchoServer.main(info);
        // TODO review the generated test code and remove the default call to fail.
       
    }

}
