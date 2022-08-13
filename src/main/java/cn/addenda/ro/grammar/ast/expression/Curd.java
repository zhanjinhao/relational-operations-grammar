package cn.addenda.ro.grammar.ast.expression;

import cn.addenda.ro.grammar.ast.*;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author addenda
 * @datetime 2021/3/1 11:35
 */
public abstract class Curd {

    private AstMetaData astMetaData;

    private final CurdPrinter curdPrinter = new CurdPrinter();

    private static final DeepCloneVisitor deepCloneVisitor = new DeepCloneVisitor();

    private static final Map<String, IdentifierFillTNVisitor> tNToIdentifierFillTNMap = new ConcurrentHashMap<>();

    protected Curd() {
        this.astMetaData = new AstMetaData();
        astMetaData.setCurd(this);
    }

    protected Curd(AstMetaData astMetaData) {
        this.astMetaData = astMetaData;
        astMetaData.setCurd(this);
    }

    public abstract <R> R accept(CurdVisitor<R> curdVisitor);

    @Override
    public String toString() {
        return this.accept(curdPrinter);
    }

    public AstMetaData getAstMetaData() {
        return astMetaData;
    }

    public Curd deepClone() {
        return this.accept(deepCloneVisitor);
    }

    public void fillTableName(String tableName) {
        IdentifierFillTNVisitor identifierFillTNVisitor =
                tNToIdentifierFillTNMap.computeIfAbsent(tableName, s -> new IdentifierFillTNVisitor(tableName));
        this.accept(identifierFillTNVisitor);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return Objects.equals(toString().replaceAll("\\s+", " "), o.toString().replaceAll("\\s+", " "));
    }

    @Override
    public int hashCode() {
        return Objects.hash(toString().replaceAll("\\s+", ""));
    }
}
