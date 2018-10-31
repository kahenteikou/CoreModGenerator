package kokkiecoremods.asm;
import java.util.Arrays;
import java.util.List;
import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import javax.annotation.Nullable;
import java.security.cert.Certificate ;
import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.versioning.ArtifactVersion;

// 必ずしも DummyModContainer を継承している必要はありません。
// net.minecraftforge.fml.common.ModContainer さえ実装していれば、どんなクラスでも構いません。

public class kokkieModContaine extends DummyModContainer
{
	private Certificate certificate;
    public kokkieModContaine()
    {
        super(new ModMetadata());
		
        // 他のModと区別するための一意なIDやmodの名前など、MODのメタデータを設定します。
        ModMetadata meta = getMetadata();
		byte[] buffQ=null;
		String nameD=null;
		String IDD=null;
		String verD=null;
		try{
			
		
		byte[] Idhoge=namefileread(buffQ,"IDDATA.txt");
		byte[] NameHoge=namefileread(buffQ,"NAMEDATA.txt");
		byte[] VerDD=namefileread(buffQ,"verData.txt");
		nameD=new String(NameHoge);
		IDD=new String(Idhoge);
		verD=new String(VerDD);
		}catch(Exception e){
			
		}
		
        meta.modId       = IDD;
        meta.name        = nameD;
        meta.version     = verD;
        meta.authorList  = Arrays.asList("Author");
        meta.description = "";
        meta.url         = "";
        meta.credits     = "";
        this.setEnabledState(true);
		Certificate[] certificates = getClass().getProtectionDomain().getCodeSource().getCertificates();
		certificate = certificates != null ? certificates[0] : null;
    }
    @Override
    public boolean registerBus(EventBus bus, LoadController controller)
    {
	bus.register(this);
	return true;
    }
	@Override
	@Nullable
	public Certificate getSigningCertificate() {
		// certificateを返す
		return certificate;
	}
	private byte[] namefileread(byte[] bytes,String filename) throws IOException
    {
        ZipFile zf = null;
        InputStream zi = null;

        try
        {
            zf = new ZipFile(CoreKokkiePlugin.location);

            // 差し替え後のファイルです。coremodのjar内のパスを指定します。
            ZipEntry ze = zf.getEntry(filename);

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

}