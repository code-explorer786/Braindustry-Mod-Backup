package braindustry.entities.compByAnuke;
import arc.math.Mathf;
import mindustry.gen.Posc;
import mindustry.type.Item;
import mindustry.type.ItemStack;

import arc.math.*;
import mindustry.annotations.Annotations.*;
import mindustry.gen.*;
import mindustry.type.*;

@braindustry.annotations.ModAnnotations.Component
abstract class ItemsComp implements Posc{
    ItemStack stack = new ItemStack();
    transient float itemTime;

    abstract int itemCapacity();
   public void update() {
      this.stack.amount = Mathf.clamp(this.stack.amount, 0, this.itemCapacity());
      this.itemTime = Mathf.lerpDelta(this.itemTime, (float)Mathf.num(this.hasItem()), 0.05F);
   }

   Item item() {
      return this.stack.item;
   }

   void clearItem() {
      this.stack.amount = 0;
   }

   boolean acceptsItem(Item item) {
      return !this.hasItem() || item == this.stack.item && this.stack.amount + 1 <= this.itemCapacity();
   }

   boolean hasItem() {
      return this.stack.amount > 0;
   }

   void addItem(Item item) {
      this.addItem(item, 1);
   }

   void addItem(Item item, int amount) {
      this.stack.amount = this.stack.item == item ? this.stack.amount + amount : amount;
      this.stack.item = item;
      this.stack.amount = Mathf.clamp(this.stack.amount, 0, this.itemCapacity());
   }

   int maxAccepted(Item item) {
      return this.stack.item != item && this.stack.amount > 0 ? 0 : this.itemCapacity() - this.stack.amount;
   }
}