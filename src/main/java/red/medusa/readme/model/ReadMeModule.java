package red.medusa.readme.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class ReadMeModule {

    private ReadMeModuleList readMeModules = ReadMeModuleList.getInstance();

    private List<Line> lines = new LinkedList<>();

    private String moduleName = "";

    public ReadMeModule(String moduleName) {
        this.moduleName = moduleName;
        readMeModules.addModule(this);
    }

    public ReadMeModule(String moduleName,boolean option) {
        this.moduleName = moduleName;
    }


    public String getModuleName() {
        return moduleName;
    }

    public ReadMeModule setModuleName(String moduleName) {
        this.moduleName = moduleName;
        return this;
    }

    public List<Line> getLines() {
        return lines;
    }

    public ReadMeModule addLine(Line line) {
        this.lines.add(line);
        return this;
    }

    public ReadMeModule dettach() {
        readMeModules.removeModule(this);
        return this;
    }

    @Override
    public String toString() {
        return moduleName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReadMeModule)) return false;
        ReadMeModule that = (ReadMeModule) o;
        return Objects.equals(moduleName, that.moduleName);
    }

    @Override
    public int hashCode() {
        return moduleName.hashCode();
    }


}