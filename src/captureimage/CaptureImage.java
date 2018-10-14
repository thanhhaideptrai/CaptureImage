package captureimage;



import java.io.File;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

        
public class CaptureImage {
    public CaptureImage() throws IOException {
        this.writer = new FileOutputStream("ForensicHardDisk.dat");
    }
    public static ArrayList<String> path = new ArrayList<>();
    FileOutputStream writer;
    String newline = System.getProperty("line.separator");
    byte [] data2 = newline.getBytes();
    
    public void fetchChild(File file) throws FileNotFoundException, IOException
    {
        int i;
        if(file.isDirectory())// kiểm tra thư mục
        {
            String thumuc = file.getName()+newline;
            byte[] thumucname=thumuc.getBytes();
            writer.write(thumucname,0,thumucname.length);
            File[] children = file.listFiles();// nếu là thư mục tạo ra ds các file 
            if (children == null) return;
            
            for (File child : children) // backtracking
            {
                if(child.isDirectory())
                this.fetchChild(child);
            }
            
            for (File child : children) // duyệt ds vừa tạo
            {
                if(child.isFile()) //kiểm tra là file hay thự mục
                {
                    System.out.println(child.getAbsolutePath());
                    FileInputStream fi = new FileInputStream(child.getAbsolutePath());     
                    byte[] tree  = child.getAbsolutePath().getBytes();
                    writer.write(tree, 0, tree.length);
                    byte[] filename = child.getName().getBytes();
                    writer.write(filename, 0, filename.length);
                    do {
                        i = fi.read();
                        if (i != -1) {
                            writer.write(i);
                        }
                    } while (i != -1);
                    writer.write(data2, 0, data2.length);
                    fi.close();
                }
            }
            
        }
    }
    public static void main(String[] args) throws IOException {
        CaptureImage Forensic = new CaptureImage();
        boolean flag = true;
        int chon;
        String check, kiemtra;
        while (flag)
        {
            path.clear();
            Scanner sc = new Scanner(System.in); 
            System.out.println("Menu:\n1. Capture tất cả ổ đĩa.\n2. Nhập đường dẫn thư mục cần capture .\n3. Thoát.");
            System.out.print("Nhập lựa chọn: ");
            chon = sc.nextInt();
            sc.nextLine();
            if (chon!=1 && chon !=2) break;
            switch(chon)
            {
                case 1:
                    
                    File[] roots = File.listRoots();// tạo ds tất cả ổ đĩa
                    for (File root : roots)
                    {
                        File child = new File(root.getAbsolutePath());
                        Forensic.fetchChild(child);
                    }
                    break;
                case 2:    
                    
                    boolean a;
                    File f;
                    String yesorno = null;
                    do
                    {
                        System.out.print("Nhập đường dẫn thư mục: ");
                        check = sc.nextLine();
                        f = new File(check);
                        a = f.exists();
                        if(!a) 
                        {
                            System.out.println("Thư mục không tồn tại");
                            System.out.println("Bạn có muốn nhập lại không?(y)");
                            yesorno = sc.nextLine();
                            
                        }
                        if ("y".equalsIgnoreCase(yesorno)) continue; 
                        else break;
                    } while(!a);
                    Forensic.fetchChild(f);
                    break;          
            }
        }
    }
}
