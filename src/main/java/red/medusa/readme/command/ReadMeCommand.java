package red.medusa.readme.command;

import red.medusa.readme.model.Line;

public abstract class ReadMeCommand {
    protected Line line;

    public ReadMeCommand(Line line) {
        this.line = line;
    }

    public abstract void execute();
}
