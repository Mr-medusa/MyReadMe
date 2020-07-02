package red.medusa.readme.utils;

import org.springframework.util.StringUtils;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PathUtils {

    public static String findRelativePath(String a, String b) {
        String aToUse = StringUtils.cleanPath(a);
        String bToUse = StringUtils.cleanPath(b + "/");
        Path pathB = Paths.get(bToUse);
        int nameCountB = pathB.getNameCount();

        StringBuilder subDir = new StringBuilder();
        StringBuilder temp = new StringBuilder();
        int index;
        int specificAIndex = -1;
        for (int i = 0; i < nameCountB; i++) {
            temp.append(pathB.getName(i).toString());
            index = a.indexOf(temp.toString());
            temp.append(File.separator);
            if (index != -1) {
                subDir.append(pathB.getName(i).toString()).append(File.separator);
                specificAIndex = index;
            }
        }

        StringBuilder sb = new StringBuilder();

        if (specificAIndex != -1)
            aToUse = aToUse.substring(specificAIndex + subDir.toString().length());
        Path aToUsePath = Paths.get(aToUse);

        for (int i = 1; i < aToUsePath.getNameCount(); i++) {
            sb.append("../");
        }
        bToUse = bToUse.substring(subDir.toString().length());
        sb.append(bToUse);
        return sb.toString();
    }

}
