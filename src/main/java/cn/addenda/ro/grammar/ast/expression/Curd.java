package cn.addenda.ro.grammar.ast.expression;

import cn.addenda.ro.grammar.DeepCloneable;
import cn.addenda.ro.grammar.ast.*;
import cn.addenda.ro.grammar.ast.create.InsertAstMetaData;
import cn.addenda.ro.grammar.ast.create.visitor.InsertAstMetaDataDetector;
import cn.addenda.ro.grammar.ast.delete.DeleteAstMetaData;
import cn.addenda.ro.grammar.ast.delete.visitor.DeleteAstMetaDataDetector;
import cn.addenda.ro.grammar.ast.expression.visitor.ExpressionAstMetaDataDetector;
import cn.addenda.ro.grammar.ast.retrieve.SelectAstMetaData;
import cn.addenda.ro.grammar.ast.retrieve.SingleSelectAstMetaData;
import cn.addenda.ro.grammar.ast.retrieve.visitor.SelectAstMetaDataDetector;
import cn.addenda.ro.grammar.ast.update.UpdateAstMetaData;
import cn.addenda.ro.grammar.ast.update.visitor.UpdateAstMetaDataDetector;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author addenda
 * @datetime 2021/3/1 11:35
 */
public abstract class Curd implements DeepCloneable<Curd> {

    private AstMetaData astMetaData;

    private final CurdPrinter curdPrinter = new CurdPrinter();

    private static final DeepCloneVisitor DEEP_CLONE_VISITOR = new DeepCloneVisitor();

    private final CurdVisitor<AstMetaData> detector;

    private static final ClearAstMetaDataVisitor CLEAR_AST_META_DATA_VISITOR = new ClearAstMetaDataVisitor();

    private static final Map<String, IdentifierFillTNVisitor> T_N_TO_IDENTIFIER_FILL_TN_MAP = new ConcurrentHashMap<>();

    protected Curd() {
        this.astMetaData = new AstMetaData();
        this.astMetaData.setCurd(this);
        this.detector = ExpressionAstMetaDataDetector.getDefaultInstance();
    }

    protected Curd(AstMetaData astMetaData) {
        this.astMetaData = astMetaData;
        this.astMetaData.setCurd(this);
        if (astMetaData instanceof InsertAstMetaData) {
            this.detector = InsertAstMetaDataDetector.getInstance();
        } else if (astMetaData instanceof DeleteAstMetaData) {
            this.detector = DeleteAstMetaDataDetector.getInstance();
        } else if (astMetaData instanceof UpdateAstMetaData) {
            this.detector = UpdateAstMetaDataDetector.getInstance();
        } else if (astMetaData instanceof SelectAstMetaData || astMetaData instanceof SingleSelectAstMetaData) {
            this.detector = SelectAstMetaDataDetector.getInstance();
        } else {
            this.detector = ExpressionAstMetaDataDetector.getDefaultInstance();
        }
    }

    public abstract <R> R accept(CurdVisitor<R> curdVisitor);

    @Override
    public String toString() {
        return this.accept(curdPrinter);
    }

    public String toString(String separator) {
        return this.accept(new CurdPrinter(separator));
    }

    public AstMetaData getAstMetaData() {
        return astMetaData;
    }

    @Override
    public Curd deepClone() {
        Curd accept = this.accept(DEEP_CLONE_VISITOR);
        accept.detectAstMetaData();
        return accept;
    }

    public void fillTableName(String tableName) {
        IdentifierFillTNVisitor identifierFillTNVisitor =
                T_N_TO_IDENTIFIER_FILL_TN_MAP.computeIfAbsent(tableName, s -> new IdentifierFillTNVisitor(tableName));
        this.accept(identifierFillTNVisitor);
    }

    public void setAstMetaData(AstMetaData astMetaData) {
        this.astMetaData = astMetaData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        return Objects.equals(toString().replaceAll("\\s+", " "), o.toString().replaceAll("\\s+", " "));
    }

    @Override
    public int hashCode() {
        return Objects.hash(toString().replaceAll("\\s+", ""));
    }

    public void detectAstMetaData() {
        this.accept(detector);
    }

    public void reSetAstMetaData() {
        this.accept(CLEAR_AST_META_DATA_VISITOR);
        this.accept(detector);
    }

}
