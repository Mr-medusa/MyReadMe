package red.medusa.readme.command;

import red.medusa.readme.model.Line;

public class ReadMeExpert {
    private static ReadMeCommand annotationCommand = new AnnotationCommand();
    private static ReadMeCommand moduleCommand = new ModuleCommand();
    private static ReadMeCommand linkCommand = new LinkCommand();
    private static ReadMeCommand adornCommand = new AdornCommand();

    public static Line build(Line line) {

        createReadMeLine(annotationCommand, line);

        switch (line.getOption()) {
            case NOTING:
                break;
            case INSERT:
            case REPLACE:
                if (line.isModule()) {
                    createReadMeLine(moduleCommand, line);
                } else {
                    createReadMeLine(linkCommand,line);
                }
                break;
        }

        createReadMeLine(adornCommand,line);

        return line;
    }

    private static void createReadMeLine(ReadMeCommand command,Line line) {
        command.execute(line);
    }
}
