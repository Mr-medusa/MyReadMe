package red.medusa.readme.command;

import red.medusa.readme.model.Line;

import static red.medusa.readme.model.MarkDownTag.BLANK_SPACE;

public class ModuleCommand extends ReadMeCommand {

    public ModuleCommand(Line line) {
        super(line);
    }

    @Override
    public void execute() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < this.line.getModuleLevel(); i++) {
            sb.append("#");
        }
        sb.append(BLANK_SPACE).append("[").append(this.line.getModuleName()).append("]");
        sb.append("(").append(this.line.getLocation()).append(BLANK_SPACE).append("\"").append(this.line.getModuleMsg()).append("\"").append(")");
        sb.append(BLANK_SPACE).append(":").append(BLANK_SPACE);
        sb.append(this.line.getModuleMsg());
        this.line.setNewLine(sb.toString());
    }

}
