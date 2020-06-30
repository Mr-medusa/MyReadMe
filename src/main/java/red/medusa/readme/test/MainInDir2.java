package red.medusa.readme.test;

import red.medusa.readme.AttachReadme;
import red.medusa.readme.ClassReadMe;
import red.medusa.readme.ReadMe;

@ClassReadMe("在test目录下若有readme则在里面创建")
public class MainInDir2 extends AttachReadme {

    @ReadMe(value = "test目录下的test5")
    public void test5() {

    }

    @ReadMe(value = "test目录下的test6",locTit = "这只是个title而已")
    public void test6() {

    }

}
