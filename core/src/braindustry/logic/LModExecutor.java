package braindustry.logic;

import mindustry.ctype.Content;
import mindustry.logic.LExecutor;
import mindustry.world.blocks.logic.MessageBlock;

import static braindustry.logic.StrOp.*;

public class LModExecutor {
    public static class StrOpI implements LExecutor.LInstruction{
        public StrOp op = StrOp.add;
        public int a, b, c, dest;

        public StrOpI(StrOp op, int a, int b, int c, int dest){
            this.op = op;
            this.a = a;
            this.b = b;
            this.c = c;
            this.dest = dest;
        }

        StrOpI(){
        }

        private static Object objVal(LExecutor exec, int index){
            return obj(exec, index);
        }

        private static double numVal(LExecutor exec, int index){
            return exec.num(index);
        }

        private static String strVal(LExecutor exec, int index){
            Object obj = obj(exec, index);
            return LExecutor.PrintI.toString(obj);
        }

        private static Object obj(LExecutor exec, int b){
            return exec.var(b).isobj ? exec.obj(b) : exec.num(b);
        }

        private Object get(LExecutor exec, int value, int index){
            if(value == objVal){
                return obj(exec, index);
            }else if(value == numVal){
                return exec.num(index);
            }else if(value == strVal){
                Object obj = obj(exec, index);
                return LExecutor.PrintI.toString(obj);
            }
            return null;
        }

        @Override
        public void run(LExecutor exec){
            Object p1 = null, p2 = null, p3 = null;
            p1 = get(exec, op.type[0], a);
            p2 = get(exec, op.type[1], b);
            p3 = get(exec, op.type[2], c);
            if(p1 == null) return;
            Object result = op.func.get(p1, p2, p3);
            if(result instanceof Number){
                exec.setnum(dest, ((Number)result).doubleValue());
            }else if(result instanceof Boolean){
                exec.setbool(dest, (Boolean)result);
            }else{
                exec.setobj(dest, result);
            }
        }
    }

    public static class MessageReadI implements LExecutor.LInstruction{
        public int messageBlock, dest;

        public MessageReadI(int messageBlock, int dest){
            this.messageBlock = messageBlock;
            this.dest = dest;
        }

        MessageReadI(){
        }


        @Override
        public void run(LExecutor exec){
            LExecutor.Var var = exec.var(messageBlock);
            if(var.isobj && var.objval instanceof MessageBlock.MessageBuild){
                String message = ((MessageBlock.MessageBuild)var.objval).message.toString();
                exec.setobj(dest, message);
            }
        }
    }
}
