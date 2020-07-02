package red.medusa.readme.model;

import java.util.*;

public class ReadMeModuleList implements Iterable<ReadMeModule> {

    private Map<String, ReadMeModule> moduleMap = new LinkedHashMap<>(8);

    private static class SingletonClassInstance {
        private static final ReadMeModuleList instance = new ReadMeModuleList();
    }

    public static ReadMeModuleList getInstance() {
        return SingletonClassInstance.instance;
    }

    private ReadMeModuleList() {
        ReadMeModule readMeModule = new ReadMeModule(MarkDownTag.SEPARATOR);
        this.moduleMap.put(readMeModule.getModuleName(), readMeModule);
    }

    public ReadMeModule getOrPutWhenNotFind(ReadMeModule module) {
        return moduleMap.computeIfAbsent(module.getModuleName(), k -> module);
    }

    public int size() {
        return moduleMap.size();
    }

    @Override
    public Iterator<ReadMeModule> iterator() {
        return moduleMap.values().iterator();
    }

    public Collection<ReadMeModule> list() {
        return moduleMap.values();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Set<Map.Entry<String, ReadMeModule>> entries = this.moduleMap.entrySet();
        for (Map.Entry<String, ReadMeModule> entry : entries) {
            sb.append("\n").append(entry.getKey()).append(" => [\n");
            for (Line line : entry.getValue().getLines()) {
                sb.append(line.getPre()).append("\n");
                sb.append(line).append("\n");
            }
            sb.append("]");
        }
        return sb.toString();
    }
}
