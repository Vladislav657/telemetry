import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

public class TmData {
    String num;
    long time;
    String size;
    int type;
    int length;
    byte[] data;

    TmData(byte[] buff, FileInputStream in){
        try {
            this.num = this.getNum(buff);
            this.time = this.getTime(buff, in);
            this.size = this.getSize(buff, in);
            this.type = this.getType(buff, in);
            if (this.type == 3)
                this.length = this.getLength(buff, in);
            else if (this.type == 1)
                this.length = 8;
            else {
                this.getLength(buff, in);
                this.length = 4;
            }
            this.data = new byte[1024];
            this.getData(in);
        }
        catch (Exception e) {
            System.out.println("Exception " + e.toString());
        }
    }

    public void putToFile(FileWriter out){
        try {
            out.write(this.num);
            out.write("  ");
            out.write(this.getFormattedTime(this.time));
            out.write("  ");
            out.write(this.size);
            out.write("  ");
            out.write(Integer.toString(this.type));
            out.write("  ");
            if (this.type == 3)
                this.putTypeThree(out);
            else
                this.putOtherType(out);
            out.write("\n");
        }
        catch (Exception e){
            System.out.println("Exception " + e.toString());
        }
    }

    public boolean useful(){
        return !this.num.equals("ffff");
    }

    private String getNum(byte[] buff) {
        return String.format("%02x%02x", buff[0], buff[1]);
    }

    private long getTime(byte[] buff, FileInputStream in) throws IOException {
        int size = in.read(buff, 0, 4);
        return buff[0] * 256 * 256 * 256 + buff[1] * 256 * 256 + buff[2] * 256 + buff[3];
    }

    private String getSize(byte[] buff, FileInputStream in) throws IOException {
        int size = in.read(buff, 0, 1);
        return String.format("%02x", buff[0]);
    }

    private int getType(byte[] buff, FileInputStream in) throws IOException {
        int size = in.read(buff, 0, 1);
        return buff[0];
    }

    private int getLength(byte[] buff, FileInputStream in) throws IOException {
        int size = in.read(buff, 0, 4);
        int len = buff[3];
        if (len < 0)
            len = 128 * 2 + len;
        return len;
    }

    private void getData(FileInputStream in) throws IOException {
        int size = in.read(this.data, 0, this.length);
    }

    private String getFormattedTime(long time){
        long hours = time / 1000 / 60 / 60;
        long minutes = time / 1000 / 60 - hours * 60;
        long seconds = time / 1000 - hours * 60 * 60 - minutes * 60;
        long milliseconds = time - hours * 60 * 60 * 1000 - minutes * 60 * 1000 - seconds * 1000;
        return String.format("%02d:%02d:%02d,%03d", hours, minutes, seconds, milliseconds);
    }

    private void putTypeThree(FileWriter out) throws IOException {
        out.write(Integer.toString(this.length));
        out.write("  ");
        for (int i = 0; i < this.length; ++i)
            out.write(String.format("%02x", this.data[i]) + " ");
    }

    private void putOtherType(FileWriter out) throws IOException {
        long data = 0;
        for (int i = 0; i < this.length; ++i)
            data += (long) (this.data[this.length - 1 - i] * Math.pow(256, this.length - 1 - i));

        if (this.type == 2) {
            String binary = Long.toBinaryString(data);
            out.write(Integer.toString(binary.length()));
            out.write("  ");
            out.write(binary);
        }
        else
            out.write(Long.toString(data));
    }
}
