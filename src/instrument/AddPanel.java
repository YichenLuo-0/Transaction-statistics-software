package instrument;

import browseInformation.BrowserData;

import java.io.File;

public final class AddPanel extends TraverseDatabase {
    int load = 0;
    public String[][] dataInformation = new String[BrowserData.dealNumber][18 + BrowserData.noteNumber];

    @Override
    protected void Options(File path) {
        if (readFromDatabase(path, this.load)) {
            ++this.load;
        } else {
            Instrument.deleteFileOrDirectory(String.valueOf(path));
            BrowserData.dealNumber--;
        }
    }

    private boolean readFromDatabase(File path, int load) {
        String pathname = path.getPath().replace("\\", "/") + "/";
        File buy = new File(pathname + "buy");
        if (!buy.exists()) {
            return false;
        }
        String[] buyInf = Instrument.getFileData(buy).split("\n");
        if (buyInf.length != 7) {
            return false;
        }
        File sell = new File(pathname + "sell");
        String[] sellInf;
        if (sell.exists()) {
            if (Instrument.getFileData(sell).split("\n").length != 6) {
                return false;
            }
            sellInf = Instrument.getFileData(sell).split("\n");
            System.arraycopy(sellInf, 0, dataInformation[load], 8, 6);
            dataInformation[load][14] = String.valueOf((Float.parseFloat(sellInf[0]) - Float.parseFloat(buyInf[1]))
                    * Float.parseFloat(buyInf[2]) - Float.parseFloat(sellInf[1]));
            dataInformation[load][15] = String.valueOf(Float.parseFloat(dataInformation[load][14]) / (Float.parseFloat(buyInf[1])
                    * Float.parseFloat(buyInf[2])));
        } else {
            for (int i = 8; i < 16; i++) {
                dataInformation[load][i] = "-";
            }
        }
        dataInformation[load][0] = pathname;
        System.arraycopy(buyInf, 0, dataInformation[load], 1, 7);
        File pictureNote1 = new File(pathname + "pictureNote1");
        if (pictureNote1.exists()) {
            dataInformation[load][16] = Instrument.getFileData(pictureNote1);
        }
        File pictureNote2 = new File(pathname + "pictureNote1");
        if (pictureNote2.exists()) {
            dataInformation[load][17] = Instrument.getFileData(pictureNote2);
        }
        for (int i = 0; i < BrowserData.noteName.length; i++) {
            File Note = new File(pathname + BrowserData.noteName[i]);
            if (Note.exists()) {
                dataInformation[load][18 + i] = Instrument.getFileData(Note);
            }
        }
        return true;
    }
}
