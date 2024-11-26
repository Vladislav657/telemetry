import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        byte[] buff = new byte[8];
        int count = 0, usefulCount = 0, size, length;
        boolean useful = true;
        try {
            File f = new File("190829_v29854.KNP");
            FileInputStream f_input= new FileInputStream(f);
            FileWriter f_output= new FileWriter("KNP_useful.rez");

            while((size=f_input.read(buff,0,8)) > 0) {
                if (String.format("%02x%02x", buff[0], buff[1]).equals("ffff"))
                    useful = false;
                else {
                    useful = true;
                    usefulCount++;
                    f_output.write(String.format("%08x", count) + "   ");
                }

                printBytes(size, f_output, buff, useful);
                f_output.write("   ");
                count += size;

                if (buff[7] == 3){
                    size = f_input.read(buff,0,4);
                    length = buff[3];
                    if (length < 0)
                        length = 128 * 2 + length;
                    printBytes(size, f_output, buff, useful);
                    count += size;
//                    System.out.println(String.format("%08x", count) + ": " + length);

                    while (length - 8 > 0){
                        size = f_input.read(buff,0,8);
                        printBytes(size, f_output, buff, useful);
                        length -= size;
                        count += size;
                    }

                    size = f_input.read(buff,0, length);
                    printBytes(size, f_output, buff, useful);
                    count += size;
                }
                else{
                    size = f_input.read(buff,0,8);
                    printBytes(size, f_output, buff, useful);
                    count += size;
                }
                if (useful)
                    f_output.write("\n");
            }

            System.out.println(usefulCount);
            f_input.close();
            f_output.close();
        }

        catch(Exception e) {
            System.out.println("Exception " + e.toString());
        }
    }

    public static void printBytes(int size, FileWriter out, byte[] buff, boolean cond) throws IOException {
        if (!cond)
            return;
        for (int i = 0; i < size; i++) {
            out.write(String.format("%02x", buff[i]) + " ");
        }
    }
}