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

    private static volatile InsertAstMetaDataDetector detector;

    private InsertAstMetaDataDetector() {
        super.init(new ExpressionAstMetaDataDetector(this));
        setErrorReporter(DumbROErrorReporterDelegate.getInstance());
    }

    public static InsertAstMetaDataDetector getInstance() {
        if (detector == null) {
            synchronized (InsertAstMetaDataDetector.class) {
                if (detector == null) {
                    detector = new InsertAstMetaDataDetector();
                }
            }
        }
        return detector;
    }

    @Override
    public AstMetaData visitInsert(Insert insert) {
        InsertAstMetaData astMetaData = (InsertAstMetaData) insert.getAstMetaData();

        Token tableName = insert.getTableName();
        astMetaData.putTableName(String.valueOf(tableName.getLiteral()));

        Curd insertRep = insert.getInsertRep();
        InsertAstMetaData insertRepAstMetaData = (InsertAstMetaData) insertRep.accept(this);
        astMetaData.mergeColumnReference(insertRepAstMetaData);

        List<Token> insertColumnList = insertRepAstMetaData.getInsertColumnList();
        astMetaData.getInsertColumnList().addAll(insertColumnList);

        Curd onDuplicateUpdate = insert.getOnDuplicateUpdate();
        if (onDuplicateUpdate != null) {
            InsertAstMetaData onDuplicateUpdateAstMetaData = (InsertAstMetaData) onDuplicateUpdate.accept(this);
            AstMetaDataHelper.mergeColumnReference(onDuplicateUpdateAstMetaData.getConditionColumnReference(), astMetaData.getOnDuplicateUpdateReference());
            astMetaData.mergeCount(onDuplicateUpdateAstMetaData);
        }

        astMetaData.sortMetaData();

        return astMetaData;
    }

    @Override
    public AstMetaData visitInsertValuesRep(InsertValuesRep insertValuesRep) {

        InsertAstMetaData astMetaData = (InsertAstMetaData) insertValuesRep.getAstMetaData();

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
        InsertAstMetaData astMetaData = (InsertAstMetaData) insertSetRep.getAstMetaData();

        InsertAstMetaData assignmentListAstMetaData = (InsertAstMetaData) insertSetRep.getAssignmentList().accept(this);
        astMetaData.mergeColumnReference(assignmentListAstMetaData);

        List<Token> insertColumnList = assignmentListAstMetaData.getInsertColumnList();
        astMetaData.getInsertColumnList().addAll(insertColumnList);

        insertColumnList.clear();

        return astMetaData;
    }

    @Override
    public AstMetaData visitOnDuplicateKey(OnDuplicateKey onDuplicateKey) {
        InsertAstMetaData astMetaData = (InsertAstMetaData) onDuplicateKey.getAstMetaData();

        Curd assignmentList = onDuplicateKey.getAssignmentList();
        InsertAstMetaData assignmentListAstMetaData = (InsertAstMetaData) assignmentList.accept(this);
        astMetaData.mergeColumnReference(assignmentListAstMetaData);

        List<Token> insertColumnList = assignmentListAstMetaData.getInsertColumnList();
        astMetaData.getInsertColumnList().addAll(insertColumnList);

        insertColumnList.clear();

        return astMetaData;
    }

    @Override
    public AstMetaData visitInsertSelectRep(InsertSelectRep insertSelectRep) {
        InsertAstMetaData astMetaData = (InsertAstMetaData) insertSelectRep.getAstMetaData();

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
        InsertAstMetaData insertAstMetaData = new InsertAstMetaData();

        AstMetaData astMetaDataCur = assignmentList.getAstMetaData();
        List<AssignmentList.Entry> entryList = assignmentList.getEntryList();
        for (AssignmentList.Entry entry : entryList) {
            Token column = entry.getColumn();
            AstMetaData accept = entry.getValue().accept(this);

            astMetaDataCur.mergeColumnReference(accept);
            insertAstMetaData.mergeColumnReference(accept);

            astMetaDataCur.putUndeterminedConditionColumn(String.valueOf(column.getLiteral()));
            insertAstMetaData.getInsertColumnList().add(column);
        }

        return insertAstMetaData;
    }

}
