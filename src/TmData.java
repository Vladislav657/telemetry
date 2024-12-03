import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

public class TmData {
    String num;
    String time;
    String size;
    int type;
    int length;
    byte[] data;

    TmData(byte[] buff, FileInputStream in){
        try {
            this.num = getNum(buff);
            this.time = getTime(buff, in);
            this.size = getSize(buff, in);
            this.type = getType(buff, in);
            if (this.type == 3)
                this.length = getLength(buff, in);
            else
                this.length = 8;
            this.data = new byte[1024];
            this.getData(in);
        }
        catch (Exception e) {
            System.out.println("Exception " + e.toString());
        }
    }

    String getNum(byte[] buff) {
        return String.format("%02x%02x", buff[0], buff[1]);
    }

    String getTime(byte[] buff, FileInputStream in) throws IOException {
        int size = in.read(buff, 0, 4);
        return String.format("%02x%02x%02x%02x", buff[0], buff[1], buff[2], buff[3]);
    }

    String getSize(byte[] buff, FileInputStream in) throws IOException {
        int size = in.read(buff, 0, 1);
        return String.format("%02x", buff[0]);
    }

    int getType(byte[] buff, FileInputStream in) throws IOException {
        int size = in.read(buff, 0, 1);
        return buff[0];
    }

    int getLength(byte[] buff, FileInputStream in) throws IOException {
        int size = in.read(buff, 0, 4);
        int len = buff[3];
        if (len < 0)
            len = 128 * 2 + len;
        return len;
    }

    void getData(FileInputStream in) throws IOException {
        int size = in.read(this.data, 0, this.length);
    }

    void putToFile(FileWriter out){
        try {
            out.write(this.num);
            out.write("  ");
            out.write(this.time);
            out.write("  ");
            out.write(this.size);
            out.write("  ");
            out.write(Integer.toString(this.type));
            out.write("  ");
            out.write(Integer.toString(this.length));
            out.write("  ");
            for (int i = 0; i < this.length; ++i)
                out.write(String.format("%02x", this.data[i]) + " ");
            out.write("\n");
        }
        catch (Exception e){
            System.out.println("Exception " + e.toString());
        }
    }

    boolean useful(){
        return !this.num.equals("ffff");
    }
}
