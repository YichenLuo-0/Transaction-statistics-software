package instrument;

import java.io.File;

public final class ChangeNoteName extends TraverseDatabase {
    String oldName, newName;

    public ChangeNoteName(String oldName, String newName) {
        this.oldName = oldName;
        this.newName = newName;
    }

    @Override
    protected void Options(File path) {
        String pathname = path.getPath().replace("\\", "/") + "/";
        File oldf = new File(pathname + this.oldName);
        File newf = new File(pathname + this.newName);
        if (oldf.exists()) {
            oldf.renameTo(newf);
        }
    }
}
