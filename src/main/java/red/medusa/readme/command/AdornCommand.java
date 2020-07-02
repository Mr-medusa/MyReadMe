package red.medusa.readme.command;

import red.medusa.readme.model.Line;

public class AdornCommand extends ReadMeCommand {

    public AdornCommand(Line line) {
        super(line);
    }

    @Override
    public void execute() {
        if (this.line.isModule()) {
            if (this.line.getAnnotation() != null && this.line.getAnnotation().getPre() != null) {
                this.line.getAnnotation().setPre(new Line("", ""));
            } else {
                if (this.line.getPre() == null) {       // 没有注解也生产一个
                    this.line.setPre(new Line("", ""));
                }
            }
        }
    }
}


















