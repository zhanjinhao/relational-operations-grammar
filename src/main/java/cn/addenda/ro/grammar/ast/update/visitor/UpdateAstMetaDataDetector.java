package cn.addenda.ro.grammar.ast.update.visitor;

import cn.addenda.ro.error.reporter.DumbROErrorReporterDelegate;
import cn.addenda.ro.grammar.ast.AstMetaData;
import cn.addenda.ro.grammar.ast.expression.Curd;
import cn.addenda.ro.grammar.ast.expression.visitor.ExpressionAstMetaDataDetector;
import cn.addenda.ro.grammar.ast.update.Update;
import cn.addenda.ro.grammar.ast.update.UpdateAstMetaData;
import cn.addenda.ro.grammar.lexical.token.Token;

/**
 * @author addenda
 * @datetime 2022/8/13 19:15
 */
public class UpdateAstMetaDataDetector extends UpdateVisitorWithDelegate<AstMetaData> {

    public UpdateAstMetaDataDetector() {
        super.init(new ExpressionAstMetaDataDetector(this));
        setErrorReporter(DumbROErrorReporterDelegate.getInstance());
    }

    @Override
    public AstMetaData visitUpdate(Update update) {
        UpdateAstMetaData astMetaData = (UpdateAstMetaData) update.getAstMetaData();

        Token tableName = update.getTableName();
        astMetaData.putTableName(String.valueOf(tableName.getLiteral()));

        Curd assignmentList = update.getAssignmentList();
        AstMetaData accept = assignmentList.accept(this);
        astMetaData.mergeColumnReference(accept);

        Curd whereSeg = update.getWhereSeg();
        if (whereSeg != null) {
            astMetaData.mergeColumnReference(whereSeg.accept(this));
        }

        astMetaData.sortMetaData();

        return astMetaData;
    }


}
