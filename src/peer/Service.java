package peer;

import java.io.IOException;

import client.ClientInterface;
import storage.StorageManager;

public class Service implements ClientInterface {
    @Override
    public boolean backup(String path, int replicationDegree) {
        try {
            System.out.println("BACKUP COMMAND: " + path + " " + replicationDegree);

            for (byte[] chunk : StorageManager.fileToChunks(path))
                Peer.getProtocolThreadPool().execute(BackupInitiator);

            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public boolean restore(String path) {
        System.out.println("RESTORE COMMAND: " + path);
        return false;
    }

    @Override
    public boolean delete(String path) {
        System.out.println("DELETE COMMAND: " + path);
        return false;
    }

    @Override
    public boolean reclaim(int diskSpace) {
        System.out.println("RECLAIM COMMAND: " + diskSpace);
        return false;
    }

    @Override
    public boolean state() {
        System.out.println("STATE COMMAND");
        return false;
    }

}