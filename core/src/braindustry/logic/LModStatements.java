package braindustry.logic;

import arc.func.Cons;
import arc.graphics.Color;
import arc.scene.ui.Button;
import arc.scene.ui.ButtonGroup;
import arc.scene.ui.layout.Cell;
import arc.scene.ui.layout.Table;
import braindustry.annotations.ModAnnotations;
import braindustry.logic.LModExecutor.MessageReadI;
import braindustry.logic.LModExecutor.StrOpI;
import mindustry.graphics.Pal;
import mindustry.logic.LAssembler;
import mindustry.logic.LExecutor.LInstruction;
import mindustry.ui.Styles;

import static mindustry.logic.LCanvas.tooltip;

public class LModStatements {
    @ModAnnotations.RegisterStatement("braindustry_strop")
    public static class StringOperation extends LModStatement {
        public StrOp op = StrOp.add;
        public String dest = "result", a = "a", b = "b", c = "fromIndex";

        @Override
        public void build(Table table) {
            rebuild(table);
        }

        void rebuild(Table table) {
            table.clearChildren();

            field(table, dest, str -> dest = str);

            table.add(" = ");
            int rtype = (int) op.type;
            if (rtype == 1) {
                opButton(table, table);
                field(table, a, str -> a = str);
            } else if (rtype == 2) {
                field(table, a, str -> a = str);
                opButton(table, table);
                field(table, b, str -> b = str);
            } else if (rtype == 3) {
                opButton(table, table);
                field(table, a, str -> a = str);
                field(table, this.b, str -> this.b = str);
                row(table);
                field(table, c, str -> c = str);
            }
        }

        void opButton(Table table, Table parent) {
            table.button(b -> {
                b.label(() -> op.symbol);
                b.clicked(() -> showSelect(b, StrOp.all, op, o -> {
                    op = o;
                    rebuild(parent);
                }));
            }, Styles.logict, () -> {
            }).size(96.0f, 40.0f).pad(4f).color(table.color);
        }
        @Override
        protected <T extends Enum<T>> void showSelect(Button b, T[] values, T current, Cons<T> getter, int cols, Cons<Cell> sizer){
            showSelectTable(b, (t, hide) -> {
                ButtonGroup<Button> group = new ButtonGroup<>();
                int i = 0;
                t.defaults().size(60f*1.5f, 38f*1.5f);

                for(T p : values){
                    sizer.get(t.button(p.toString(), Styles.logicTogglet, () -> {
                        getter.get(p);
                        hide.run();
                    }).self(c -> tooltip(c, p)).checked(current == p).group(group));

                    if(++i % cols == 0) t.row();
                }
            });
        }
        @Override
        public LInstruction build(LAssembler builder) {
            return new StrOpI(op, builder.var(a), builder.var(b), builder.var(c), builder.var(dest));
        }

        @Override
        public Color color() {
            return Pal.logicOperations;
        }
    }

    @ModAnnotations.RegisterStatement("braindustry_messageread")
    public static class MessageRead extends LModStatement {
        public String dest = "result", messageBlock = "message1";

        @Override
        public void build(Table table) {
            rebuild(table);
        }

        void rebuild(Table table) {
            table.clearChildren();

            field(table, dest, str -> dest = str);

            row(table);
            table.add("text in ").self(this::param);

            field(table, messageBlock, str -> messageBlock = str);
        }

        @Override
        public LInstruction build(LAssembler builder) {
            return new MessageReadI(builder.var(messageBlock), builder.var(dest));
        }

        @Override
        public Color color() {
            return Pal.logicBlocks;
        }
    }
}
