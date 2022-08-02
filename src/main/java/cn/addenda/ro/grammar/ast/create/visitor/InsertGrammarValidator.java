package cn.addenda.ro.grammar.ast.create.visitor;

import cn.addenda.ro.error.reporter.ROErrorReporter;
import cn.addenda.ro.grammar.ast.AstROErrorReporterDelegate;
import cn.addenda.ro.grammar.ast.create.*;
import cn.addenda.ro.grammar.ast.expression.Curd;
import cn.addenda.ro.grammar.ast.expression.visitor.ExpressionGrammarValidator;
import cn.addenda.ro.grammar.ast.retrieve.Select;
import cn.addenda.ro.grammar.ast.retrieve.visitor.SelectGrammarValidator;
import cn.addenda.ro.grammar.lexical.token.Token;

import java.util.List;

/**
 * @author addenda
 * @datetime 2022/1/3 22:45
 */
public class InsertGrammarValidator extends InsertVisitorWithDelegate<Void> {

    public InsertGrammarValidator(ROErrorReporter roErrorReporter) {
        super.init(new ExpressionGrammarValidator(this, roErrorReporter));
        setErrorReporter(roErrorReporter);
    }

    @Override
    public Void visitInsert(Insert insert) {

        Curd curd = insert.getCurd();
        Token constrict = insert.getConstrict();
        Curd onDuplicateUpdate = insert.getOnDuplicateUpdate();

        if (curd instanceof InsertSelectRep && onDuplicateUpdate != null) {
            error(AstROErrorReporterDelegate.INSERT_insert_VALIDATION);
        }

        if (constrict != null && onDuplicateUpdate != null) {
            error(AstROErrorReporterDelegate.INSERT_insert_VALIDATION);
        }

        if (curd instanceof Select) {

        }
        curd.accept(this);

        if (onDuplicateUpdate != null) {
            onDuplicateUpdate.accept(this);
        }

        return null;
    }

    @Override
    public Void visitInsertValuesRep(InsertValuesRep insertValuesRep) {
        List<Token> columnList = insertValuesRep.getColumnList();
        List<List<Curd>> curdListList = insertValuesRep.getCurdListList();
        int columnSize = columnList.size();
        for (List<Curd> curdList : curdListList) {
            if (curdList.size() != columnSize) {
                error(AstROErrorReporterDelegate.INSERT_insertValuesRep_VALIDATION);
            }
            for (Curd curd : curdList) {
                curd.accept(this);
            }
        }
        return null;
    }

    @Override
    public Void visitInsertSetRep(InsertSetRep insertSetRep) {
        Curd entryList = insertSetRep.getEntryList();
        if (entryList == null) {
            error(AstROErrorReporterDelegate.INSERT_insertSetRep_VALIDATION);
        } else {
            entryList.accept(this);
        }
        return null;
    }

    @Override
    public Void visitOnDuplicateKey(OnDuplicateKey onDuplicateKey) {
        Curd curd = onDuplicateKey.getCurd();
        if (curd == null) {
            error(AstROErrorReporterDelegate.INSERT_onDuplicateKey_VALIDATION);
        } else {
            curd.accept(this);
        }
        return null;
    }

    @Override
    public Void visitInsertSelectRep(InsertSelectRep insertSelectRep) {
        Curd select = insertSelectRep.getSelect();
        if (select == null) {
            error(AstROErrorReporterDelegate.INSERT_insertSelectRep_VALIDATION);
        } else {
            select.accept(new SelectGrammarValidator(this));
        }
        return null;
    }

}
