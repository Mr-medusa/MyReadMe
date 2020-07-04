package red.medusa.readme.command;

import red.medusa.readme.model.Line;

import static red.medusa.readme.model.MarkDownTag.BLANK_SPACE;

public class ModuleCommand extends ReadMeCommand {

    @Override
    public void execute(Line line) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < line.getModuleLevel(); i++) {
            sb.append("#");
        }
        sb.append(BLANK_SPACE).append("[").append(line.getModuleName()).append("]");
        sb.append("(").append(line.getLocation()).append(BLANK_SPACE).append("\"").append(line.getLocationTitle()).append("\"").append(")");
        sb.append(BLANK_SPACE).append(":").append(BLANK_SPACE);
        sb.append(line.getModuleMsg());
        line.setNewLine(sb.toString());
    }

}
