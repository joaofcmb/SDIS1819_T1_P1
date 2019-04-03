package peer;

import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import client.ClientInterface;
import multicast.MulticastInterface;
import multicast.MulticastThread;
import storage.StorageManager;

public class Peer {
    private static String protocolVersion;
    private static String id;
    private static String accessPoint;

    public static MulticastInterface mc, mdb, mdr;

    private static final ThreadPoolExecutor protocolThreadPool = new ThreadPoolExecutor(
            10, Integer.MAX_VALUE, 15, TimeUnit.SECONDS, new SynchronousQueue<>());

    private static final ThreadPoolExecutor multicastThreadPool = new ThreadPoolExecutor(
            8, 8, 1, TimeUnit.SECONDS, new LinkedBlockingQueue<>());

    public static void main(String[] args) {
        initPeerInfo(args);
        StorageManager.storageSetup();
        initRMI();
        initMulticast(args);
    }

    private static void initPeerInfo(String[] args) throws IllegalArgumentException {
        if (args.length != 9)
            throw new IllegalArgumentException();

        Peer.protocolVersion = args[0];
        Peer.id = args[1];
        Peer.accessPoint = args[2];
    }

    private static void initRMI() {
        try {
            Service service = new Service();
            ClientInterface stub = (ClientInterface) UnicastRemoteObject.exportObject(service, 0);
            LocateRegistry.getRegistry().bind(Peer.accessPoint, stub);
        } catch (Exception e) {
            System.err.println("Peer(" + id + ") - RMI exception: " + e.toString());
            e.printStackTrace();
        }

        System.out.println("Peer(" + id + ") - RMI Done");
    }


    private static void initMulticast(String[] args) {
        mc = new MulticastInterface(args[3], Integer.parseInt(args[4]));
        mdb = new MulticastInterface(args[5], Integer.parseInt(args[6]));
        mdr = new MulticastInterface(args[7], Integer.parseInt(args[8]));

        new MulticastThread(mc).start();
        new MulticastThread(mdb).start();
        new MulticastThread(mdr).start();
    }

    public static synchronized String getProtocolVersion() {
        return protocolVersion;
    }

    public static synchronized String getId() {
        return id;
    }

    public static ThreadPoolExecutor getProtocolThreadPool() {
        return protocolThreadPool;
    }

    public static ThreadPoolExecutor getMulticastThreadPool() {
        return multicastThreadPool;
    }
}