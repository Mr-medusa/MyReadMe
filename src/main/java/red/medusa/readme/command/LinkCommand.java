package red.medusa.readme.command;

import red.medusa.readme.model.Line;

import static red.medusa.readme.model.MarkDownTag.BLANK_SPACE;

public class LinkCommand extends ReadMeCommand {

    @Override
    public void execute(Line line) {
        StringBuilder sb = new StringBuilder();
        sb.append("+").append(BLANK_SPACE).append("[").append(line.getMethodName()).append("]");
        sb.append("(").append(line.getLocation()).append(BLANK_SPACE).append("\"").append(line.getLocationTitle()).append("\"").append(")");
        sb.append(BLANK_SPACE).append(":").append(BLANK_SPACE).append(line.getMethodUsage());
        line.setNewLine(sb.toString());
    }
}


















