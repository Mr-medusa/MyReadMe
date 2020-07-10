package red.medusa.other_package.sub;

import red.medusa.readme.AttachReadme;
import red.medusa.readme.ClassReadMe;
import red.medusa.readme.ReadMe;

@ClassReadMe(value = "ReadMeTestModule5", order = 1 , groupName = "groupName")
public class ReadMeTestModule5 extends AttachReadme {

    @ReadMe(value = "test1", order = 2)
    public void test1() {

    }

    @ReadMe(value = "test2", locTit = "BBB", order = 12)
    public void test2() {

    }

    @ReadMe(value = "test3", locTit = "CCC")
    public void test3() {

    }
}
