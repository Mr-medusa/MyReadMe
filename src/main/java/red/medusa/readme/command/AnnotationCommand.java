package red.medusa.readme.command;

import red.medusa.readme.model.Line;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnnotationCommand extends ReadMeCommand {
    // 匹配注释
    public static Pattern matchAnnotation = Pattern.compile("^\\u0020*<!--\\u0020+.+\\u0020+-->\\u0020*$");

    // 找到注解数据
    public static Pattern MATCH_TAG = Pattern.compile("(\\w+)\\u0020?=\\u0020?\"(\\w+)\"");
    public static String CHECK_TAG = "^\\u0020*<!-- .+-->$";

    public AnnotationCommand(Line line) {
        super(line);
    }

    @Override
    public void execute() {

    }

    private boolean isAnnonation(String line) {
        return matchAnnotation.matcher(line).matches();
    }

    static Map<String, String> findTags(String line) {
        Map<String, String> tags = new HashMap<>(8);
        if (line.matches(CHECK_TAG)) {
            Matcher matcher = MATCH_TAG.matcher(line);
            while (matcher.find()) {
                String key = matcher.group(1);
                String value = matcher.group(2);
                if (!(key == null || value == null)) {
                    tags.put(key, value);
                }
            }
        }
        return tags;
    }

    // 当前line是注解
    private Line createAnnonationForLine(Line readMeLine) {

        Line annonLine = new Line().modifyWithOldLine(readMeLine);

        StringBuilder sb = new StringBuilder();
        sb.append("<!-- ");
        sb.append(" order = \"").append(readMeLine.getOrder()).append("\" ");
        sb.append(" is-module = \"").append(readMeLine.isModule() ? "true" : "false").append("\" ");
        sb.append(" module_name = \"").append(readMeLine.getModuleName()).append("\" ");
        sb.append(" method_name = \"").append(readMeLine.getMethodName()).append("\" ");
        sb.append(" module-Level = \"").append(readMeLine.getModuleLevel()).append("\" ");
        sb.append(" method-Level = \"").append(readMeLine.getModuleLevel()).append("\" ");
        sb.append(" -->");

        annonLine.setLine(sb.toString());
        annonLine.setNewLine(sb.toString());

        return annonLine;
    }

    // 当前line设置一个注解
    private void addAnnonationForLine(Line readMeLine) {

        Line annonLine = new Line().modifyWithOldLine(readMeLine);

        StringBuilder sb = new StringBuilder();
        sb.append("<!-- ");
        sb.append(" order = \"").append(readMeLine.getOrder()).append("\" ");
        sb.append(" is-module = \"").append(readMeLine.isModule() ? "true" : "false").append("\" ");
        sb.append(" module_name = \"").append(readMeLine.getModuleName()).append("\" ");
        sb.append(" method_name = \"").append(readMeLine.getMethodName()).append("\" ");
        sb.append(" module-Level = \"").append(readMeLine.getModuleLevel()).append("\" ");
        sb.append(" method-Level = \"").append(readMeLine.getModuleLevel()).append("\" ");
        sb.append(" -->");

        annonLine.setLine(sb.toString());
        annonLine.setNewLine(sb.toString());

        readMeLine.setAnnotation(annonLine);
    }

    private void setAnnoData(Line readMeLine) {

        Map<String, String> tags = findTags(readMeLine.getLine());

        String order = tags.get("order");
        String isModule = tags.get("is-module");
        String moduleName = tags.get("module_name");
        String methodName = tags.get("method_name");
        String methodLevel = tags.get("method-Level");
        String moduleLevel = tags.get("module-Level");

        if (order != null) {
            readMeLine.setOrder(Integer.valueOf(order.trim()));
        }
        if (moduleName != null) {
            readMeLine.setModuleName(moduleName.trim());
        }
        if (isModule != null) {
            readMeLine.setModule(isModule.trim().equals("true") ? true : false);
        }
        if (methodName != null) {
            readMeLine.setModuleName(methodName.trim());
        }
        if (methodLevel != null) {
            readMeLine.setListLevel(Integer.valueOf(methodLevel.trim()));
        }
        if (moduleLevel != null) {
            readMeLine.setModuleLevel(Integer.valueOf(moduleLevel.trim()));
        }

    }
}