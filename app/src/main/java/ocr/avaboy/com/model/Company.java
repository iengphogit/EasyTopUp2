package ocr.avaboy.com.model;

import java.io.Serializable;

/**
 * Created by ILSP on 3/14/2017.
 */

public class Company implements Serializable {

    private int id;
    private String name,desc,imieStart,imieEnd;
    private int imieLength;
    private byte[] image;

    public Company(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
