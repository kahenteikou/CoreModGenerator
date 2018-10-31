using Microsoft.Win32;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;
using System.IO;
using System.IO.Compression;

namespace Coremodder1
{
    /// <summary>
    /// MainWindow.xaml の相互作用ロジック
    /// </summary>
    public partial class MainWindow : Window
    {
        public MainWindow()
        {
            InitializeComponent();
        }
        public string EXEDirpath;
        private void TextBox_TextChanged(object sender, TextChangedEventArgs e)
        {

        }

        private void StartButton_Click(object sender, RoutedEventArgs e)
        {
            //ModIDが空の場合
            if (ModIDBox.Text == "")
            {
                MessageBox.Show("ModIDを入れてね!", "エラー", MessageBoxButton.OK, MessageBoxImage.Error);
                return;

            }
            String ModIDsDest = ModIDBox.Text;
            //ModIDが大文字だった時のために変換
            char[] cs = ModIDsDest.ToCharArray();
            for (int i = 0; i < cs.Length; i++)
            {
                //大文字か調べる
                if (char.IsUpper(cs[i]))
                {
                    //小文字にする
                    cs[i] = char.ToLower(cs[i]);
                }
            }
            string ModID = new string(cs);//Stringに戻す
            if (ModNameBox.Text == "")//ModNameが空だったら
            {
                MessageBox.Show("ModNameを入れてね!", "エラー", MessageBoxButton.OK, MessageBoxImage.Error);
                return;
            }
            string ModName = ModNameBox.Text;
            if (VersionBox.Text == "")//versionが空だったら
            {
                MessageBox.Show("バージョンを入れてね!", "エラー", MessageBoxButton.OK, MessageBoxImage.Error);
            }
            string ModVersion = VersionBox.Text;
            if (INpathbox.Text =="")//フォルダが指定されていないとき
            {
                MessageBox.Show("Classファイルのフォルダを指定してください!", "エラー", MessageBoxButton.OK, MessageBoxImage.Error);
                return;
            }
            string ClassInpath = INpathbox.Text;
            if (OUTFilepathBox.Text == "")//Jarファイルが指定されていなかったら
            {
                MessageBox.Show("Jarファイルを指定してください", "エラー", MessageBoxButton.OK, MessageBoxImage.Error);
                return;
            }
            string JarOutpath = OUTFilepathBox.Text;
            /*
            System.IO.DirectoryInfo di = new System.IO.DirectoryInfo(EXEDirpath + "\\temp");

            if (Directory.Exists(EXEDirpath+ "\\temp"))//tempが存在する場合
            {
                di.Delete(true);
            }
            di.Create();
            */
            //Jarコピー
            System.IO.File.Copy(EXEDirpath + "\\modgentemp.jar", JarOutpath, true);
            //読み取りと書き込みができるようにして、ZIP書庫を開く
            using (ZipArchive a = ZipFile.Open(JarOutpath, ZipArchiveMode.Update))
            {
                //""以下の"*"ファイルをすべて取得する

                System.IO.DirectoryInfo di = new System.IO.DirectoryInfo(ClassInpath);
                System.IO.FileInfo[] files =
                    di.GetFiles("*");

                //処理(Classファイル、mcmod.infoなどを追加!!
                foreach (System.IO.FileInfo f in files)
                {
                    //ファイル「f.fullname」を「f.fullname」としてZIPに追加する
                    ZipArchiveEntry ze = a.CreateEntryFromFile(f.FullName, System.IO.Path.GetFileName(f.FullName));
                }
                //Entry add
                ZipArchiveEntry IDen= a.CreateEntry("IDDATA.txt");
                //書き込むために、開く
                using (StreamWriter sw = new StreamWriter(IDen.Open(),
                    System.Text.Encoding.ASCII))
                {
                    //書き込む
                    sw.Write(ModID);

                }
                ZipArchiveEntry NameEN = a.CreateEntry("NAMEDATA.txt");
                using (StreamWriter sw = new StreamWriter(NameEN.Open(),
                System.Text.Encoding.UTF8))
                {
                    //書き込む
                    sw.Write(ModName);

                }
                ZipArchiveEntry VerEN = a.CreateEntry("verData.txt");
                using (StreamWriter sw = new StreamWriter(VerEN.Open(),
                System.Text.Encoding.UTF8))
                {
                    //書き込む
                    sw.Write(ModVersion);

                }


            }
            MessageBox.Show("成功!", "成功", MessageBoxButton.OK, MessageBoxImage.Information);




        }

        private void INButton_Click(object sender, RoutedEventArgs e)
        {
            //FolderBrowserDialogクラスのインスタンスを作成
            System.Windows.Forms.FolderBrowserDialog fbd = new System.Windows.Forms.FolderBrowserDialog();

            //上部に表示する説明テキストを指定する
            fbd.Description = "Classファイルの入ったフォルダを指定してください。";
            //ルートフォルダを指定する
            //デフォルトでDesktop
            fbd.RootFolder = Environment.SpecialFolder.Desktop;
            //ユーザーが新しいフォルダを作成できるようにする
            //デフォルトでTrue
            fbd.ShowNewFolderButton = true;

            //ダイアログを表示する
            if (fbd.ShowDialog() == System.Windows.Forms.DialogResult.OK)
            {
                INpathbox.Text = fbd.SelectedPath;
            }
        }

        private void OutButton_Click(object sender, RoutedEventArgs e)
        {
            //SaveFileDialogクラスのインスタンスを作成
            Microsoft.Win32.SaveFileDialog sfd = new SaveFileDialog();

            //はじめのファイル名を指定する
            //はじめに「ファイル名」で表示される文字列を指定する
            sfd.FileName = "OutMod.jar";
            //[ファイルの種類]に表示される選択肢を指定する
            //指定しない（空の文字列）の時は、現在のディレクトリが表示される
            sfd.Filter = "Jarファイル(*.jar)|*.jar|すべてのファイル(*.*)|*.*";
            //[ファイルの種類]ではじめに選択されるものを指定する
            sfd.FilterIndex = 1;
            //タイトルを設定する
            sfd.Title = "出力先のファイルを選択してください";
            //ダイアログボックスを閉じる前に現在のディレクトリを復元するようにする
            sfd.RestoreDirectory = true;
            //既に存在するファイル名を指定したとき警告する
            //デフォルトでTrueなので指定する必要はない
            sfd.OverwritePrompt = true;
            //存在しないパスが指定されたとき警告を表示する
            //デフォルトでTrueなので指定する必要はない
            sfd.CheckPathExists = true;

            //ダイアログを表示する
            if (sfd.ShowDialog() == true)
            {
                //OKボタンがクリックされたとき、選択されたファイル名を表示する
                OUTFilepathBox.Text = sfd.FileName;
            }
        }

        private void MainWindow_Loaded(object sender, RoutedEventArgs e)
        {
            EXEDirpath= System.AppDomain.CurrentDomain.BaseDirectory.TrimEnd('\\');
        }
    }
}
