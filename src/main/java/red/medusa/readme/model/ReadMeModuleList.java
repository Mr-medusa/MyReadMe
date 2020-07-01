package red.medusa.readme.model;

import java.util.*;

public class ReadMeModuleList implements Iterable<ReadMeModule> {

    private LinkedList<ReadMeModule> moduleList = new LinkedList<>();

    private Map<String, ReadMeModule> moduleMap = new LinkedHashMap<>(8);

    private static class SingletonClassInstance{
        private static final ReadMeModuleList instance=new ReadMeModuleList();
    }

    public static ReadMeModuleList getInstance(){
        return SingletonClassInstance.instance;
    }

    private ReadMeModuleList() {
        this.addModule(new ReadMeModule(MarkDownTag.SEPARATOR,false));
    }

    public ReadMeModuleList addModule(ReadMeModule module) {
        this.moduleList.add(module);
        this.moduleMap.put(module.getModuleName(), module);
        return this;
    }

    public ReadMeModuleList removeModule(ReadMeModule module) {
        this.moduleList.remove(module);
        this.moduleMap.remove(module.getModuleName());
        return this;
    }

    public ReadMeModuleList addLine(ReadMeModule module, Line line) {
        moduleMap.computeIfAbsent(module.getModuleName(), k ->         // 若找不到使用默认的
                moduleMap.computeIfAbsent(MarkDownTag.SEPARATOR, ReadMeModule::new))
                .getLines().add(line);
        return this;
    }

    public void shift(ReadMeModule module, int order) {
        moduleList.remove(module);
        if (order > 0 && order <= moduleList.size()) {
            moduleList.add(order - 1, module);
        } else if (!moduleList.contains(module)) {
            moduleList.add(module);
        }
    }

    public ReadMeModule get(ReadMeModule module) {
        return moduleMap.computeIfAbsent(module.getModuleName(), k -> new ReadMeModule(module.getModuleName()));
    }

    public int size() {
        return moduleList.size();
    }

    @Override
    public Iterator<ReadMeModule> iterator() {
        return moduleList.iterator();
    }


}
