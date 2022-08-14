package cn.addenda.ro.grammar.ast.create.visitor;

import cn.addenda.ro.error.reporter.DumbROErrorReporterDelegate;
import cn.addenda.ro.grammar.ast.AstMetaData;
import cn.addenda.ro.grammar.ast.AstMetaDataHelper;
import cn.addenda.ro.grammar.ast.create.*;
import cn.addenda.ro.grammar.ast.expression.AssignmentList;
import cn.addenda.ro.grammar.ast.expression.Curd;
import cn.addenda.ro.grammar.ast.expression.visitor.ExpressionAstMetaDataDetector;
import cn.addenda.ro.grammar.lexical.token.Token;

import java.util.List;

/**
 * @author addenda
 * @datetime 2022/8/13 21:05
 */
public class InsertAstMetaDataDetector extends InsertVisitorWithDelegate<AstMetaData> {

    public InsertAstMetaDataDetector() {
        super.init(new ExpressionAstMetaDataDetector(this));
        setErrorReporter(DumbROErrorReporterDelegate.getInstance());
    }

    @Override
    public AstMetaData visitInsert(Insert insert) {
        InsertSelectAstMetaData astMetaData = (InsertSelectAstMetaData) insert.getAstMetaData();

        Token tableName = insert.getTableName();
        astMetaData.putTableName(String.valueOf(tableName.getLiteral()));

        Curd insertRep = insert.getInsertRep();
        InsertSelectAstMetaData insertRepAstMetaData = (InsertSelectAstMetaData) insertRep.accept(this);
        astMetaData.mergeColumnReference(insertRepAstMetaData);

        List<Token> insertColumnList = insertRepAstMetaData.getInsertColumnList();
        astMetaData.getInsertColumnList().addAll(insertColumnList);

        Curd onDuplicateUpdate = insert.getOnDuplicateUpdate();
        if (onDuplicateUpdate != null) {
            InsertSelectAstMetaData onDuplicateUpdateAstMetaData = (InsertSelectAstMetaData) onDuplicateUpdate.accept(this);
            AstMetaDataHelper.mergeColumnReference(onDuplicateUpdateAstMetaData.getConditionColumnReference(), astMetaData.getOnDuplicateUpdateReference());
        }

        astMetaData.sortMetaData();

        return astMetaData;
    }

    @Override
    public AstMetaData visitInsertValuesRep(InsertValuesRep insertValuesRep) {

        InsertSelectAstMetaData astMetaData = (InsertSelectAstMetaData) insertValuesRep.getAstMetaData();

        List<Token> columnList = insertValuesRep.getColumnList();
        columnList.forEach(token -> astMetaData.getInsertColumnList().add(token));

        List<List<Curd>> curdListList = insertValuesRep.getCurdListList();
        for (List<Curd> curdList : curdListList) {
            for (Curd curd : curdList) {
                astMetaData.mergeColumnReference(curd.accept(this));
            }
        }

        return astMetaData;
    }

    @Override
    public AstMetaData visitInsertSetRep(InsertSetRep insertSetRep) {
        InsertSelectAstMetaData astMetaData = (InsertSelectAstMetaData) insertSetRep.getAstMetaData();

        InsertSelectAstMetaData assignmentListAstMetaData = (InsertSelectAstMetaData) insertSetRep.getAssignmentList().accept(this);
        astMetaData.mergeColumnReference(assignmentListAstMetaData);

        List<Token> insertColumnList = assignmentListAstMetaData.getInsertColumnList();
        astMetaData.getInsertColumnList().addAll(insertColumnList);

        insertColumnList.clear();

        return astMetaData;
    }

    @Override
    public AstMetaData visitOnDuplicateKey(OnDuplicateKey onDuplicateKey) {
        InsertSelectAstMetaData astMetaData = (InsertSelectAstMetaData) onDuplicateKey.getAstMetaData();

        Curd assignmentList = onDuplicateKey.getAssignmentList();
        InsertSelectAstMetaData assignmentListAstMetaData = (InsertSelectAstMetaData) assignmentList.accept(this);
        astMetaData.mergeColumnReference(assignmentListAstMetaData);

        List<Token> insertColumnList = assignmentListAstMetaData.getInsertColumnList();
        astMetaData.getInsertColumnList().addAll(insertColumnList);

        insertColumnList.clear();

        return astMetaData;
    }

    @Override
    public AstMetaData visitInsertSelectRep(InsertSelectRep insertSelectRep) {
        InsertSelectAstMetaData astMetaData = (InsertSelectAstMetaData) insertSelectRep.getAstMetaData();

        List<Token> columnList = insertSelectRep.getColumnList();
        columnList.forEach(token -> astMetaData.getInsertColumnList().add(token));

        Curd select = insertSelectRep.getSelect();
        AstMetaData selectAstMetaData = select.getAstMetaData();

        selectAstMetaData.setParent(astMetaData);
        astMetaData.addChild(selectAstMetaData);

        return astMetaData;
    }

    @Override
    public AstMetaData visitAssignmentList(AssignmentList assignmentList) {
        InsertSelectAstMetaData insertSelectAstMetaData = new InsertSelectAstMetaData();

        AstMetaData astMetaDataCur = assignmentList.getAstMetaData();
        List<AssignmentList.Entry> entryList = assignmentList.getEntryList();
        for (AssignmentList.Entry entry : entryList) {
            Token column = entry.getColumn();
            astMetaDataCur.mergeColumnReference(entry.getValue().accept(this));
            astMetaDataCur.putUndeterminedConditionColumn(String.valueOf(column.getLiteral()));

            insertSelectAstMetaData.getInsertColumnList().add(column);
        }

        insertSelectAstMetaData.mergeColumnReference(astMetaDataCur);
        return insertSelectAstMetaData;
    }

}
