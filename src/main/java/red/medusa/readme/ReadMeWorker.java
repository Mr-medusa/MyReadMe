package red.medusa.readme;

import red.medusa.readme.command.ReadMeExpert;
import red.medusa.readme.model.*;
import red.medusa.readme.utils.PathUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ReadMeWorker {

    private ReadMeParam readMeParam;

    // 方法中构建的当前模块
    private static List<Line> methodLines = new ArrayList<>();

    // 文件中构建的
//    private Map<ReadMeModule, List<Line>> lines = new LinkedHashMap<>();

    private ReadMeModuleList moduleList = ReadMeModuleList.getInstance();

    // 匹配文件中的模块名
    public static Pattern matchNameCompile = Pattern.compile("^#{1,5}\\u0020+.*$");

    // 匹配简单的模块名 (为了方便以文件路径当作模块名)
    public static Pattern matchSimpleNameCompile = Pattern.compile("^#{1,5}\\u0020\\[[\\w.]+]\\(([\\w./\\\\]+)\\u0020+.*\\)");

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
        currentModuleLine = new Line(
                PathUtils.getShortName(readMeParam.getClazz()),
                null,
                null,
                relativePath,
                PathUtils.getShortName(readMeParam.getClazz()),
                PathUtils.isEmpty(classReadMe.msg()) ? classReadMe.value() : classReadMe.msg(),
                classReadMe.moduleLevel(),
                0);

        /**
         * 当前模块中的Lines
         */
        for (ReadMeParam.ReadMeMethod readMeMethod : readMeParam.getReadMeMethods()) {

            ReadMe readMe = readMeMethod.getReadMe();

            methodLines.add(
                    new Line(
                            PathUtils.getShortName(readMeParam.getClazz()),
                            readMeMethod.getMethod().getName(),
                            // 方法使用信息
                            !PathUtils.isEmpty(readMe.value()) ? readMe.value() : readMe.locTit(),
                            relativePath,
                            readMe.locTit(),
                            PathUtils.isEmpty(classReadMe.msg()) ? classReadMe.value() : classReadMe.msg(),
                            classReadMe.moduleLevel(),
                            readMe.listLevel())
            );

        }
    }

    /**
     * 建立ReadMeLine数据集
     */
    private void initData() {

        ReadMeModule moduleKey = new ReadMeModule(MarkDownTag.SEPARATOR);

        try {

            Line pre = null;
            for (String line : Files.lines(readMeParam.getREADME().toPath(), Charset.forName("UTF-8")).collect(Collectors.toList())) {

                Line readMeLine = new Line(line, line).plusNum();

                if (matchNameCompile.matcher(line).matches()) {                 // 是模块
                    readMeLine.setModule(true).setModuleName(line);
                    Matcher matcher = matchSimpleNameCompile.matcher(line);
                    if (matcher.find()) {
                        // 添加模块名
                        String simpleModuleName = PathUtils.cleanPath(matcher.group(1));
                        // 添加位置
                        readMeLine.setLocation(simpleModuleName);

                        moduleKey = new ReadMeModule(simpleModuleName);
                    }
                }
                pre = readMeLine;
                moduleKey.addLine(readMeLine.setPre(pre));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * 构造替换或添加标记
     */
    private void markLines() {

        // 获得当前模块
        ReadMeModule readMeModule = getCurrentModuleLines();

        // 调整排序
//        sortModule(readMeModule);

        // 替换模块
        Line currentModuleLine = readMeModule.getLines().get(0);

        currentModuleLine.modifyWithOldLine(currentModuleLine).setOption(NewLineOption.REPLACE).setModule(true);

        /*
         * 在当前的模块看看哪些方法需要被替换
         */
        for (Line methodLine : methodLines) {
            markLine(methodLine, readMeModule.getLines());
        }
    }

    private void sortModule(ReadMeModule readMeModule) {
        // 若当前模块还没有创建则在末尾创建一个
        int order = readMeParam.getClazz().getAnnotation(ClassReadMe.class).order();
        if (order != -1) {
            moduleList.shift(readMeModule, order);
        }
    }

    private ReadMeModule getCurrentModuleLines() {
        // 仅仅作为比较
        ReadMeModule readMeModule = new ReadMeModule(relativePath, false);

        // 查看当前模块是否以加载到了列表中
        boolean isFind = false;
        for (ReadMeModule meModule : moduleList) {
            if (meModule.getModuleName().equals(readMeModule.getModuleName())) {      // 找到了当前模块
                isFind = true;
                break;
            }
        }

        // 获得当前模块 若获取不到则加入到其中并添加当前Line(注意第0个默认是模块类型的Line)
        List<Line> lineList = moduleList.get(readMeModule).getLines();
        if (!isFind) {
            lineList.add(currentModuleLine.plusNum());
        }
        return moduleList.get(readMeModule);
    }


    private void markLine(Line line, List<Line> modules) {
        boolean isFind = false;
        int num = -1;
        for (Line moduleLine : modules) {
            num = moduleLine.getSelfNum();
            if (!moduleLine.isModule() && moduleLine.getLine().startsWith("+ [" + line.getMethodName() + "]")) {
                // 注意这里是是设置模块里的line
                moduleLine.modifyWithOldLine(line);
                moduleLine.setOption(NewLineOption.REPLACE);
                isFind = true;
                break;
            }
        }
        // 没有找到添加到里面去
        if (!isFind) {
            line.setSelfNum(++num).setOption(NewLineOption.INSERT);
            modules.add(line);
        }
    }


    /**
     * 生成ReadMe
     */
    public void parseReadMe() {
        try (PrintWriter p = new PrintWriter(
                new BufferedWriter(
                        new OutputStreamWriter(new FileOutputStream(readMeParam.getREADME()), Charset.forName("UTF-8"))
                )
        )) {
            for (ReadMeModule module : moduleList) {
                for (Line line : module.getLines()) {

                    System.out.println(line);

                    Line build = ReadMeExpert.build(line);
                    if (line.getAnnotation() != null)
                        p.println(line.getAnnotation().getNewLine());
                    p.println(build.getNewLine());
                }
            }
            p.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public ReadMeWorker(ReadMeParam readMeParam) {
        this.readMeParam = readMeParam;
        execute();
    }

    public void execute() {
        // 准备参数
        preparedParams();
        // 建立数据集
        initData();
        // 构造标记
        markLines();
        // 生成README
        parseReadMe();

    }

}






















