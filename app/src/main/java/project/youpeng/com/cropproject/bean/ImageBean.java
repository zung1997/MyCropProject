package project.youpeng.com.cropproject.bean;

public class ImageBean {
    private String path;
    private String name;
    private String parentPath;
    private String parentName;
    private int parentId;

    public ImageBean(String path, String name, String parentPath, String parentName,int parentId) {
        this.path = path;
        this.name = name;
        this.parentPath = parentPath;
        this.parentName = parentName;
        this.parentId = parentId;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentPath() {
        return parentPath;
    }

    public void setParentPath(String parentPath) {
        this.parentPath = parentPath;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }
}
