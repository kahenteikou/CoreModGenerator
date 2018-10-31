package kokkiecoremods.asm;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import java.io.File;
import java.io.IOException;

import java.util.Map;

/**
 * クラス書き換え用のコアクラス。
 * Modアノテーションを付けているクラスとは別に必要なことに注意。
 */
@IFMLLoadingPlugin.TransformerExclusions({"kokkiecoremods.asm"})
public class CoreKokkiePlugin implements IFMLLoadingPlugin {
	//static File location;
    //書き換え機能を実装したクラス一覧を渡す関数。書き方はパッケージ名+クラス名。
    @Override
    public String[] getASMTransformerClass() {
        return new String[]{"kokkiecoremods.asm.CoreModExampleTransformer"};
    }
    static File location;
    //あとは今回は使わない為適当に。
@Override
    public String getSetupClass() {
        //return "kokkiecoremods.asm.DepLoader";
		return null;
    }


    @Override
    public String getAccessTransformerClass() {
        return null;
    }

    @Override
    public String getModContainerClass()
    {
        return "kokkiecoremods.asm.kokkieModContaine";
    }
	@Override
    public void injectData(Map<String, Object> data)
    {
        if (data.containsKey("coremodLocation"))
        {
            location = (File) data.get("coremodLocation");
        }
    }

}
