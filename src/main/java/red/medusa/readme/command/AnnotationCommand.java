package red.medusa.readme.command;

import red.medusa.readme.model.Line;
import red.medusa.readme.model.NewLineOption;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnnotationCommand extends ReadMeCommand {
    // 匹配注释
    private static Pattern matchAnnotation = Pattern.compile("^\\u0020*<!--\\u0020+.+\\u0020+-->\\u0020*$");

    // 找到注解数据
    private static Pattern MATCH_TAG = Pattern.compile("(\\w+)\\u0020?=\\u0020?\"(\\w+)\"");

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
                // 若前面是当前行的注解
                if (isSameModule(line)) {
                    // 且该行需要更新 则更新注解
                    if (line.getOption() == NewLineOption.REPLACE) {
                        pre.modifyWithOldLine(line);
                        // 设置新注解
                        updateAnnotation(pre);
                    }
                    line.setAnnotation(pre);
                }
            } else {
                // 为当前行生成新的注解
                createAnnotationWithAdd(line);
            }
        } else {
            // 前面没有注解也生成一个
            createAnnotationWithAdd(line);
        }
    }

    public static boolean isAnnotation(String line) {
        return line != null && matchAnnotation.matcher(line).matches();
    }

    private static boolean isSameModule(Line line) {
        return !(tags.get("module_name") == null || line.getModuleName() == null) && line.getModuleName().equals(tags.get("module_name"));
    }

    // 当前line是注解
    private Line createAnnotationWithAdd(Line readMeLine) {

        Line annonLine = new Line().modifyWithOldLine(readMeLine);

        StringBuilder sb = new StringBuilder();
        sb.append("<!-- ");
        sb.append(" order = \"").append(readMeLine.getOrder()).append("\" ");
        sb.append(" module_order = \"").append(readMeLine.getModuleOrder()).append("\" ");
        sb.append(" is_module = \"").append(readMeLine.isModule() ? "true" : "false").append("\" ");
        sb.append(" module_name = \"").append(readMeLine.getModuleName()).append("\" ");
        sb.append(" method_name = \"").append(readMeLine.getMethodName()).append("\" ");
        sb.append(" module_Level = \"").append(readMeLine.getModuleLevel()).append("\" ");
        sb.append(" method_Level = \"").append(readMeLine.getModuleLevel()).append("\" ");
        sb.append(" -->");

        annonLine.setLine(sb.toString());
        annonLine.setNewLine(sb.toString());

        readMeLine.setAnnotation(annonLine);

        return annonLine;
    }

    private void updateAnnotation(Line annoLine) {

        StringBuilder sb = new StringBuilder();
        sb.append("<!-- ");
        sb.append(" order = \"").append(annoLine.getOrder()).append("\" ");
        sb.append(" module_order = \"").append(annoLine.getModuleOrder()).append("\" ");
        sb.append(" is_module = \"").append(annoLine.isModule() ? "true" : "false").append("\" ");
        sb.append(" module_name = \"").append(annoLine.getModuleName()).append("\" ");
        sb.append(" method_name = \"").append(annoLine.getMethodName()).append("\" ");
        sb.append(" module_Level = \"").append(annoLine.getModuleLevel()).append("\" ");
        sb.append(" method_Level = \"").append(annoLine.getModuleLevel()).append("\" ");
        sb.append(" -->");

        annoLine.setNewLine(sb.toString());
    }

    public static Map<String, String> findTags(String line) {
        String CHECK_TAG = "^\\u0020*<!-- .+-->$";
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