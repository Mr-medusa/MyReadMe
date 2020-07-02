package red.medusa.other_package;

import red.medusa.readme.AttachReadme;
import red.medusa.readme.ClassReadMe;
import red.medusa.readme.ReadMe;

@ClassReadMe(value = "ReadMeTestModule2",msg = "二号模块")
public class ReadMeTestModule2 extends AttachReadme {

    @ReadMe(value = "test1")
    public void test1(){

    }
}