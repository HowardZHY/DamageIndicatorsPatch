package com128.kzf.m.dip;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.launchwrapper.Launch;

public class DamageIndicatorsPatchTransformer implements IClassTransformer {
    public ZipFile zip;

    public DamageIndicatorsPatchTransformer() {
        try{
            URL[] urls = Launch.classLoader.getURLs();
            for(URL url : urls){
                ZipFile zip = getZip(url);
                if(zip != null){
                    this.zip = zip;
                    break;
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        if (this.zip == null)
            System.out.println("[DIP] Couldn't load Damage Indicator Patch! Jar is not on the classpath.");
    }

    private ZipFile getZip(URL url) {
        try {
            URI uri = url.toURI();
            File file = new File(uri);
            ZipFile zip = new ZipFile(file);
            if (zip.getEntry(getClass().getName().replace(".", "/").concat(".class")) == null) {
                zip.close();
                return null;
            }
            return zip;
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        byte[] bytes = getClass(name);
        if (bytes != null)
            return bytes;
        return basicClass;
    }

    private byte[] getClass(String name) {
        if (this.zip == null)
            return null;
        String fullName = name + ".class";
        ZipEntry ze = this.zip.getEntry(fullName);
        if (ze == null)
            return null;
        try {
            InputStream in = this.zip.getInputStream(ze);
            byte[] bytes = readAll(in);
            if (bytes.length != ze.getSize())
                return null;
            return bytes;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private byte[] readAll(InputStream in) throws IOException {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        while (true) {
            int len = in.read(buf);
            if (len < 0)
                break;
            b.write(buf, 0, len);
        }
        in.close();
        return b.toByteArray();
    }
}
