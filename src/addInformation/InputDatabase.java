package addInformation;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

final class InputDatabase {
    String StockID, BuyingPrice, Number, buyYear, buyMonth, buyDay;
    String SellingPrice, Poundage, sellYear, sellMonth, sellDay;
    String picNote1, picNote2;
    String category1, category2;
    String pathName;
    boolean BuyingMode, SellingMode;

    protected void getStockID(String StockID) {
        this.StockID = StockID;
    }

    protected boolean getBuyingPrice(String BuyingPrice) {
        String isInteger = "\\d+(\\.\\d{1,2})?";
        if (BuyingPrice.matches(isInteger)) {
            this.BuyingPrice = BuyingPrice;
            return true;
        } else {
            return false;
        }
    }

    protected void getBuyingDay(String year, String month, String day) {
        this.buyYear = year;
        if (month.length() == 1) {
            this.buyMonth = "0" + month;
        } else {
            this.buyMonth = month;
        }
        if (day.length() == 1) {
            this.buyDay = "0" + day;
        } else {
            this.buyDay = day;
        }
    }

    protected void getBuyingMode(boolean BuyingMode) {
        this.BuyingMode = BuyingMode;
    }

    protected boolean getBuyingNumber(String Number) {
        if (Number.length() == 0) {
            return false;
        } else {
            this.Number = Number;
            return true;
        }
    }

    protected boolean getSellingPrice(String SellingPrice) {
        String isInteger = "\\d+(\\.\\d{1,2})?";
        if (SellingPrice.matches(isInteger)) {
            this.SellingPrice = SellingPrice;
            return true;
        } else {
            return false;
        }
    }

    protected void getSellingMode(boolean SellingMode) {
        this.SellingMode = SellingMode;
    }

    protected boolean getPoundage(String Poundage) {
        String isInteger = "\\d+(\\.\\d{1,2})?";
        if (Poundage.matches(isInteger)) {
            this.Poundage = Poundage;
            return true;
        } else {
            return false;
        }
    }

    protected boolean getSellingDay(String year, String month, String day) {
        if ((Integer.parseInt(year) < Integer.parseInt(buyYear))
                || (Integer.parseInt(year) == Integer.parseInt(buyYear)
                && Integer.parseInt(month) < Integer.parseInt(buyMonth))
                || (Integer.parseInt(year) == Integer.parseInt(buyYear)
                && Integer.parseInt(month) == Integer.parseInt(buyMonth)
                && Integer.parseInt(day) < Integer.parseInt(buyDay) + 1)) {
            return false;
        } else {
            this.sellYear = year;
            if (month.length() == 1) {
                this.sellMonth = "0" + month;
            } else {
                this.sellMonth = month;
            }
            if (day.length() == 1) {
                this.sellDay = "0" + day;
            } else {
                this.sellDay = day;
            }
            return true;
        }
    }

    protected void getPictureNotes(String notes, int i) {
        if (notes.length() == 0 || notes.equals("在此输入图片备注")) {
            if (i == 1) {
                this.picNote1 = "无图片注释";
            } else {
                this.picNote2 = "无图片注释";
            }
        } else {
            if (i == 1) {
                this.picNote1 = notes;
            } else {
                this.picNote2 = notes;
            }
        }
    }

    protected boolean getCategory(String category1, String category2) {
        if (category1.equals("<请选择一级分类>") || category2.equals("<请选择二级分类>")) {
            return false;
        } else {
            this.category1 = category1;
            this.category2 = category2;
            return true;
        }
    }

    protected void inputBuying() {
        File buy = new File(pathName + "/buy");
        try {
            buy.createNewFile();
            FileWriter buyInf = new FileWriter(buy);
            buyInf.write(StockID + "\n" + BuyingPrice + "\n" + Number + "\n" + buyYear + "\n" + buyMonth + "\n" + buyDay
                    + "\n" + BuyingMode);
            buyInf.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "写入文件失败，请重试！", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    protected void inputSelling() {
        File sell = new File(pathName + "/sell");
        try {
            sell.createNewFile();
            FileWriter sellInf = new FileWriter(sell);
            sellInf.write(SellingPrice + "\n" + Poundage + "\n" + sellYear + "\n" + sellMonth + "\n" + sellDay + "\n"
                    + SellingMode);
            sellInf.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "写入文件失败，请重试！", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    protected void inputNotes() {
        File picture1 = new File("temporary1.jpg");
        File move1 = new File(pathName + "/picture1.jpg");
        if (picture1.exists()) {
            picture1.renameTo(move1);
        } else if (move1.exists()) {
            move1.delete();
        }
        File picture2 = new File("temporary2.jpg");
        File move2 = new File(pathName + "/picture2.jpg");
        if (picture2.exists()) {
            picture2.renameTo(move2);
        } else if (move2.exists()) {
            move2.delete();
        }
        File pictureNote1 = new File(pathName + "/pictureNote1");
        try {
            pictureNote1.createNewFile();
            FileWriter out1 = new FileWriter(pictureNote1);
            out1.write(String.valueOf(this.picNote1));
            out1.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "写入文件失败，请重试！", "错误", JOptionPane.ERROR_MESSAGE);
        }
        File pictureNote2 = new File(pathName + "/pictureNote2");
        try {
            pictureNote2.createNewFile();
            FileWriter out2 = new FileWriter(pictureNote2);
            out2.write(String.valueOf(this.picNote2));
            out2.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "写入文件失败，请重试！", "错误", JOptionPane.ERROR_MESSAGE);
        }
        for (int num = 0; num < AddNotes.notes.length; num++) {
            File Note = new File(pathName + "/" + AddNotes.notes[num]);
            try {
                Note.createNewFile();
                FileWriter out3 = new FileWriter(Note);
                out3.write(AddNotes.jt[num].getText());
                out3.close();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "写入文件失败，请重试！", "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    protected void checkFolder(int i) {
        File file0 = new File("./database");
        if (!file0.exists()) {
            file0.mkdir();
        }
        if (i == 1) {
            File file1 = new File("./database/machine");
            if (!file1.exists()) file1.mkdir();
        } else {
            File file2 = new File("./database/human");
            if (!file2.exists()) file2.mkdir();
            File file3 = new File("./database/human/" + category1);
            if (!file3.exists()) file3.mkdir();
            File file4 = new File("./database/human/" + category1 + "/" + category2);
            if (!file4.exists()) file4.mkdir();
        }
    }

    protected void createFilePath(int robotOrHuman) {
        checkFolder(robotOrHuman);
        String pathname;
        if (robotOrHuman == 1) {
            pathname = "./database/machine/";
        } else {
            pathname = "./database/human/" + category1 + "/" + category2 + "/";
        }
        int num = 0;
        String filename = pathname + buyYear + buyMonth + buyDay + num;
        File file = new File(filename);
        while (true) {
            if (file.exists()) {
                num++;
                filename = pathname + buyYear + buyMonth + buyDay + num;
                file = new File(filename);
            } else {
                file.mkdir();
                break;
            }
        }
        this.pathName = filename;
    }
}