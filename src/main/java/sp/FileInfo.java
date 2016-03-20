package sp;

public class FileInfo {
    private String name;
    private Boolean isDir;
    private static final int P = 239017;

    FileInfo(String name, Boolean isDir) {
        this.name = name;
        this.isDir = isDir;
    }

    public String getName() {
        return name;
    }

    public Boolean getIsDir() {
        return isDir;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof FileInfo)) {
            return false;
        }
        FileInfo fileInfo = (FileInfo) obj;
        return name.equals(fileInfo.name) && isDir.equals(fileInfo.isDir);
    }

    @Override
    public int hashCode() {
        return name.hashCode() * P + isDir.hashCode();
    }
}
