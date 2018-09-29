package ocr.avaboy.com;

/**
 * Created by ILSP on 3/14/2017.
 */

public class Company {

    private String name,desc,imieStart,imieEnd;
    private int imieLength;
    private byte[] image;

    public Company(){}

    public Company(String name, String desc, String imieStart, String imieEnd, int imieLength, byte[] image) {
        this.name = name;
        this.desc = desc;
        this.imieStart = imieStart;
        this.imieEnd = imieEnd;
        this.imieLength = imieLength;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImieStart() {
        return imieStart;
    }

    public void setImieStart(String imieStart) {
        this.imieStart = imieStart;
    }

    public String getImieEnd() {
        return imieEnd;
    }

    public void setImieEnd(String imieEnd) {
        this.imieEnd = imieEnd;
    }

    public int getImieLength() {
        return imieLength;
    }

    public void setImieLength(int imieLength) {
        this.imieLength = imieLength;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
