package instrument;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public final class ChangeStockID extends TraverseDatabase {
    String oldID, newID;

    public ChangeStockID(String oldID, String newID) {
        this.oldID = oldID;
        this.newID = newID;
    }

    @Override
    protected void Options(File path) {
        String pathname = path.getPath().replace("\\", "/") + "/";
        File file = new File(pathname + "buy");
        if (file.exists()) {
            String[] inf = Instrument.getFileData(file).split("\n");
            if (inf.length == 7 && inf[0].equals(oldID)) {
                inf[0] = newID;
                try {
                    FileWriter buyInf = new FileWriter(file);
                    buyInf.write(inf[0] + "\n" + inf[1] + "\n" + inf[2] + "\n" + inf[3] + "\n" + inf[4] + "\n" + inf[5] + "\n" + inf[6]);
                    buyInf.close();
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, "写入文件失败，请重试！", "错误", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
}
