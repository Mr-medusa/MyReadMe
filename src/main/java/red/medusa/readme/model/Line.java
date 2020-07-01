package red.medusa.readme.model;

public class Line {
    private Line  annotation;
    private Line pre;
    private static int num;
    private int order = -1;
    private int selfNum = -1;
    private String moduleName;
    private String methodName;
    private String line = "";
    private String newLine = "";
    private boolean isModule;
    private int moduleLevel = 3;
    private int listLevel;
    private String location;
    private String locationTitle;
    private String moduleMsg;
    private String mark;
    private NewLineOption option = NewLineOption.NOTING;

    public Line() {
    }

    public Line(String line, String newLine) {
        this.line = line;
        this.newLine = newLine;
    }

    public Line(String moduleName, String methodName, String newLine, String location, String locationTitle, String moduleMsg, int moduleLevel, int listLevel) {
        this.setModuleName(moduleName).setMethodName(methodName).setNewLine(newLine).setLocation(location).setLocationTitle(locationTitle)
                .setModuleLevel(moduleLevel).setListLevel(listLevel).setModuleMsg(moduleMsg);
    }

    public Line modifyWithOldLine(Line param) {
        this.setOrder(param.order);
        this.setModuleName(param.getModuleName());
        this.setMethodName(param.getMethodName());
        this.setNewLine(param.getNewLine());
        this.setModule(param.isModule());
        this.setModuleLevel(param.getModuleLevel());
        this.setLocation(param.getLocation());
        this.setLocationTitle(param.getLocationTitle());
        this.setListLevel(param.getListLevel());
        this.setModuleMsg(param.getModuleMsg());

        return this;

    }

    public String getLine() {
        return line;
    }

    public Line setLine(String line) {
        this.line = line;
        return this;
    }

    public String getNewLine() {
        return newLine;
    }

    public Line setNewLine(String newLine) {
        this.newLine = newLine;
        return this;
    }

    public String getModuleName() {
        return moduleName;
    }

    public Line setModuleName(String moduleName) {
        this.moduleName = moduleName;
        return this;
    }

    public boolean isModule() {
        return isModule;
    }

    public Line setModule(boolean module) {
        isModule = module;
        return this;
    }

    public int getModuleLevel() {
        return moduleLevel;
    }

    public Line setModuleLevel(int moduleLevel) {
        this.moduleLevel = moduleLevel;
        return this;
    }

    public String getMethodName() {
        return methodName;
    }

    public Line setMethodName(String methodName) {
        this.methodName = methodName;
        return this;
    }

    public NewLineOption getOption() {
        return option;
    }

    public Line setOption(NewLineOption option) {
        this.option = option;
        return this;
    }

    public String getLocation() {
        return location;
    }

    public Line setLocation(String location) {
        this.location = location;
        return this;
    }

    public String getLocationTitle() {
        return locationTitle;
    }

    public Line setLocationTitle(String locationTitle) {
        this.locationTitle = locationTitle;
        return this;
    }

    public int getListLevel() {
        return listLevel;
    }

    public Line setListLevel(int listLevel) {
        this.listLevel = listLevel;
        return this;
    }

    public String getModuleMsg() {
        return moduleMsg;
    }

    public Line setModuleMsg(String moduleMsg) {
        this.moduleMsg = moduleMsg;
        return this;
    }

    public Line plusNum() {
        selfNum = ++num;
        return this;
    }

    public Line subNum() {
        selfNum = --num;
        return this;
    }

    public int getSelfNum() {
        if (this.selfNum == -1)
            return num;
        return selfNum;
    }

    public Line setSelfNum(int selfNum) {
        if (selfNum != -1) {
            this.selfNum = selfNum;
        }
        return this;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public Line getAnnotation() {
        return annotation;
    }

    public void setAnnotation(Line annotation) {
        this.annotation = annotation;
    }

    public Line getPre() {
        return pre;
    }

    public Line setPre(Line pre) {
        this.pre = pre;
        return this;
    }

    @Override
    public String toString() {
        return "Line{" +
                "num=" + selfNum +
                ", moduleName='" + moduleName + '\'' +
                ", methodName='" + methodName + '\'' +
                ", line='" + line + '\'' +
                ", newLine='" + newLine + '\'' +
                ", isModule=" + isModule +
                ", moduleLevel=" + moduleLevel +
                ", listLevel=" + listLevel +
                ", location='" + location + '\'' +
                ", order='" + order + '\'' +
                ", locationTitle='" + locationTitle + '\'' +
                ", moduleMsg='" + moduleMsg + '\'' +
                ", option=" + option +
                '}';
    }
}