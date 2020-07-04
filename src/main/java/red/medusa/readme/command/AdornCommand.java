package red.medusa.readme.command;

import red.medusa.readme.model.Line;

public class AdornCommand extends ReadMeCommand {

    @Override
    public void execute(Line line) {
        if (line.isModule()) {
            if (line.getAnnotation() != null && line.getAnnotation().getPre() != null) {
                line.getAnnotation().setPre(new Line("", ""));
            } else {
                if (line.getPre() == null) {       // 没有注解也生产一个
                    line.setPre(new Line("", ""));
                }
            }
        }
    }
}


















