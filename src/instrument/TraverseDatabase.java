package instrument;

import java.io.File;
import java.util.Objects;

class TraverseDatabase {
    public void traverse() {
        Instrument.newConfigurationFile();
        File machine = new File("./database/machine");
        File[] list = machine.listFiles();
        for (File file : Objects.requireNonNull(list)) {
            if (!file.isFile()) {
                this.Options(file);
            }
        }
        File none = new File("./database/none");
        File[] none1 = none.listFiles();
        for (File file : Objects.requireNonNull(none1)) {
            if (!file.isFile()) {
                this.Options(file);
            }
        }
        File human = new File("./database/human");
        File[] human1 = human.listFiles();
        for (File file : Objects.requireNonNull(human1)) {
            if (!file.isFile()) {
                File[] category1 = file.listFiles();
                for (File value : Objects.requireNonNull(category1)) {
                    if (!value.isFile()) {
                        File[] category2 = value.listFiles();
                        for (File item : Objects.requireNonNull(category2)) {
                            if (!item.isFile()) {
                                this.Options(item);
                            }
                        }
                    }
                }
            }
        }
    }

    protected void Options(File path) {
    }
}

