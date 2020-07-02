package red.medusa.readme;

import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import red.medusa.readme.command.ReadMeExpert;
import red.medusa.readme.model.Line;
import red.medusa.readme.model.MarkDownTag;
import red.medusa.readme.model.NewLineOption;
import red.medusa.readme.model.ReadMeParam;
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
    private List<Line> methodLines = new ArrayList<>();

    // 文件中构建的
    private Map<ReadMeModule, List<Line>> lines = new LinkedHashMap<>();

    // 文件中构建的
//    private List<ReadMeModule> modules = new ArrayList<>();

    // 匹配文件中的模块名
    private Pattern matchNameCompile = Pattern.compile("^#{1,5}\\u0020+.*$");

    // 匹配简单的模块名 (为了方便以文件路径当作模块名)
    private Pattern matchSimpleNameCompile = Pattern.compile("^#{1,5}\\u0020\\[[\\w.]+]\\(([\\w./\\\\]+)\\u0020+.*\\)");

    private String relativePath = "";

    private Line currentLine;


    /**
     * 准备参数
     */
    private void preparedParams() {

        String readme = readMeParam.getREADME().getAbsolutePath();
        String classFilePath = ClassUtils.classPackageAsResourcePath(readMeParam.getClazz());
        relativePath = PathUtils.findRelativePath(readme, classFilePath) + readMeParam.getClazz().getSimpleName() + ".java";

        ClassReadMe classReadMe = readMeParam.getClassReadMe();

        /**
         * 当前模块
         */
        currentLine = new Line(
                ClassUtils.getShortName(readMeParam.getClazz()),
                null,
                null,
                StringUtils.cleanPath(relativePath),
                ClassUtils.getShortName(readMeParam.getClazz()),
                StringUtils.isEmpty(classReadMe.msg()) ? classReadMe.value() : classReadMe.msg(),
                classReadMe.moduleLevel(),
                0);

        /**
         * 当前模块中的Lines
         */
        for (ReadMeParam.ReadMeMethod readMeMethod : readMeParam.getReadMeMethods()) {

            ReadMe readMe = readMeMethod.getReadMe();

            methodLines.add(
                    new Line(
                            ClassUtils.getShortName(readMeParam.getClazz()),
                            readMeMethod.getMethod().getName(),
                            // 方法使用信息
                            !StringUtils.isEmpty(readMe.value()) ? readMe.value() : readMe.locTit(),
                            StringUtils.cleanPath(relativePath),
                            readMe.locTit(),
                            StringUtils.isEmpty(classReadMe.msg()) ? classReadMe.value() : classReadMe.msg(),
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
            for (String line : Files.lines(readMeParam.getREADME().toPath(), Charset.forName("UTF-8")).collect(Collectors.toList())) {

                Line readMeLine = new Line(line, line).plusNum();

                if (matchNameCompile.matcher(line).matches()) {
                    readMeLine.setModule(true).setModuleName(line);

                    Matcher matcher = matchSimpleNameCompile.matcher(line);

                    if (matcher.find()) {

                        String simpleModuleName = StringUtils.cleanPath(matcher.group(1));
                        readMeLine.setLocation(simpleModuleName);

                        moduleKey = new ReadMeModule(simpleModuleName);
                    }
                }


                this.lines.computeIfAbsent(moduleKey, (k) -> new ArrayList<>()).add(readMeLine);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 构造替换或添加标记
     */
    private void markLines() {
        // 若当前模块还没有创建则在末尾创建一个
        ReadMeModule readMeModule = new ReadMeModule(relativePath);

        // 获得当前模块
        List<Line> lineList = lines.get(readMeModule);
        boolean isFind = false;
        for (Iterator<Map.Entry<ReadMeModule, List<Line>>> it = lines.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<ReadMeModule, List<Line>> entry = it.next();
            if (entry.getKey().getModuleName().equals(readMeModule.getModuleName())) {
                isFind = true;
                lineList = entry.getValue();
                break;
            }
        }

        if (!isFind) {
            lineList = lines.computeIfAbsent(readMeModule, k -> new ArrayList<>());
            lineList.add(currentLine.plusNum());
        }

        // 替换模块
        lineList.get(0).modifyWithOldLine(currentLine).setOption(NewLineOption.REPLACE).setModule(true);

        /*
         * 在当前的模块看看哪些方法需要被替换
         */
        for (Line methodLine : methodLines) {
            markLine(methodLine, lineList);
        }
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
            for (List<Line> value : lines.values()) {
                for (Line line : value) {

//                    System.out.println(line);

                    Line build = ReadMeExpert.build(line);

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

    private static class ReadMeModule {

        String moduleName = "";

        private List<Line> lines = new ArrayList<>();

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

        @Override
        public String toString() {
            return moduleName;
        }

        public ReadMeModule addLine(Line line) {
            this.lines.add(line);
            return this;
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


}
