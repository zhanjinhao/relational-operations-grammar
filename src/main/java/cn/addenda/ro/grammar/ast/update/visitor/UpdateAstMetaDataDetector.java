package cn.addenda.ro.grammar.ast.update.visitor;

import cn.addenda.ro.error.reporter.DumbROErrorReporterDelegate;
import cn.addenda.ro.grammar.ast.AstMetaData;
import cn.addenda.ro.grammar.ast.expression.AssignmentList;
import cn.addenda.ro.grammar.ast.expression.Curd;
import cn.addenda.ro.grammar.ast.expression.visitor.ExpressionAstMetaDataDetector;
import cn.addenda.ro.grammar.ast.update.Update;
import cn.addenda.ro.grammar.ast.update.UpdateAstMetaData;
import cn.addenda.ro.grammar.lexical.token.Token;

import java.util.List;

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
        UpdateAstMetaData assignmentListAmd = (UpdateAstMetaData) assignmentList.accept(this);
        astMetaData.mergeColumnReference(assignmentListAmd);

        List<Token> updateColumnList = assignmentListAmd.getUpdateColumnList();
        astMetaData.getUpdateColumnList().addAll(updateColumnList);
        updateColumnList.clear();

        Curd whereSeg = update.getWhereSeg();
        if (whereSeg != null) {
            astMetaData.mergeColumnReference(whereSeg.accept(this));
        }

        astMetaData.sortMetaData();

        return astMetaData;
    }


    @Override
    public AstMetaData visitAssignmentList(AssignmentList assignmentList) {
        UpdateAstMetaData updateAstMetaData = new UpdateAstMetaData();

        AstMetaData astMetaDataCur = assignmentList.getAstMetaData();
        List<AssignmentList.Entry> entryList = assignmentList.getEntryList();
        for (AssignmentList.Entry entry : entryList) {
            Token column = entry.getColumn();
            AstMetaData accept = entry.getValue().accept(this);

            astMetaDataCur.mergeColumnReference(accept);
            updateAstMetaData.mergeColumnReference(accept);

            astMetaDataCur.putUndeterminedConditionColumn(String.valueOf(column.getLiteral()));
            updateAstMetaData.getUpdateColumnList().add(column);
        }

        return updateAstMetaData;
    }
}
