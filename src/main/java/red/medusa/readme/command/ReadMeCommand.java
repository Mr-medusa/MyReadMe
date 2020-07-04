package red.medusa.readme.command;

import red.medusa.readme.model.Line;

public abstract class ReadMeCommand {

    public abstract void execute(Line line);
}
