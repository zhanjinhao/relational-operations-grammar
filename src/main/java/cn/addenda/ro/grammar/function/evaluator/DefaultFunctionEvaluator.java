package cn.addenda.ro.grammar.function.evaluator;

import cn.addenda.ro.grammar.function.descriptor.FunctionDescriptor;
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

    private static final FunctionEvaluator<? extends FunctionDescriptor> instance = new DefaultFunctionEvaluator();

    public static FunctionEvaluator<? extends FunctionDescriptor> getInstance() {
        return instance;
    }

    private DefaultFunctionEvaluator() {
        addFunction(DateAddDescriptor.class);
        addFunction(DateFormatDescriptor.class);
        addFunction(DateSubDescriptor.class);
        addFunction(UnixTimestampDescriptor.class);
        addFunction(NowDescriptor.class);
        addFunction(StrToDateDescriptor.class);
        addFunction(FromUnixtimeDescriptor.class);
        addFunction(TimestampDiffDescriptor.class);
        addFunction(DateDescriptor.class);
        addFunction(ExtractDescriptor.class);

        addFunction(ConcatDescriptor.class);
        addFunction(ReplaceDescriptor.class);
        addFunction(SubstringDescriptor.class);

        addFunction(DecodeDescriptor.class);
        addFunction(IfDescriptor.class);
    }

}
