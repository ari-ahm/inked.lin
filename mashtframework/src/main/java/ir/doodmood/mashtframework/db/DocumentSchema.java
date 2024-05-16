package ir.doodmood.mashtframework.db;

public class DocumentSchema {
    private String key;
    private Object value;

    public DocumentSchema(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public Object getValue() {
        return value;
    }
}
