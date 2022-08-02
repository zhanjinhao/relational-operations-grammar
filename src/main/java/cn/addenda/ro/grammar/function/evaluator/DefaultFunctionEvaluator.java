package cn.addenda.ro.grammar.function.evaluator;

import cn.addenda.ro.grammar.function.descriptor.date.*;
import cn.addenda.ro.grammar.function.descriptor.logic.DecodeDescriptor;
import cn.addenda.ro.grammar.function.descriptor.logic.IfDescriptor;
import cn.addenda.ro.grammar.function.descriptor.string.ConcatDescriptor;
import cn.addenda.ro.grammar.function.descriptor.string.ReplaceDescriptor;
import cn.addenda.ro.grammar.function.descriptor.string.SubstringDescriptor;

/**
 * @author addenda
 * @datetime 2021/4/11 15:25
 */
public class DefaultFunctionEvaluator extends AbstractFunctionEvaluator {

    private static final FunctionEvaluator instance = new DefaultFunctionEvaluator();

    public static FunctionEvaluator getInstance() {
        return instance;
    }

    private DefaultFunctionEvaluator() {
        addFunctionDescriptor(DateAddDescriptor.class);
        addFunctionDescriptor(DateFormatDescriptor.class);
        addFunctionDescriptor(DateSubDescriptor.class);
        addFunctionDescriptor(UnixTimestampDescriptor.class);
        addFunctionDescriptor(NowDescriptor.class);
        addFunctionDescriptor(StrToDateDescriptor.class);
        addFunctionDescriptor(FromUnixtimeDescriptor.class);
        addFunctionDescriptor(TimestampDiffDescriptor.class);
        addFunctionDescriptor(DateDescriptor.class);
        addFunctionDescriptor(ExtractDescriptor.class);

        addFunctionDescriptor(ConcatDescriptor.class);
        addFunctionDescriptor(ReplaceDescriptor.class);
        addFunctionDescriptor(SubstringDescriptor.class);

        addFunctionDescriptor(DecodeDescriptor.class);
        addFunctionDescriptor(IfDescriptor.class);
    }

}
