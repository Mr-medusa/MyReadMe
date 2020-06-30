package red.medusa.readme;

@ClassReadMe(value = "当前目录下的readme")
public class MainInCurrentDir extends AttachReadme {

    @ReadMe(value = "当前目录下的test1")
    public void test1() {

    }

    @ReadMe(value = "当前目录下的test2",locTit = "这只是个title而已")
    public void test2() {

    }

}
