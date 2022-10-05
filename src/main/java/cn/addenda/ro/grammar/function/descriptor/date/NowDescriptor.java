package cn.addenda.ro.grammar.function.descriptor.date;

import cn.addenda.ro.data.DataTypeConst;
import cn.addenda.ro.grammar.ast.expression.Curd;
import cn.addenda.ro.grammar.ast.expression.CurdType;
import cn.addenda.ro.grammar.ast.expression.Function;
import cn.addenda.ro.grammar.ast.expression.Literal;
import cn.addenda.ro.grammar.function.descriptor.ErrorReportableFunctionDescriptor;
import cn.addenda.ro.grammar.function.descriptor.FunctionDescriptorROErrorReporterDelegate;
import cn.addenda.ro.grammar.lexical.token.TokenType;

import java.util.List;

/**
 * @author addenda
 * @datetime 2021/4/11 14:56
 */
public class NowDescriptor extends ErrorReportableFunctionDescriptor {

    @Override
    public boolean isIndependent() {
        return false;
    }

    @Override
    public String functionName() {
        return "now";
    }

    @Override
    public int innerType() {
        return DataTypeConst.DATE;
    }

    @Override
    public void staticCheck(Function function, CurdType type) {
        List<Curd> parameterList = function.getParameterList();
        if (parameterList == null || parameterList.isEmpty()) {
            return;
        }
        // 参数为一个的时候，参数必须是数字
        if (parameterList.size() == 1) {
            Curd curd = parameterList.get(0);
            if (curd instanceof Literal) {
                Literal literal = (Literal) curd;
                if (TokenType.INTEGER.equals(literal.getValue().getType())) {
                    return;
                }
            }
        }
        error(FunctionDescriptorROErrorReporterDelegate.FUNCTION_parameter_PARSE, function);
    }

}
