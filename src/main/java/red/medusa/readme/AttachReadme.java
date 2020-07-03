package red.medusa.readme;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import red.medusa.readme.model.ReadMeFlag;
import red.medusa.readme.model.ReadMeParam;
import red.medusa.readme.utils.PathUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Optional;

@ExtendWith({AttachReadme.ReadMeCallback.class})
public class AttachReadme {

    private static Class<?> that;

    public static void buildReadMe() {
        // 获得当前目录
        Path basePath = Paths.get("", "src/main/java/");
        // 当前类的目录
        Path childPath = Paths.get(PathUtils.classPackageAsResourcePath(that));
        // 当前类的全路径
        Path fullPath = basePath.resolve(childPath);

        int basePathNameCount = basePath.getNameCount();

        Path readMePath;

        while ((readMePath = findReadMeFile(fullPath)) == null) {
            // 这里是边界  src/main/java/red/medusa
            if (fullPath.getNameCount() - basePathNameCount > 3) {
                // 向上一级查询
                fullPath = fullPath.getParent();
            } else {
                break;
            }
        }

        // 生成README
        File README = null;
        if (readMePath != null) {
            README = new File(readMePath.resolve(Paths.get("README.md")).toUri());
        } else {
            try {
                // 就在当前目录下创建吧
                Path directory = Files.createDirectory(basePath.resolve(childPath).resolve("readme"));
                README = new File(directory.resolve("README.md").toUri());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (!README.exists()) {
            try {
                README.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (README.exists()) {

            ReadMeParam readMeParam = findReadMe();

            if (readMeParam != null) {

                readMeParam.setREADME(README);

                new ReadMeWorker(readMeParam);
            }
        }
    }

    /**
     * 构建参数
     */
    private static ReadMeParam findReadMe() {

        ClassReadMe classReadMe = that.getAnnotation(ClassReadMe.class);

        ReadMeParam param = new ReadMeParam();
        ArrayList<ReadMeParam.ReadMeMethod> readMeSet = new ArrayList<>(8);

        Method[] methods = that.getMethods();
        for (Method method : methods) {
            // 所有有ReadMe注解的方法
            ReadMe readMe = method.getAnnotation(ReadMe.class);
            if (readMe != null) {
                readMeSet.add(new ReadMeParam.ReadMeMethod(readMe, method));
            }
        }

        param.setClazz(that);
        param.setClassReadMe(classReadMe);
        param.setReadMeMethods(readMeSet);

        return param;
    }

    /**
     * 找到README文件所在目录位置
     */
    public static Path findReadMeFile(Path asbPath) {
        try {
            return Files.walk(asbPath).filter(path -> Files.isDirectory(path) && path.toString().endsWith("readme"))
                    .findFirst().orElse(null);
        } catch (IOException e) {
        }

        return null;
    }

    protected static class ReadMeCallback implements AfterAllCallback {
        @Override
        public void afterAll(ExtensionContext context) {
            Optional<Class<?>> oC = context.getTestClass();
            if (oC.isPresent()) {
                that = oC.get();
                ClassReadMe classReadMe = that.getAnnotation(ClassReadMe.class);
                if (!(that == null || classReadMe == null || classReadMe.flag() == ReadMeFlag.DONE)) {
                    buildReadMe();
                }

            }
        }
    }

}












