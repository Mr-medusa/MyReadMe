package red.medusa.other_package;

import red.medusa.readme.AttachReadme;
import red.medusa.readme.ClassReadMe;
import red.medusa.readme.ReadMe;

@ClassReadMe(value = "ReadMeTestModule1-SSS",order = 5)
public class ReadMeTestModule1 extends AttachReadme {

    @ReadMe(value = "test1 - For")
    public void test1(){

    }
}
