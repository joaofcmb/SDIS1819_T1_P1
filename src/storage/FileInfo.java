package storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FileInfo {
    private static final int CHUNK_SIZE = 64000;

    private final File file;
    private final byte[][] chunks;
    private int[] replication;

    FileInfo(File file) throws IOException {
        int chunkNum = Math.toIntExact(file.length() / CHUNK_SIZE + 1);

        this.file = file;
        this.chunks = new byte[chunkNum][CHUNK_SIZE];
        this.replication = new int[chunkNum];

        FileInputStream fileInput = new FileInputStream(file);

        for (byte[] chunk : this.chunks)
            fileInput.read(chunk);
    }

    public void incReplication(int chunkNo) {
        if (chunkNo < this.replication.length)
            this.replication[chunkNo]++;
    }

    public byte[][] getChunks() {
        return chunks;
    }

    public int getReplication(int chunkNo) {
        return this.replication[chunkNo];
    }
}
