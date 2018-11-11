package hu.uni.miskolc.iit.softwaretesting.service.impl;

import org.apache.commons.lang.SystemUtils;

import java.io.*;

public class CommandLine {

    public static String findMyDatabaseFile() throws FileNotFoundException {
        File file = null;

        if (SystemUtils.IS_OS_WINDOWS) {
            String filename = "findMyFile.bat";
            file = new File(filename);
            PrintWriter writer = new PrintWriter(filename);
            for (File root : File.listRoots()) {
                String rootPath = root.getAbsolutePath();
                writer.println("WHERE \"dao/resources/database.xml\" /r \"" + rootPath + "\\\"");
            }
            writer.close();
        }
        else{
            file = new File("findMyFile.sh");
            PrintWriter writer = new PrintWriter("findMyFile.sh");
            writer.println("find ~/ -path '*/dao/resources/database.xml'");
            writer.close();
        }

        String result = null;
        try {
            Runtime.getRuntime().exec("chmod +x " + file.getAbsoluteFile());
            Process p = Runtime.getRuntime().exec(file.getAbsolutePath());

            BufferedReader in =
                    new BufferedReader(new InputStreamReader(p.getInputStream()));
            result = "";
            result = in.readLine();

            in.close();

        } catch (IOException e) {
            System.out.println(e);
        }
        return result;
    }
}
