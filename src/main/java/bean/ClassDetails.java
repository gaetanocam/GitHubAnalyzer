package bean;

/**
 * Created by Placido Russo on 17/05/2016.
 */
public class ClassDetails {
    private String path;
    private String name;
    private int startIndex;
    private int endIndex;
    private boolean modified = false;

    public ClassDetails() {}

    public ClassDetails(ClassDetails obj) {
        this.path = obj.getPath();
        this.name = obj.getName();
        this.startIndex = obj.getStartIndex();
        this.endIndex = obj.getEndIndex();
        this.modified = obj.isModified();
    }

    public boolean isModified() {
        return modified;
    }

    public void setModified(boolean modified) {
        this.modified = modified;
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

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public int getEndIndex() {
        return endIndex;
    }

    public void setEndIndex(int endIndex) {
        this.endIndex = endIndex;
    }

    @Override
    public String toString() {
        return (modified ? "SI: "+name : "NO");
    }

    @Override
    public boolean equals(Object obj) {
        ClassDetails classDetails = (ClassDetails) obj;
        return (this.name == classDetails.getName() && this.path == classDetails.getPath());
    }
}
