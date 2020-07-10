package red.medusa.readme;

import red.medusa.readme.command.AnnotationCommand;
import red.medusa.readme.command.ReadMeExpert;
import red.medusa.readme.model.*;
import red.medusa.readme.utils.Log;
import red.medusa.readme.utils.PathUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ReadMeWorker {

    private ReadMeParam readMeParam;

    // 方法中构建的当前模块
    private static List<Line> methodLines = new ArrayList<>();

    private ReadMeModuleList moduleList = ReadMeModuleList.getInstance();

    // 匹配文件中的模块名
    private static Pattern matchNameCompile = Pattern.compile("^#{1,5}\\u0020+.*");

    // 匹配简单的模块名 (为了方便以文件路径当作模块名)
    private static Pattern matchLocationCompile = Pattern.compile("^#{1,5}\\u0020\\[[\\w.]+]\\(([\\w./\\\\]+)\\u0020+.*\\)");

    private static Pattern matchMethodNameCompile = Pattern.compile("^\\u0020*[+\\d\\.]{1,2}\\u0020\\[([\\w.]+)].+$");

    private String relativePath = "";

    private Line currentModuleLine;

    /**
     * 准备参数
     */
    private void preparedParams() {

        String readme = readMeParam.getREADME().getAbsolutePath();
        String classFilePath = PathUtils.classPackageAsResourcePath(readMeParam.getClazz());
        relativePath = PathUtils.cleanPath(PathUtils.findRelativePath(readme, classFilePath) + readMeParam.getClazz().getSimpleName() + ".java");

        ClassReadMe classReadMe = readMeParam.getClassReadMe();

        /**
         * 当前模块
         */
        currentModuleLine = new Line()
                .setModuleOrder(classReadMe.order())
                .setModuleName(PathUtils.getShortName(readMeParam.getClazz()))
                .setLocation(relativePath)
                .setLocationTitle(PathUtils.getShortName(readMeParam.getClazz()))
                .setModuleMsg(!PathUtils.isEmpty(classReadMe.value()) ? classReadMe.value() : classReadMe.msg())
                .setModuleLevel(classReadMe.moduleLevel());

        /*
         * 当前模块中的Lines
         * 注意:
         *      JVM在编译时,会自行决定类成员的顺序,不一定要按照代码中的声明顺序来进行编译。
         *      因此返回的顺序其实是class文件中的成员正向顺序,若要完全指定顺序请使用Order吧!
         */
        for (ReadMeParam.ReadMeMethod readMeMethod : readMeParam.getReadMeMethods()) {

            ReadMe readMe = readMeMethod.getReadMe();

            methodLines.add(
                    new Line()
                            .setOrder(readMe.order())
                            .setReadMeOrder(readMe.order() == 0 ? null : readMe.order())
                            .setModuleName(PathUtils.getShortName(readMeParam.getClazz()))
                            .setMethodName(readMeMethod.getMethod().getName())
                            .setMethodUsage(!PathUtils.isEmpty(readMe.value()) ? readMe.value() : readMe.usage())
                            .setLocation(relativePath)
                            .setLocationTitle(readMe.locTit())
                            .setListLevel(readMe.listLevel())
                            .setSeparator(readMe.separator())

            );
        }
    }

    /**
     * 建立ReadMeLine数据集
     */
    private void initData() {

        Log.separatorLog("initData Start");

        ReadMeModule moduleKey = new ReadMeModule(MarkDownTag.SEPARATOR);

        try {
            String moduleName = "";
            String locationName = "";
            for (String line : Files.lines(readMeParam.getREADME().toPath(), Charset.forName("UTF-8")).collect(Collectors.toList())) {
                // continue
                if(line.trim().equals("---"))
                    continue;

                String[] lines = line.split("<!-- ");
                Line readMeLine = new Line(lines[0], lines[0]).plusNum();
                Line annotationLine = null;
                if (lines.length == 2)
                    annotationLine = new Line("<!-- " + lines[1], "<!-- " + lines[1]).plusNum();

                if (matchNameCompile.matcher(line).matches()) {
                    // 是模块
                    readMeLine.plusNum().setModule(true).setModuleName(line);
                    Matcher matcher = matchLocationCompile.matcher(line);
                    if (matcher.find()) {
                        locationName = PathUtils.cleanPath(matcher.group(1));
                        // 添加模块名
                        moduleName = locationName.substring(locationName.lastIndexOf("/") + 1, locationName.lastIndexOf("."));
                        // 添加位置
                        readMeLine.setLocation(locationName);
                        readMeLine.setModuleName(moduleName);

                        // 路径确定模块
                        moduleKey = new ReadMeModule(locationName);
                        moduleList.getOrPutWhenNotFind(moduleKey);
                    }
                } else {
                    Matcher matcher = matchMethodNameCompile.matcher(line);
                    if (matcher.find()) {
                        readMeLine.setMethodName(matcher.group(1));
                        readMeLine.setModuleName(moduleName);
                        readMeLine.setLocation(locationName);
                    }
                }

                if (lines.length == 2 && AnnotationCommand.isAnnotation(annotationLine.getLine())) {
                    recoveryLine(annotationLine);
                    annotationLine.setAnnotation(true);
                    readMeLine.setPre(annotationLine);
                    moduleKey.addLine(readMeLine);
                } else if (!PathUtils.isEmpty(line)) {   // 这里排除空行
                    moduleKey.addLine(readMeLine);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.log("initData[moduleMap] = ", moduleList.toString());

        Log.separatorLog("initData End");
    }

    public static void recoveryLine(Line readMeLine) {

        Map<String, String> tags = AnnotationCommand.findTags(readMeLine.getLine());

        String order = tags.getOrDefault("order", "0");
        String moduleOrder = tags.getOrDefault("module_order", "0");
        String isModule = tags.getOrDefault("is_module", "false");
        String moduleName = tags.getOrDefault("module_name", "\u0020");
        String methodName = tags.getOrDefault("method_name", "\u0020");
        String methodLevel = tags.getOrDefault("method_Level", "3");
        String moduleLevel = tags.getOrDefault("module_Level", "3");
        String separator = tags.getOrDefault("separator", "0");

        readMeLine.setOrder(Integer.valueOf(order.trim()));
        readMeLine.setModuleOrder(Integer.valueOf(moduleOrder.trim()));

        readMeLine.setModuleName(moduleName.trim());
        readMeLine.setModule(isModule.trim().equals("true"));
        readMeLine.setMethodName(methodName.trim());
        readMeLine.setListLevel(Integer.valueOf(methodLevel.trim()));
        readMeLine.setModuleLevel(Integer.valueOf(moduleLevel.trim()));
        try{
            readMeLine.setSeparator(Integer.valueOf(separator));
        }catch (NumberFormatException e){}
    }

    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    //+++++++++++++++++++++++++++++++++++++      构造替换或添加标记            ++++++++++++++++++++++++++++++++++++
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    private void markLines() {
        Log.separatorLog("markLines Start");

        // 获得当前模块
        ReadMeModule readMeModule = getCurrentModuleLines();

        // 出于方便不管在不在里面都直接替换模块
        Line currentLine = readMeModule.getLines().get(0);
        currentLine.modifyWithOldLine(currentModuleLine)
                .setOrder(0)       // 保证它在当前模块的第一个位置
                .setOption(NewLineOption.REPLACE)
                .setModule(true);

        /*
         * 在当前的模块看看哪些方法需要被替换
         */
        List<Line> queryLines = readMeModule.getLines();
        for (Line methodLine : methodLines) {
            markLine(methodLine, queryLines);
        }


        Log.log("markLines[moduleMap] = ", moduleList.toString());

        Log.separatorLog("markLines End");
    }

    /**
     * @param line    - 方法中的Line
     * @param modules - Modules
     */
    private void markLine(Line line, List<Line> modules) {
        boolean isFind = false;
        int num = -1;
        for (Line moduleLine : modules) {
            num = moduleLine.getSelfNum();
            if (!moduleLine.isModule() && moduleLine.getLine().startsWith("+ [" + line.getMethodName() + "]")) {
                // 注意这里是是设置模块里的line
                moduleLine.modifyWithOldLine(line);
                moduleLine.setOption(NewLineOption.REPLACE);
                /*
                 * 设置Order的顺序
                 */
                if (line.getReadMeOrder() != null) {
                    moduleLine.setOrder(line.getReadMeOrder());
                }
                isFind = true;
                break;
            }
        }
        // 没有找到添加到里面去
        if (!isFind) {
            // line 自带order,无需再设置
            modules.add(line.setSelfNum(++num).setOption(NewLineOption.INSERT));
        }
    }

    private ReadMeModule getCurrentModuleLines() {
        // 仅仅作为比较
        ReadMeModule readMeModule = new ReadMeModule(relativePath);
        // 获得当前模块 若获取不到则加入到其中并添加当前Line(注意第0个默认是模块类型的Line)
        List<Line> lineList = moduleList.getOrPutWhenNotFind(readMeModule).getLines();
        if (lineList.isEmpty()) {
            lineList.add(currentModuleLine.setSelfNum(moduleList.size()));
        }
        return moduleList.getOrPutWhenNotFind(readMeModule);
    }
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    //+++++++++++++++++++++++++++++++++++++      构造替换或添加标记            ++++++++++++++++++++++++++++++++++++
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    /**
     * 生成ReadMe
     *
     * @param lines
     */
    private void parseReadMeForTest(List<Line> lines) {
        Log.separatorLog("parseReadMeForTest Start");
        for (Line line : lines) {
            System.out.println(line.getAnnotation());
            System.out.println(line);
        }
        Log.separatorLog("parseReadMeForTest End");
    }

    private void consultingExperts() {
        for (ReadMeModule readMeModule : moduleList) {
            for (Line line : readMeModule.getLines()) {
                ReadMeExpert.build(line);
            }
        }
    }

    private void parseReadMe(List<Line> lines) {
        if (Log.enableDebug) {
            parseReadMeForTest(lines);
            return;
        }
        try (PrintWriter p = new PrintWriter(
                new BufferedWriter(
                        new OutputStreamWriter(new FileOutputStream(readMeParam.getREADME()), Charset.forName("UTF-8"))
                ), true
        )) {
            for (Line line : lines) {

                printSeparatorTop(p,line);

                printPrettyModule(p, line);

                p.print(line.getNewLine());

                if (line.getAnnotation() != null)
                    p.println(" " + line.getAnnotation().getNewLine());

                printSeparatorBottom(p,line);
            }
        } catch (
                IOException e) {
            e.printStackTrace();
        }

    }
    // ------- 添加一些样式 -------
    //------------------------------------------------------------------------------------
    private void printPrettyModule(PrintWriter p, Line line) {
        if (line != null && line.isModule()) {
            if (line.getAnnotation() != null && line.getAnnotation().getPre() != null && line.getAnnotation().getPre().isBlank()) {
                p.println();
            } else {
                if (line.getPre() != null && line.getPre().isBlank())
                    p.println();
            }
        }
    }
    private void printSeparatorTop(PrintWriter p, Line line) {
        if(line.getSeparator() == 1 || line.getSeparator() == 3)
            p.println("---");
    }
    private void printSeparatorBottom(PrintWriter p, Line line) {
        if(line.getSeparator() == 2 || line.getSeparator() == 3)
            p.println("---");
    }
    //------------------------------------------------------------------------------------

    private List<Line> sortLines() {
        List<Line> lines = new ArrayList<>();
        moduleList.list().stream().sorted().map(ReadMeModule::getLines).forEach(
                ms -> lines.addAll(ms.stream().sorted().collect(Collectors.toList()))
        );
        return lines;
    }


    public ReadMeWorker(ReadMeParam readMeParam) {
        this.readMeParam = readMeParam;
        work();
    }

    private void work() {
        // 准备参数
        preparedParams();
        // 建立数据集
        initData();
        // 构造标记
        markLines();
        // 构造ReadMe数据
        consultingExperts();
        // 写入数据
        parseReadMe(sortLines());
    }
}






















