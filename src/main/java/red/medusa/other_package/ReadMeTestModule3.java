package red.medusa.other_package;

import red.medusa.readme.AttachReadme;
import red.medusa.readme.ClassReadMe;
import red.medusa.readme.ReadMe;


@ClassReadMe(value = "ReadMeTestModule3",order = 3)
public class ReadMeTestModule3 extends AttachReadme {

    @ReadMe("test1")
    public void test1(){

    }
    @ReadMe("test2")
    public void test2(){

    }
}