package instrument;

import java.io.File;

public final class DealNumber extends TraverseDatabase {
    public int dealNumber;

    @Override
    protected void Options(File path) {
        ++this.dealNumber;
    }
}
