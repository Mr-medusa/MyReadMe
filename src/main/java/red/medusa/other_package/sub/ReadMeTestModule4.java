package red.medusa.other_package.sub;

import red.medusa.readme.AttachReadme;
import red.medusa.readme.ClassReadMe;
import red.medusa.readme.ReadMe;

@ClassReadMe(value = "ReadMeTestModule4")
public class ReadMeTestModule4 extends AttachReadme {

    @ReadMe("test1")
    public void test1(){

    }

    @ReadMe(value = "test2")
    public void test2(){

    }
}
