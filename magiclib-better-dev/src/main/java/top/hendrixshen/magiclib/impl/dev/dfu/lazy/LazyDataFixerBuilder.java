package top.hendrixshen.magiclib.impl.dev.dfu.lazy;

import com.mojang.datafixers.DataFixer;
import com.mojang.datafixers.DataFixerBuilder;

import java.util.concurrent.Executor;

public class LazyDataFixerBuilder extends DataFixerBuilder {
    private static final Executor NO_OP_EXECUTOR = command -> {
    };

    public LazyDataFixerBuilder(int dataVersion) {
        super(dataVersion);
    }

    @Override
    public DataFixer build(Executor executor) {
        return super.build(LazyDataFixerBuilder.NO_OP_EXECUTOR);
    }
}
