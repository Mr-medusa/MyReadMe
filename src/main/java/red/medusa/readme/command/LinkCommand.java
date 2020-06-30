package red.medusa.readme.command;

import red.medusa.readme.model.Line;

import static red.medusa.readme.model.MarkDownTag.BLANK_SPACE;

public class LinkCommand extends ReadMeCommand {

    public LinkCommand(Line line) {
        super(line);
    }

    @Override
    public void execute() {
        // [ClassLoader资源](../classpath/ClassLoaderResourceUtils.java "url资源")
        StringBuilder sb = new StringBuilder();
        sb.append("+").append(BLANK_SPACE).append("[").append(this.line.getMethodName()).append("]");
        sb.append("(").append(this.line.getLocation()).append(BLANK_SPACE).append("\"").append(this.line.getLocationTitle()).append("\"").append(")");
        sb.append(BLANK_SPACE).append(":").append(BLANK_SPACE).append(this.line.getNewLine());
        this.line.setNewLine(sb.toString());
    }
}

















