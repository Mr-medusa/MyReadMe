package red.medusa.readme.command;

import red.medusa.readme.model.Line;

public class ReadMeExpert {

    public static Line build(Line line) {

        createReadMeLine(new AnnotationCommand(line));

        switch (line.getOption()) {
            case NOTING:
                break;
            case INSERT:
            case REPLACE:
                if (line.isModule()) {
                    createReadMeLine(new ModuleCommand(line));
                } else {
                    createReadMeLine(new LinkCommand(line));
                }
                break;
        }

        createReadMeLine(new AdornCommand(line));

        return line;
    }

    private static void createReadMeLine(ReadMeCommand command) {
        command.execute();
    }
}
