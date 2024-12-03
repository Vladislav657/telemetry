import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;

public class Main {
    public static void main(String[] args) {
        byte[] buff = new byte[8];
        int count = 0;
        TmData current;

        try {
            File f = new File("190829_v29854.KNP");
            FileInputStream f_input= new FileInputStream(f);
            FileWriter f_output= new FileWriter("useful.rez");

            while(f_input.read(buff,0,2) > 0) {
                current = new TmData(buff, f_input);
                if (current.useful()){
                    current.putToFile(f_output);
                    count++;
                }
            }

            System.out.println(count);
            f_input.close();
            f_output.close();
        }

        catch(Exception e) {
            System.out.println("Exception " + e.toString());
        }
    }
}