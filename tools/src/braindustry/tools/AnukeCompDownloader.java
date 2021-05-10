package braindustry.tools;

import arc.Core;
import arc.Files;
import arc.files.Fi;
import arc.func.Boolf;
import arc.func.Cons;
import arc.struct.Seq;
import arc.util.ArcRuntimeException;
import arc.util.OS;
import arc.util.Strings;
import arc.util.Time;
import arc.util.io.Reads;
import arc.util.io.Streams;
import arc.util.io.Writes;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Scanner;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

public class AnukeCompDownloader {
    public static void main(String[] args) {
        try {
            long nanos = System.nanoTime();
            Fi dir = new Fi("annotations/main/src/braindustry/entities/compByAnuke"), fi;
            if (dir.exists()) dir.delete();
            URLConnection connection = new URL("https://github.com/Anuken/Mindustry/tree/master/core/src/mindustry/entities/comp").openConnection();
            Scanner scanner = new Scanner(connection.getInputStream());
            boolean prepare = false;
            int i = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (prepare) {
                    prepare = false;
                    try {
                        String fileName = line.split("<span class=\"css-truncate css-truncate-target d-block width-fit\"><a class=\"js-navigation-open Link--primary\" title=\"")[1]
                                .split("\"")[0];
                        String strUrl = "https://raw.githubusercontent.com/Anuken/Mindustry/master/core/src/mindustry/entities/comp/" + fileName;
                        URLConnection con = new URL(strUrl).openConnection();
                        fi = dir.child(fileName);
                        fi.delete();
                        Scanner scan = new Scanner(con.getInputStream());
                        while (scan.hasNextLine()) {
                            String string = scan.nextLine() + "\n";
                            if (string.startsWith("package ")) {
                                fi.writeString(string.replace("mindustry.entities.comp", "braindustry.entities.compByAnuke")+"\nimport mindustry.entities.*;\nimport mindustry.entities.comp.Sized;",true );
                            } else {

                                fi.writeString(string.replace("mindustry.annotations.Annotations.*","braindustry.annotations.ModAnnotations.*"), true);
                            }
                        }
                        i++;
                        //                    System.out.println(fileName+" "+packageStr);
                    } catch (Exception exception) {
                    }
                } else if (line.contains("role=\"rowheader\"")) {
                    prepare = true;
                }
            }
            System.out.println(Strings.format("Time taken to download: @s", Time.nanosToMillis(Time.timeSinceNanos(nanos)) / 1000f));
//            System.out.println(Strings.format("files generating: @", i));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
