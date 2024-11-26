import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;

public class HexPrint {

    public static void main(String[] args) {
        byte[] buff = new byte[16];
        int count = 0, size;
        int useless = 0;
        try {
            File f = new File("190829_v29854.KNP");
            FileInputStream f_input= new FileInputStream(f);
            FileWriter f_output= new FileWriter("KNP.rez");
            while((size=f_input.read(buff,0,16)) > 0) {
                f_output.write(String.format("%08x", count) + "   ");
                if (String.format("%02x%02x", buff[0], buff[1]).equals("ffff"))
                    useless++;
                for(int i=0; i < size; i++){
                    f_output.write(String.format("%02x", buff[i]) + " ");
                    if((i+1)%8 == 0)
                        f_output.write("   ");
                }
                f_output.write("\n");
                count+=size;
            }
            System.out.println(useless);
            f_input.close();
            f_output.close();
        }
        catch(Exception e) {
            System.out.println("Exception " + e.toString());
        }
    }
}
