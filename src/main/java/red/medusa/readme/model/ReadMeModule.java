package red.medusa.readme.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class ReadMeModule implements Comparable<ReadMeModule> {

    private ReadMeModuleList readMeModules = ReadMeModuleList.getInstance();

    private List<Line> lines = new LinkedList<>();
    private int order = Integer.MAX_VALUE;
    private String moduleName = "";

    public ReadMeModule(String moduleName) {
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

    public int moduleSize() {
        return readMeModules.size();
    }

    @Override
    public String toString() {
        return moduleName;
    }

    public int getOrder() {
        if (!this.lines.isEmpty()) {
            Line moduleLine = this.lines.get(0);
            if (moduleLine.isModule() && moduleLine.getAnnotation() != null) {
                return moduleLine.getAnnotation().getModuleOrder();
            }
        }
        return order;
    }

    public void setOrder(int order) {
        if (order > 0 && order <= this.moduleSize())
            this.order = order;
    }

    public boolean isDefaultModule() {
        return this.moduleName.endsWith(MarkDownTag.SEPARATOR);
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


    @Override
    public int compareTo(ReadMeModule o) {
        return this.getOrder() - o.getOrder();
    }
}