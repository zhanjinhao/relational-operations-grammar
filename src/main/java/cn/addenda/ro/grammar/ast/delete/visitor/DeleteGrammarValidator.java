package cn.addenda.ro.grammar.ast.delete.visitor;

import cn.addenda.ro.error.reporter.ROErrorReporter;
import cn.addenda.ro.grammar.ast.AstROErrorReporterDelegate;
import cn.addenda.ro.grammar.ast.delete.Delete;
import cn.addenda.ro.grammar.ast.expression.Curd;
import cn.addenda.ro.grammar.ast.expression.visitor.ExpressionGrammarValidator;
import cn.addenda.ro.grammar.ast.retrieve.Select;
import cn.addenda.ro.grammar.ast.retrieve.visitor.SelectGrammarValidator;
import cn.addenda.ro.grammar.lexical.token.Token;

/**
 * @author addenda
 * @datetime 2022/1/3 22:40
 */
public class DeleteGrammarValidator extends DeleteVisitorWithDelegate<Void> {

    public DeleteGrammarValidator(ROErrorReporter roErrorReporter) {
        super.init(new ExpressionGrammarValidator(this, roErrorReporter));
        setErrorReporter(roErrorReporter);
    }

    @Override
    public Void visitSelect(Select select) {
        select.accept(new SelectGrammarValidator(this));
        return null;
    }

    @Override
    public Void visitDelete(Delete delete) {
        Token tableName = delete.getTableName();
        if (tableName == null) {
            error(AstROErrorReporterDelegate.DELETE_delete_VALIDATION);
        }

        Curd whereSeg = delete.getWhereSeg();
        if (whereSeg != null) {
            whereSeg.accept(this);
        }

        return null;
    }

}
