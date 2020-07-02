package red.medusa.readme.utils;

public class Log {
    public static boolean enableDebug = false;

    public static void log(String methodName, Object data) {
        if (enableDebug)
            System.out.println(methodName + ":" + data);
    }

    public static void separatorLog(Object data) {
        if (enableDebug) {
            String format = "%-100.100s";
            System.out.println();
            System.out.printf(format, "\n+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            System.out.printf(format, "\n++++++++++++++++++++++++++++++++   " + data.toString() + "  +++++++++++++++++++++++++++++++++++++++++++++++");
            System.out.printf(format, "\n+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            System.out.println();
        }
    }

    public static void main(String[] args) {
        System.out.printf("%-5s", "ABCDEFG");
        System.out.println();
        System.out.printf("%-5s", "A");
        System.out.println();

        System.out.printf("%-5.1s", "ABCDEFG");
        System.out.println();
        System.out.printf("%-5.5s", "A");
        System.out.println();

        System.out.printf("%5.1s", "ABCDEFG");
        System.out.println();
        System.out.printf("%5.5s", "A");
        System.out.println();
    }
}
