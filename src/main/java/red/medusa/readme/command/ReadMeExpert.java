package red.medusa.readme.command;

import red.medusa.readme.model.Line;

public class ReadMeExpert {

    public static Line build(Line line) {

        createReadMeLine(new AnnotationCommand(line));

        switch (line.getOption()) {
            case NOTING:
                return line;
            case INSERT:
            case REPLACE:
                if (line.isModule()) {
                    createReadMeLine(new ModuleCommand(line));
                } else {
                    createReadMeLine(new LinkCommand(line));
                }
                break;
        }
        return line;
    }

    public static void createReadMeLine(ReadMeCommand command) {
        command.execute();
    }
}
