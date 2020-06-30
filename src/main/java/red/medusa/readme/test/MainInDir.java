package red.medusa.readme.test;

import red.medusa.readme.AttachReadme;
import red.medusa.readme.ClassReadMe;
import red.medusa.readme.ReadMe;

@ClassReadMe("在test目录下若有readme则在里面创建")
public class MainInDir extends AttachReadme {

    @ReadMe(value = "test目录下的test3")
    public void test3() {

    }

    @ReadMe(value = "test目录下的test4",locTit = "这只是个title而已")
    public void test4() {

    }

}
