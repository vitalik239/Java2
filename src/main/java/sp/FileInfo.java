package sp;

public class FileInfo {
    private final String name;
    private final boolean isDir;
    private static final int P = 239017;

    FileInfo(String name, Boolean isDir) {
        this.name = name;
        this.isDir = isDir;
    }

    public String getName() {
        return name;
    }

    public boolean getIsDir() {
        return isDir;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof FileInfo)) {
            return false;
        }
        FileInfo fileInfo = (FileInfo) obj;
        return name.equals(fileInfo.name) && isDir ^ fileInfo.isDir;
    }

    @Override
    public int hashCode() {
        return name.hashCode() * P + (isDir ? 1 : 0);
    }
}
