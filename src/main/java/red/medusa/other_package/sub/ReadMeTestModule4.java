package red.medusa.other_package.sub;

import red.medusa.readme.AttachReadme;
import red.medusa.readme.ClassReadMe;
import red.medusa.readme.ReadMe;

@ClassReadMe(value = "ReadMeTestModule4", order = 2 , groupName = "groupName")
public class ReadMeTestModule4 extends AttachReadme {

    @ReadMe(value = "test1", order = 2)
    public void test1() {

    }

    @ReadMe(value = "test2", locTit = "BBB", order = 12 ,separator = 2)
    public void test2() {

    }

    @ReadMe(value = "test3", locTit = "CCC",separator = 2) // 注意顺序
    public void test3() {

    }
}
