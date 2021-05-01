package braindustry.gen;

import arc.func.Cons;
import arc.util.io.Reads;
import arc.util.io.Writes;
import braindustry.annotations.ModAnnotations;
import braindustry.versions.ModEntityc;
import mindustry.gen.Entityc;

final class NullModEntity implements ModEntityc {
  @Override
  @ModAnnotations.OverrideCallSuper
  public final <T> T as() {
    return null;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final <T extends Entityc> T self() {
    return null;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final <T> T with(Cons<T> arg0) {
    return null;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void add() {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void afterRead() {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final int classId() {
    return 0;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final int id() {
    return -1;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void id(int arg0) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final boolean isAdded() {
    return false;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final boolean isLocal() {
    return false;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final boolean isNull() {
    return true;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final boolean isRemote() {
    return false;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final int modClassId() {
    return 0;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void read(Reads arg0) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void remove() {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final boolean serialize() {
    return false;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void update() {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void write(Writes arg0) {
  }
}
