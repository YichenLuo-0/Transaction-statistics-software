package instrument;

import java.io.File;

public final class DeleteNotes extends TraverseDatabase {
    String noteName;

    public DeleteNotes(String noteName) {
        this.noteName = noteName;
    }

    @Override
    protected void Options(File path) {
        String pathname = path.getPath().replace("\\", "/") + "/";
        File file = new File(pathname + this.noteName);
        if (file.exists()) {
            file.delete();
        }
    }
}
