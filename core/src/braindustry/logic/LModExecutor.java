package braindustry.logic;

import mindustry.ctype.Content;
import mindustry.logic.LExecutor;
import mindustry.world.blocks.logic.MessageBlock;

import static braindustry.logic.StrOp.*;

public class LModExecutor {
    public static class StrOpI implements LExecutor.LInstruction {
        public StrOp op = StrOp.add;
        public int a, b, c, dest;

        public StrOpI(StrOp op, int a, int b, int c, int dest) {
            this.op = op;
            this.a = a;
            this.b = b;
            this.c = c;
            this.dest = dest;
        }

        StrOpI() {
        }

        private Object set(LExecutor exec, int value, int index) {
            if (value == objVal) {
                return obj(exec, index);
            } else if (value == numVal) {
                return exec.num(index);
            } else if (value == strVal) {
                Object obj = obj(exec, index);
                return obj + "";
            }
            return null;
        }

        @Override
        public void run(LExecutor exec) {
            Object p1 = null, p2 = null, p3 = null;
            p1 = set(exec, op.type[0], a);
            p2 = set(exec, op.type[1], b);
            p3 = set(exec, op.type[2], c);
            if (p1 == null) return;
            Object result = op.func.get(p1, p2, p3);
            double value;
            if (result instanceof Double || result instanceof Float || result instanceof Integer || result instanceof Long) {
                if (result instanceof Double) {
                    value = (double) result;
                } else if (result instanceof Float) {
                    value = (float) result;
                } else if (result instanceof Integer) {
                    value = (int) result;
                } else {
                    value = (long) result;
                }
                exec.setnum(dest, value);
            } else if (result instanceof Boolean) {
                exec.setbool(dest, (Boolean) result);
            } else {
                exec.setobj(dest, result);
            }
        }

        private Object obj(LExecutor exec, int b) {
            return exec.var(b).isobj ? exec.obj(b) : exec.num(b);
        }
    }

    public static class MessageReadI implements LExecutor.LInstruction {
        public int messageBlock, dest;

        public MessageReadI(int messageBlock, int dest) {
            this.messageBlock = messageBlock;
            this.dest = dest;
        }

        MessageReadI() {
        }


        @Override
        public void run(LExecutor exec) {
            LExecutor.Var var = exec.var(messageBlock);
            if (var.isobj && var.objval instanceof MessageBlock.MessageBuild) {
                String message = ((MessageBlock.MessageBuild) var.objval).message.toString();
                exec.setobj(dest, message);
            }
        }
    }
}
