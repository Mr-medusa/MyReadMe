package red.medusa.readme;

public class ReadMePool {
    private static ReadMeModuleList moduleList = new ReadMeModuleList();

    public static ReadMeModuleList getModuleList() {
        return moduleList;
    }
}
