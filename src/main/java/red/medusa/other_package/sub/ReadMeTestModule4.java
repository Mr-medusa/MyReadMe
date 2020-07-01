package red.medusa.other_package.sub;

import red.medusa.readme.AttachReadme;
import red.medusa.readme.ClassReadMe;
import red.medusa.readme.ReadMe;

@ClassReadMe(value = "ReadMeTestModule4",msg = "AAAAA")
public class ReadMeTestModule4 extends AttachReadme {

    @ReadMe("test1")
    public void test1(){

    }

    @ReadMe(value = "test2", locTit="AAAAA")
    public void test2(){

    }
}
