package red.medusa.readme.model;

public enum NewLineOption {

    NOTING, REPLACE, INSERT,NULL;

    private static int index;
    private static boolean stop;

    NewLineOption() {
    }

    public int getIndex() {
        return index;
    }

    public void plus() {
        if (!stop) {
            index++;
        }
    }

    public void stopPlus() {
        stop = true;
    }

    public void continues() {
        stop = false;
    }
}