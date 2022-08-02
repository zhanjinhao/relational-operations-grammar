package cn.addenda.ro.grammar.ast;

import cn.addenda.ro.grammar.ast.expression.*;
import cn.addenda.ro.grammar.ast.retrieve.ExistsCondition;
import cn.addenda.ro.grammar.ast.retrieve.InCondition;
import cn.addenda.ro.grammar.ast.retrieve.Select;
import cn.addenda.ro.grammar.ast.retrieve.SingleSelect;
import cn.addenda.ro.grammar.lexical.token.Token;
import cn.addenda.ro.grammar.lexical.token.TokenType;

/**
 * @author addenda
 * @datetime 2022/1/3 12:04
 */
public class LogicGrammarValidationDelegate {

    public static boolean checkBooleanResult(Curd curd) {
        if (curd == null) {
            return true;
        }
        if (curd instanceof Grouping) {
            Grouping grouping = (Grouping) curd;
            curd = grouping.getCurd();
        }
        if (curd instanceof Logic) {
            Logic logic = (Logic) curd;
            return checkBooleanResult(logic.getLeftCurd()) &&
                    checkBooleanResult(logic.getRightCurd());
        } else if (curd instanceof Comparison || curd instanceof ExistsCondition || curd instanceof InCondition) {
            return true;
        } else if (curd instanceof Identifier) {
            Identifier identifier = (Identifier) curd;
            Token name = identifier.getName();
            return TokenType.TRUE.equals(name.getType()) || TokenType.FALSE.equals(name.getType());
        }
        return !(curd instanceof SingleSelect);
    }

    public static boolean checkComparisonResult(Curd curd) {
        if (curd == null) {
            return true;
        }
        if (curd instanceof Logic) {
            Logic logic = (Logic) curd;
            return checkComparisonResult(logic.getLeftCurd()) &&
                    checkComparisonResult(logic.getRightCurd());
        } else if (curd instanceof Comparison) {
            return true;
        }
        return false;
    }

    public static boolean checkFunctionParameterResult(Curd curd) {
        if (curd == null) {
            return true;
        }
        if (curd instanceof Logic) {
            Logic logic = (Logic) curd;
            return checkFunctionParameterResult(logic.getLeftCurd()) &&
                    checkFunctionParameterResult(logic.getRightCurd());
        } else if (curd instanceof Select || curd instanceof SingleSelect) {
            return false;
        }
        return false;
    }

}
