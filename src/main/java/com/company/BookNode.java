package com.company;

import com.sun.javafx.beans.IDProperty;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by pc on 2017/7/1.
 */
@Entity
public class BookNode {
    @Id
    @GeneratedValue
    private int id;
    @Audited
    private String bookName;
    @Audited
    private String fileSize;
    @Audited
    private String uploadTime;
    @Audited
    private String relaUrl;
    /**
     * 文件类型
     */
    @Audited
    private String fileType;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(String uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getRelaUrl() {
        return relaUrl;
    }

    public void setRelaUrl(String relaUrl) {
        this.relaUrl = relaUrl;
    }
    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    @Override
    public int hashCode() {
        int result;
        result = id;
        result = 31 * result + (bookName != null ? bookName.hashCode() : 0);
        result = 31 * result + (fileSize != null ? fileSize.hashCode() : 0);
        result = 31 * result + (relaUrl != null ? relaUrl.hashCode() : 0);
        return result;
    }
}
