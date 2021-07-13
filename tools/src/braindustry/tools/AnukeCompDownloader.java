package braindustry.tools;

import arc.files.Fi;
import arc.files.ZipFi;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.Strings;
import arc.util.Time;
import org.apache.commons.io.FileUtils;

import java.net.URL;

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

            for (Fi fi : compJava.list()) {
                if (fi.isDirectory()) continue;
                String className = fi.nameWithoutExtension();
                if (Seq.with("BuildingComp", "BulletComp", "DecalComp", "EffectStateComp", "FireComp", "LaunchCoreComp", "PlayerComp", "PuddleComp").contains(className)) {
//                    Log.info("@ skipped", className);
                    compJava.child(fi.name()).delete();
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
                Log.info("" + fi.name());
                StringBuilder newFile = new StringBuilder();
                String file = fi.readString();
                String[] lines = file.split("\n");
                StringBuilder partBuilder = new StringBuilder();
                boolean switchStart = false;
                int c = 0;
                for (int i = 0; i < lines.length; i++) {
                    String line = lines[i];
//                    Log.info(i);
                    if (switchStart) {
                        partBuilder.append(line).append("\n");
                        if (line.replace(" ", "").equals("};")) {
                            switchStart = false;
                            Log.info(partBuilder.toString());
                            newFile.append(transform(partBuilder.toString()));
                        }
                    } else if (line.contains("return switch")) {
                        switchStart = true;
                        partBuilder = new StringBuilder();
                        partBuilder.append(line).append("\n");
                    } else {
                        newFile.append(transform(line)
                                .replace("var core = team.core();", "mindustry.world.blocks.storage.CoreBlock.CoreBuild core = team.core();")
                                .replace("package mindustry.entities.comp;", "package braindustry.entities.compByAnuke;")
                                .replace("import static mindustry.logic.GlobalConstants.*;",
                                        "import static mindustry.logic.GlobalConstants.*;\n" + "import static mindustry.logic.LAccess.*;")
                        ).append("\n");
                    }
                }
                finalComp.child(fi.name()).writeString(newFile.toString());
            }
//            finalComp.copyTo(dir);
            for (Fi fi : finalComp.list()) {
                fi.copyTo(dir.child(fi.name()));
            }
            folder.deleteDirectory();
            System.out.println(Strings.format("Time taken: @s", Time.nanosToMillis(Time.timeSinceNanos(nanos)) / 1000f));
//        }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String transform(String line) {
        if (line.contains("return switch")) {
            String[] split = line.split("\n");
            StringBuilder newLine=new StringBuilder();
            for (String s : split) {
                newLine.append(s
                        .replace("return switch","switch")
                        .replace(" -> ",":\nreturn ")
                ).append("\n");
            }
            String s1 = newLine.toString();
            StringBuilder g=new StringBuilder(s1.split("\n",2)[0]);
            for (String s2 : s1.split("\n", 2)[1].split("\n")) {
                Log.info("[@]",s2);
                g.append(transform(s2)).append("\n");
            }
            line=g.toString();
            return line;
        }
        try {
            if (line.contains("instanceof ")) {
                if (line.contains("->")) {
                    int brackets = 0;
                    for (int i = 0; i < line.length(); i++) {
                        char c = line.charAt(i);
                        if (c == '(') brackets++;
                        if (c == ')') brackets--;
                    }
                    boolean monoLine = brackets == 0;
                    String preCenter = line.substring(0, line.indexOf('('));
                    String center = line.substring(line.indexOf('('), line.lastIndexOf(')') + 1);
                    String postCenter = line.substring(line.lastIndexOf(')'));
                    if (monoLine) {
                        boolean curlyBraces = center.substring(center.indexOf("->")).replace(" ", "").startsWith("{");
                        if (curlyBraces) {
                            String part1 = center.substring(0, center.indexOf("{") + 1);
                            String partCode = center.substring(center.indexOf("{") + 1, center.lastIndexOf("}"));
                            String part2 = center.substring(center.lastIndexOf("}"));
                            line = preCenter + part1 + transform(partCode) + part2 + postCenter;
                        } else {
                            String part1 = center.substring(0, center.indexOf("->") + 2) + "{";
                            String partCode = "return " + center.substring(center.indexOf("->") + 2, center.lastIndexOf(")")) + ";";
                            String part2 = "}";
                            line = preCenter + part1 + transform(partCode) + part2 + postCenter;
                        }
                    } else {
                        //            controlling.removeAll(u -> u.dead || !(u.controller() instanceof FormationAI ai && ai.leader == self()));
                        throw new RuntimeException("It's not monoline, I don't know what to do!!!");
                    }
                } else {
                    String deb = line.split(" instanceof")[0];
                    int opened = 0;
                    String instanceName = "";
                    String[] g17 = deb.split("");
                    for (int i = g17.length - 1; i >= 0; i--) {
                        String symbol = g17[i];
                        if (symbol.equals(")")) {
                            opened++;
                        }
                        if (opened > 0) {
                            if (symbol.equals("(")) opened--;
                        } else if (symbol.equals("(") || symbol.equals("&") || symbol.equals("|")) {
                            instanceName = deb.substring(i + 1);
                            break;
                        }

                    }
                    if (instanceName.equals("")) return line;
//                Log.info(instanceName);
                    String[] split = line.split("instanceof ")[1].split(" ", 2);
                    String[] strings = split[1].split("");
                    StringBuilder variableBuild = new StringBuilder();
                    Seq<String> with = Seq.with(")", "&", "|");
                    for (int i = 0; i < strings.length; i++) {
                        String string = strings[i];
                        if (with.contains(string)) break;
                        variableBuild.append(string);
                    }
                    String variableName = variableBuild.toString();
                    if (variableName.replace(" ", "").equals("")) return line;
                    int index = split[1].indexOf(variableName);
                    String part1 = line.substring(0, line.indexOf("instanceof ") + "instanceof ".length()) + split[0] + split[1].substring(0, index);
                    String part2 = split[1].substring(index + variableName.length());
                    String l = " && (" + variableName + " = (" + split[0] + ")" + instanceName + ")==" + instanceName, nl = split[0] + " " + variableName + ";";
//            Log.info("===@",part1);
                    line = nl + "\n" + part1 + l + part2;
//            Log.info("==@",Seq.with(split).toString(","));
                    if (split.length > 3 && !split[2].equals("&&") && !split[2].equals("||") && !split[2].equals(")") && !split[2].equals("}")) {
                    }
                }
            }
        } catch (Exception e) {
            Log.err("cannot transform line: [@]", line);
        }
        return line;
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