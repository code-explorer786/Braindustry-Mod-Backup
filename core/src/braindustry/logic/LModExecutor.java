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

        @Override
        public void run(LExecutor exec){
            Object result = op.func.get(exec.var(a), exec.var(b), exec.var(c));
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
