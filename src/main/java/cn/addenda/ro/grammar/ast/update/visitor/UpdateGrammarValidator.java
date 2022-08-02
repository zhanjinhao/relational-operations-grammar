package cn.addenda.ro.grammar.ast.update.visitor;

import cn.addenda.ro.error.reporter.ROErrorReporter;
import cn.addenda.ro.grammar.ast.AstROErrorReporterDelegate;
import cn.addenda.ro.grammar.ast.expression.Curd;
import cn.addenda.ro.grammar.ast.expression.visitor.ExpressionGrammarValidator;
import cn.addenda.ro.grammar.ast.update.Update;

/**
 * @author addenda
 * @datetime 2022/1/3 11:57
 */
public class UpdateGrammarValidator extends UpdateVisitorWithDelegate<Void> {

    public UpdateGrammarValidator(ROErrorReporter roErrorReporter) {
        super.init(new ExpressionGrammarValidator(this, roErrorReporter));
        setErrorReporter(roErrorReporter);
    }

    @Override
    public Void visitUpdate(Update update) {
        Curd assignmentList = update.getAssignmentList();
        if (assignmentList == null) {
            error(AstROErrorReporterDelegate.UPDATE_update_VALIDATION);
        } else {
            assignmentList.accept(this);
        }

        Curd whereSeg = update.getWhereSeg();
        if (whereSeg != null) {
            whereSeg.accept(this);
        }
        return null;
    }

}
