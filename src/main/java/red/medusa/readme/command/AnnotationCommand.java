package red.medusa.readme.command;

import red.medusa.readme.model.Line;
import red.medusa.readme.model.NewLineOption;

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

    private static Map<String, String> tags = new HashMap<>();

    public AnnotationCommand(Line line) {
        super(line);
    }

    @Override
    public void execute() {
        Line pre = line.getPre();

        boolean currentIsAnnotation = isAnnotation(line.getLine());
        if (currentIsAnnotation) {
            return;
        }
        if (pre != null) {
            if (isAnnotation(pre.getLine())) {
                findTags(pre.getLine());
                if (isSameModule(line)) {
                    // 更新pre
                    if (line.getOption() == NewLineOption.REPLACE) {
                        pre.modifyWithOldLine(line);
                    }
                    line.setAnnotation(pre);
                }
            } else {
                createAnnotationWithAdd(line);
            }
        }else{
            createAnnotationWithAdd(line);
        }
    }

    public static boolean isAnnotation(String line) {
        return matchAnnotation.matcher(line).matches();
    }

    private boolean isSameModule(Line line) {
        return line.getModuleName().equals(tags.get("module_name"));
    }

    // 当前line是注解
    private Line createAnnotationWithAdd(Line readMeLine) {

        Line annonLine = new Line().modifyWithOldLine(readMeLine);

        StringBuilder sb = new StringBuilder();
        sb.append("<!-- ");
        sb.append(" order = \"").append(readMeLine.getOrder()).append("\" ");
        sb.append(" is_module = \"").append(readMeLine.isModule() ? "true" : "false").append("\" ");
        sb.append(" module_name = \"").append(readMeLine.getModuleName()).append("\" ");
        sb.append(" method_name = \"").append(readMeLine.getMethodName()).append("\" ");
        sb.append(" module_Level = \"").append(readMeLine.getModuleLevel()).append("\" ");
        sb.append(" method_Level = \"").append(readMeLine.getModuleLevel()).append("\" ");
        sb.append(" -->");

        annonLine.setLine(sb.toString());
        annonLine.setNewLine(sb.toString());

        line.setAnnotation(annonLine);

        return annonLine;
    }

    public static void recoveryLine(Line readMeLine) {
        findTags(readMeLine.getLine());
        String order = tags.getOrDefault("order", "100");
        String isModule = tags.getOrDefault("is_module", "false");
        String moduleName = tags.getOrDefault("module_name", "\u0020");
        String methodName = tags.getOrDefault("method_name", "\u0020");
        String methodLevel = tags.getOrDefault("method_Level", "3");
        String moduleLevel = tags.getOrDefault("module_Level", "3");

        readMeLine.setOrder(Integer.valueOf(order.trim()));
        readMeLine.setModuleName(moduleName.trim());
        readMeLine.setModule(isModule.trim().equals("true"));
        readMeLine.setMethodName(methodName.trim());
        readMeLine.setListLevel(Integer.valueOf(methodLevel.trim()));
        readMeLine.setModuleLevel(Integer.valueOf(moduleLevel.trim()));
    }

    static Map<String, String> findTags(String line) {
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
}