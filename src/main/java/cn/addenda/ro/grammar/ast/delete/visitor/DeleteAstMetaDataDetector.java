package cn.addenda.ro.grammar.ast.delete.visitor;

import cn.addenda.ro.error.reporter.DumbROErrorReporterDelegate;
import cn.addenda.ro.grammar.ast.AstMetaData;
import cn.addenda.ro.grammar.ast.delete.Delete;
import cn.addenda.ro.grammar.ast.delete.DeleteAstMetaData;
import cn.addenda.ro.grammar.ast.expression.Curd;
import cn.addenda.ro.grammar.ast.expression.visitor.ExpressionAstMetaDataDetector;
import cn.addenda.ro.grammar.lexical.token.Token;

/**
 * @author addenda
 * @datetime 2022/8/13 18:20
 */
public class DeleteAstMetaDataDetector extends DeleteVisitorWithDelegate<AstMetaData> {

    public DeleteAstMetaDataDetector() {
        super.init(new ExpressionAstMetaDataDetector(this));
        setErrorReporter(DumbROErrorReporterDelegate.getInstance());
    }

    @Override
    public AstMetaData visitDelete(Delete delete) {

        DeleteAstMetaData astMetaData = (DeleteAstMetaData) delete.getAstMetaData();

        Token tableName = delete.getTableName();

        astMetaData.putTableName(String.valueOf(tableName.getLiteral()));

        Curd whereSeg = delete.getWhereSeg();
        if (whereSeg != null) {
            astMetaData.mergeColumnReference(whereSeg.accept(this));
        }

        astMetaData.sortMetaData();

        return astMetaData;
    }

}
