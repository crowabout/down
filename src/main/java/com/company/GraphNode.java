package com.company;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * Created by pc on 2017/7/5.
 */
@Entity
public class GraphNode {
    @Id
    @GeneratedValue
    private int id;
    /**
     * node Name
     */
    private String nodeName;
    /**
     * url
     */
    @Column(columnDefinition = "TEXT DEFAULT NULL")
    private String relUrl;
    /**
     * level
     */
    private int level;

    /**
     * branch node or leaf node?
     */
    private boolean isLeafNode;

    private String fileSize;

    /**
     * upload time
     */
    private String uploadTime;
    /**
     * type of file
     */
    private String fileType;
    /**
     * dir or file
     */
    private String dirOrFile;

    public String getCurPageIndex() {
        return curPageIndex;
    }

    public void setCurPageIndex(String curPageIndex) {
        this.curPageIndex = curPageIndex;
    }

    /**
     *the directory index about current page
     */
    private String curPageIndex;

    public GraphNode() {
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getRelUrl() {
        return relUrl;
    }

    public void setRelUrl(String relUrl) {
        this.relUrl = relUrl;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setLeafNode(boolean leafNode) {
        isLeafNode = leafNode;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(String uploadTime) {
        this.uploadTime = uploadTime;
    }

    /**
     * set file type
     *
     * @return
     */
    public String getFileType() {
        if (isLeafNode()) {
            if (nodeName.contains(".")) {
                int index = nodeName.lastIndexOf(".");
                fileType = nodeName.substring(index + 1, nodeName.length());
            }
        }
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getDirOrFile() {
        return dirOrFile;
    }

    public void setDirOrFile(String dirOrFile) {
        this.dirOrFile = dirOrFile;
    }


    public GraphNode(String nodeName, String relUrl) {
        this.nodeName = nodeName;
        this.relUrl = relUrl;
    }

    public GraphNode(String nodeName, String relUrl, String fileSize) {
        this(nodeName, relUrl);
        this.fileSize = fileSize;
    }

    /**
     * convert relative url to absolute url;
     *
     * @return
     */
    public URL relUrl2abs() {
        if (relUrl == null || relUrl.equals("")) {
            throw new IllegalArgumentException("!!!relUrl is null or empty!!!");
        }
        URL url = null;
        try {
            url = new URL(Iidc.BASE + "/" + relUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * leafNode or branchNode
     *
     * @return
     */
    public boolean isLeafNode() {

        //suffix end of the fileName
//        boolean bNodeName = TextUtils.suffixIn(nodeName);
        //and the attribute of size
        Pattern p = Pattern.compile("\\d*.{0,1}\\d*\\s*[GgMmKkTt]{0,1}[Bb]");
        Matcher m = p.matcher(fileSize.trim());
        boolean bfileSize = m.matches();

        //there is no suffix about the file Name but have size
        if(bfileSize){
            return bfileSize;
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format("[%s %s %s %s %s %s]", nodeName, fileSize, relUrl, isLeafNode(), uploadTime, curPageIndex);
    }

}
