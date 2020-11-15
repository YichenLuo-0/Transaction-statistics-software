package instrument;

import java.io.File;

public final class FindUnfinished extends TraverseDatabase {
    public String total = "";

    @Override
    protected void Options(File path) {
        String pathname = path.getPath().replace("\\", "/") + "/";
        File buy = new File(pathname + "buy");
        File sell = new File(pathname + "sell");
        if (!buy.exists()) {
            Instrument.deleteFileOrDirectory(String.valueOf(path));
            return;
        }
        String buy1 = Instrument.getFileData(buy);
        if (buy1.split("\n").length != 7) {
            Instrument.deleteFileOrDirectory(String.valueOf(path));
            return;
        }
        if (!sell.exists()) {
            String buyInf = buy1 + "\n" + pathname + "I";
            total = total + buyInf;
        }
    }
}
