package braindustry.logic;

import mindustry.logic.LExecutor;
import mindustry.world.blocks.logic.MessageBlock;

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

        @Override
        public void run(LExecutor exec) {
            float type = op.type;
            Object p1 = null, p2 = null, p3 = null;
            if (type == 1f) {
                p1 = exec.num(a);
            } else if (type == 1.1f) {
                p1 = (obj(exec, a)) + "";
            } else if (type == 2f) {
                p1 = (obj(exec, a));
                p2 = (obj(exec, b));
            } else if (type == 2.1f) {
                p1 = (obj(exec, a)) + "";
                p2 = exec.num(b);
            } else  if (type == 2.2f) {
                p1 = obj(exec, a) + "";
                p2 = obj(exec, b)+"";
            } else if (type == 3f) {
                p1 = (obj(exec, a)) + "";
                p2 = exec.num(b);
                p3 = exec.num(c);
            } else if (type == 3.2f) {
                p1 = (obj(exec, a)) + "";
                p2 = obj(exec, b) + "";
                p3 = exec.num(c);
            } else  if (type == 3.3f) {
                p1 = (obj(exec, a)) + "";
                p2 = obj(exec, b) + "";
                p3 = exec.num(c)+"";
            } else {
                return;
            }
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
