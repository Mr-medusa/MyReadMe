package red.medusa.readme.model;

import red.medusa.readme.ClassReadMe;
import red.medusa.readme.ReadMe;

import java.io.File;
import java.lang.reflect.Method;
import java.util.List;

public class ReadMeParam {
    private Class<?> clazz;
    private ClassReadMe classReadMe;
    private List<ReadMeMethod> readMeMethods;
    private File README;

    public ReadMeParam() {
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    public ClassReadMe getClassReadMe() {
        return classReadMe;
    }

    public void setClassReadMe(ClassReadMe classReadMe) {
        this.classReadMe = classReadMe;
    }

    public List<ReadMeMethod> getReadMeMethods() {
        return readMeMethods;
    }

    public void setReadMeMethods(List<ReadMeMethod> readMeMethods) {
        this.readMeMethods = readMeMethods;
    }

    public File getREADME() {
        return README;
    }

    public void setREADME(File README) {
        this.README = README;
    }

    public static class ReadMeMethod{
        private ReadMe readMe;
        private Method method;

        public ReadMeMethod(ReadMe readMe, Method method) {
            this.readMe = readMe;
            this.method = method;
        }

        public ReadMe getReadMe() {
            return readMe;
        }

        public void setReadMe(ReadMe readMe) {
            this.readMe = readMe;
        }

        public Method getMethod() {
            return method;
        }

        public void setMethod(Method method) {
            this.method = method;
        }
    }
}
