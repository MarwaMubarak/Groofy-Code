package com.groofycode.GroofyCode.utilities;

import java.io.*;

public class BlobConverter {
    public BlobConverter() {
    }

    public Object convertBlobToObject(byte[] blob) throws Exception {
        ByteArrayInputStream bais = new ByteArrayInputStream(blob);
        ObjectInputStream ois = new ObjectInputStream(bais);
        return ois.readObject();
    }

    public <T> byte[] convertObjectToBlob(T object) throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(object);
        objectOutputStream.flush();
        objectOutputStream.close();
        return byteArrayOutputStream.toByteArray();
    }
}
