package kokkiecoremods.asm;

import net.minecraft.launchwrapper.IClassTransformer;
import java.io.IOException;

import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.Arrays;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.security.ProtectionDomain;
public class CoreModExampleTransformer implements IClassTransformer {
	//static String JsonList;
    @Override
    public byte[] transform(final String name, final String transformedName, byte[] baseClass) {
		if(name.matches("com.fasterxml.*")) { return baseClass; }

		try {
   
			

            
			byte[] bytes= replaceClass(baseClass,name);
			if(Arrays.equals(bytes, baseClass)){
				return baseClass;
			}
			System.err.println("Class replace success! " );
			System.err.println("transformedName:" + transformedName);
			System.err.println("name: " + name);
			return bytes;
        } catch (IOException e) {
			System.err.println(e);
			return baseClass;
			
        }
		
       
    }
	
    private byte[] replaceClass(byte[] bytes,String filename) throws IOException
    {
        ZipFile zf = null;
        InputStream zi = null;

        try
        {
            zf = new ZipFile(CoreKokkiePlugin.location);

            // 差し替え後のファイルです。coremodのjar内のパスを指定します。
			
            ZipEntry ze = zf.getEntry(filename + ".class");
			if(ze==null) {
				return bytes;
			}

            if (ze != null)
            {
                zi = zf.getInputStream(ze);
                int len = (int) ze.getSize();
                bytes = new byte[len];

                // ヒープサイズを超えないように、ストリームからファイルを1024ずつ読み込んで bytes に格納する
                int MAX_READ = 1024;
                int readed = 0, readsize, ret;
                while(readed < len) {
                    readsize = MAX_READ;
                    if (len - readed < MAX_READ ) {
                        readsize = len - readed;
                    }
                    ret = zi.read(bytes, readed, readsize);
                    if (ret == -1) break;
                    readed += ret;
                }
            }

            return bytes;
        }
        finally
        {
            if (zi != null)
            {
                zi.close();
            }

            if (zf != null)
            {
                zf.close();
            }
        }
    }

	public byte[] readAll(InputStream inputStream) throws IOException {
    ByteArrayOutputStream bout = new ByteArrayOutputStream();
    byte [] buffer = new byte[1024];
    while(true) {
        int len = inputStream.read(buffer);
        if(len < 0) {
            break;
        }
        bout.write(buffer, 0, len);
    }
    return bout.toByteArray();}
}