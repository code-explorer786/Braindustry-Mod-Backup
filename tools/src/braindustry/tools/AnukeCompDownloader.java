package braindustry.tools;

import arc.files.Fi;
import arc.files.ZipFi;
import arc.struct.ObjectMap;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.Strings;
import arc.util.Time;
import org.apache.commons.io.FileUtils;
import org.jetbrains.java.decompiler.main.decompiler.ConsoleDecompiler;
import org.jetbrains.java.decompiler.main.decompiler.PrintStreamLogger;
import org.jetbrains.java.decompiler.main.extern.IFernflowerPreferences;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AnukeCompDownloader {
    public static void main(String[] args) {
        try {
            long nanos = System.nanoTime();
            Fi dir = new Fi("core/src/braindustry/entities/compByAnuke");
            if (dir.exists()) dir.delete();
//            dir.mkdirs();
            Fi folder = new Fi("debug");
            folder.mkdirs();
            Fi compJava = folder.child("compJava");
            Fi compClass = folder.child("compClass");
            Fi compDecompiled = folder.child("compDecompiled");
            Fi finalComp = folder.child("finalComp");
            boolean debug = true, log = false;

//        URL sourceZipLink = ;https://codeload.github.com/Anuken/Mindustry/zip/refs/tags/v126.2

            if (!debug || !folder.child("sources.zip").exists())
                FileUtils.copyURLToFile(new URL("https://codeload.github.com/Anuken/Mindustry/zip/refs/tags/v126.2"), folder.child("sources.zip").file(), 10000, 10000);

            ZipFi sourceZip = new ZipFi(folder.child("sources.zip"));

            Fi child = sourceZip.list()[0].child("core").child("src").child("mindustry").child("entities").child("comp");
            for (Fi fi : child.list()) {
                fi.copyTo(compJava.child(fi.name()));
            }
            for (Fi fi : compJava.list()) {
//            Log.info("@",fi);
            }
//        String serverLink = "https://w3g3a5v6.ssl.hwcdn.net/build/377240/1442306-archive-default?GoogleAccessId=uploader@moonscript2.iam.gserviceaccount.com&Expires=1624227195&Signature=bqM%2BK32GOMC7d1pA2KOd6xPjykHoyGHESSD5wraKzEnK%2BZf096h3lekhXg0615y0IkYIC%2BsqHVbDuOePan74h5TN42ZI0Jwo6UU%2B8%2FFVt%2BUUzVzfj8jhDZXPurs802PqNic7u1H9RdjnAl2ad2o98kBVYdOtuDc2v2KhhddnqT4dLxswIV1RdQjCtBtCnoJpt6wjWKL0Mo3RgiekHdcoswkZK1JlF%2B2fDfGzqRGHIBIVpU8yRpVNb8z3G%2BuO5u3jzVEHgoCnJr8glKMfacd9Wkoo%2Bn02EY0d0LZyO0lo3QuOBcA9AF%2FetLWfdtullTb2wpOO5ZbD1eb8KKN7J9DPWw==&hwexp=1624227455&hwsig=c472a60f0541e4cd5a1c2382fc52f0ce";
            String serverLink = "https://github.com/Anuken/Mindustry/releases/download/v126.2/server-release.jar";
            if (!debug || !folder.child("server.jar").exists()) {
                FileUtils.copyURLToFile(new URL(serverLink), folder.child("server.jar").file(), 10000, 10000);
            }
            ZipFi serverZip = new ZipFi(folder.child("server.jar"));
            for (Fi fi : serverZip.child("mindustry").child("entities").child("comp").list()) {
                fi.copyTo(compClass.child(fi.name()));
            }
            if (!compDecompiled.exists()) {
                PrintStreamLogger logger = new PrintStreamLogger(System.out);
                List<File> sources = new ArrayList<>();
                sources.add(compClass.file());
                List<File> libraries = new ArrayList<>();
                Map<String, Object> defaults = IFernflowerPreferences.getDefaults();
                defaults.put(IFernflowerPreferences.RENAME_ENTITIES,"1");
                ConsoleDecompiler decompiler = new ConsoleDecompiler(compDecompiled.file(), defaults, logger);
                sources.forEach(decompiler::addSource);
                libraries.forEach(decompiler::addLibrary);
                decompiler.decompileContext();
            }
            for (Fi fi : compJava.list()) {
                if (fi.isDirectory()) continue;
                String className = fi.nameWithoutExtension();
                if (Seq.with("BuildingComp", "BulletComp", "DecalComp", "EffectStateComp", "FireComp", "LaunchCoreComp", "PlayerComp", "PuddleComp").contains(className)) {
//                    Log.info("@ skipped", className);
                    compJava.child(fi.name()).delete();
                    compClass.child(fi.name()).delete();
                    compDecompiled.child(fi.name()).delete();
                    finalComp.child(fi.name()).delete();
                    continue;
                }
                if (className.equals("BuildingComp") ||
                    className.equals("BulletComp") ||
                    className.equals("DecalComp") ||
                    className.equals("EffectStateComp") ||
                    className.equals("FireComp") ||
                    className.equals("LaunchCoreComp") ||
                    className.equals("PlayerComp") ||
                    className.equals("PuddleComp")
                ) {
                    Log.info("@ skipped", className);
                    continue;
                }
                log = className.equals("DamageComp");
//            Log.info(className);
                ObjectMap<String, Seq<String>> annotationMethod = new ObjectMap<>();
                Seq<String> variableLines = new Seq<>();
                Seq<String> startAnnotations = new Seq<>();

                String file = fi.readString();
                String[] strings = file.split("abstract class " + className, 2);
                String[] spl = strings[0].split("\n");
                for (int i = spl.length - 1; i >= 0; i--) {
                    if (spl[i].replace(" ", "").equals("")) {
                        break;
                    }
                    startAnnotations.add(spl[i]);
                }
                if (strings.length == 2) {
//                String deb= strings[1].substring(strings[1].indexOf("{",strings[1].indexOf("{"))+1);
                    String variables = strings[1].substring(strings[1].indexOf("{", strings[1].indexOf("{")) + 1);
                    String[] varLines = variables.split("\n");
                    for (int i = 0; i < varLines.length; i++) {
                        String varLine = varLines[i];
//                    String nextLine = i<varLines.length-1?varLines[i+1]:"";
                        if (varLine.contains("{") && !varLine.contains(";")) {
                            variableLines.reverse();
                            while (variableLines.first().replace(" ", "").startsWith("@")) {
                                variableLines.remove(0);
                            }
                            variableLines.reverse();
                            break;
                        }
                        if (varLine.equals("}")) break;
                        variableLines.add(varLine);

                    }
//                Log.info("start:[@]\n variables: [@]",startAnnotations.toString("\n"),variableLines.toString("\n"));
//                System.out.println(variableLines.get(variableLines.size-2).strip());
                    String strip = "";
                    for (int i = variableLines.size - 1; i > 0 && strip.equals(""); i--, strip = strip(variableLines.get(i))) {
                    }
                    boolean hasVariables = !strip.equals("");
                    int methodIndex = strings[1].indexOf(strip) + strip.length();
                    String methods = strip(strings[1].substring(methodIndex));
//                Log.info("methods: @",methods);
                    String[] mlines = methods.split("\n");
                    for (int i = 0; i < mlines.length; i++) {
                        String mline = mlines[i];
                        if (isMethod(mline)) {
                            String prev = "";
                            String methodName = strip(mline.substring(0, mline.indexOf("(")));
                            for (int j = i; !"}".equals(prev) && !isMethod(prev) && j > 1; prev = mlines[--j]) {
                                if (strip(prev).startsWith("@"))
                                    annotationMethod.get(methodName, Seq::new).add(strip(prev));
                            }
                        }
                    }
                    String decomp = compDecompiled.child(fi.name()).readString();
                    Seq<String> newLines = new Seq<>();
                    boolean canAppend = false;
                    Seq<String> imports = new Seq<>();
                    if (log) {
                        Log.info(strip);
                        Log.info("[@]", variableLines.toString("],["));
                    }
                    for (String line : decomp.split("\n")) {
                        if (line.startsWith("package")) {
                            if (hasVariables) {
                                newLines.add(file.substring(0, file.indexOf(strip) + strip.length()));
                            } else {
                                for (String l : file.split("\n")) {
                                    newLines.add(l);
                                    if (l.contains("abstract class")) break;
                                }
                            }
                        } else if (line.startsWith("import")) {
                            imports.add(line);
//                            newLines.insert(newLines.size-1,line);
                        } else if (line.contains("(") && line.contains(") {") && line.startsWith("   ") && !line.substring(4).startsWith("   ")) {
                            canAppend = true;
                            String methodName = strip(line.substring(0, line.indexOf("(")));
                            if (annotationMethod.containsKey(methodName)) {
                                Seq<String> select = annotationMethod.get(methodName).select(s -> !s.contains("@Nullable") && !s.contains("@Override"));
                                for (String s : select) {
                                    while (select.count(l -> l.equals(s)) > 1) {
                                        select.remove(s);
                                    }
                                }
                                newLines.addAll(select);
                            }

                        }
                        if (canAppend) {
                            newLines.add(line);
                        }
                    }
                    if (!canAppend) {
                        newLines.add(variableLines.toString("\n"));
                        newLines.add("}");
                    }
                    String replace = newLines.toString("\n")
                            .replace("package mindustry.entities.comp;", "package braindustry.entities.compByAnuke;")
                            .replace(", Sized", "")
                            .replace("@Nullable", "NULLABLE")
                            .replace("@Override", "OVERRIDE")
//                            .replace("@Component","@braindustry.annotations.ModAnnotations.Component")
//                            .replace("@EntityDef","@braindustry.annotations.ModAnnotations.EntityDef")
//                            .replace("@BaseComponent","@braindustry.annotations.ModAnnotations.BaseComponent")
                            .replace("@", "@braindustry.annotations.ModAnnotations.")
                            .replace("NULLABLE", "@Nullable")
                            .replace("OVERRIDE", "@Override");
                    String[] split = replace.split("\n", 2);
                    finalComp.child(fi.name()).writeString(split[0] + "\n" + imports.toString("\n") + "\n" + split[1]);
//                for (ObjectMap.Entry<String, Seq<String>> entry : annotationMethod) {
//                    Log.info("@: [@]",entry.key,entry.value.toString(","));
//                }
                }
            }
//            finalComp.copyTo(dir);
            for (Fi fi : finalComp.list()) {
                fi.copyTo(dir.child(fi.name()));
            }
//            folder.deleteDirectory();
            System.out.println(Strings.format("Time taken: @s", Time.nanosToMillis(Time.timeSinceNanos(nanos)) / 1000f));
//        }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String strip(String str) {
        while (str.startsWith(" ")) {
            str = str.substring(1);
        }
        while (str.endsWith(" ")) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    private static boolean isMethod(String mline) {
        return mline.contains("(") && mline.contains(")") && mline.contains("{") && mline.startsWith("    ") && !mline.substring(4).startsWith("    ");
    }
}