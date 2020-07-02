package red.medusa.readme.model;

import red.medusa.readme.utils.PathUtils;

public class Line implements Comparable<Line> {
    private static int num;
    private int order = 0;
    private Integer readMeOrder;
    private int moduleOrder = 0;
    private int selfNum = 0;
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
    private String methodUsage;
    private boolean isAnnotation;
    private Line annotation;
    private Line pre;
    private NewLineOption option = NewLineOption.NOTING;

    public Line() {
    }

    public Line(String line, String newLine) {
        this.line = line;
        this.newLine = newLine;
    }

    public Line modifyWithOldLine(Line param) {

        this.setOrder(param.getOrder());
        this.setModuleOrder(param.getModuleOrder());
        this.setModuleName(param.getModuleName());
        this.setMethodName(param.getMethodName());
        this.setModule(param.isModule());
        this.setModuleLevel(param.getModuleLevel());
        this.setLocation(param.getLocation());
        this.setLocationTitle(param.getLocationTitle());
        this.setListLevel(param.getListLevel());
        this.setModuleMsg(param.getModuleMsg());
        this.setMethodUsage(param.getMethodUsage());

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

    public int getSelfNum() {
        return selfNum;
    }

    public Line setSelfNum(int selfNum) {
        this.selfNum = selfNum;
        return this;
    }

    public Integer getReadMeOrder() {
        return readMeOrder;
    }

    public Line setReadMeOrder(Integer readMeOrder) {
        this.readMeOrder = readMeOrder;
        return this;
    }

    public int getOrder() {
        if (!this.isAnnotation() && this.getAnnotation() != null) {
            return this.getAnnotation().getOrder();
        }
        return order;
    }

    public Line setOrder(int order) {
        if (order >= 0)
            this.order = order;
        return this;
    }

    public boolean isAnnotation() {
        return isAnnotation;
    }

    public void setAnnotation(boolean annotation) {
        isAnnotation = annotation;
    }

    public Line getAnnotation() {
        return annotation;
    }

    public Line setAnnotation(Line annotation) {
        this.annotation = annotation;
        return this;
    }

    public Line getPre() {
        return pre;
    }

    public Line setPre(Line pre) {
        this.pre = pre;
        return this;
    }

    public String getMethodUsage() {
        return methodUsage;
    }

    public Line setMethodUsage(String methodUsage) {
        this.methodUsage = methodUsage;
        return this;
    }

    public int getModuleOrder() {
        return moduleOrder;
    }

    public Line setModuleOrder(int moduleOrder) {
        this.moduleOrder = moduleOrder;
        return this;
    }

    public boolean isBlank() {
        return PathUtils.isEmpty(this.getNewLine());
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
                ", moduleOrder='" + moduleOrder + '\'' +
                ", isAnnotation='" + isAnnotation + '\'' +
                ", locationTitle='" + locationTitle + '\'' +
                ", moduleMsg='" + moduleMsg + '\'' +
                ", methodUsage='" + methodUsage + '\'' +
                ", option=" + option +
                '}';
    }

    @Override
    public int compareTo(Line o) {
        return this.getOrder() - o.getOrder();
    }
}