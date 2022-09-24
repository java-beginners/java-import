package org.example.io.imports;

import org.example.io.imports.functional_Interface_define.BuildFileName;
import org.example.io.imports.functional_Interface_define.Next;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64.Decoder;
import java.util.List;
import java.util.Scanner;

public class FileReader {
    private String fileName;
    private String delimiter;
    private Decoder decoder;

    public FileReader(BuildFileName buildFileName, String delimiter, Decoder... opts) {
        Decoder decoder = null;
        if (opts.length > 0 && opts[0] != null) {
            decoder = opts[0];
        }
        String fileName = buildFileName.buid();
        if (fileName.trim().length() == 0) {
            throw new RuntimeException("file name cannot be emtpy");
        }
        this.fileName = fileName;
        this.decoder = decoder;
        this.delimiter = delimiter;
    }

    public void Read(Next next) throws Exception {
        try {
            File myObj = new File(fileName);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                List<String> lines = new ArrayList<>();
                if (data != "") {
                    lines.add(data);
                    next.next(lines, null);
                }
            }
            myReader.close();
            next.next(new ArrayList<String>(), true);
        } catch (Exception e) {
            e.printStackTrace();
            next.next(new ArrayList<String>(), false);
        }
    }

    public void ReadDelimiterFile(Next next) throws Exception {
        try {
            File myObj = new File(fileName);

            Scanner myReader = new Scanner(myObj);

//            if (this.delimiter != null) {
//                myReader.useDelimiter(this.delimiter);
//            }

            while (myReader.hasNextLine()) {
                // resolve double quote from csv
                String data = myReader.nextLine().replace("\"","");
                List<String> lines = new ArrayList<String>();
                lines.addAll(Arrays.asList(data.split(delimiter)));
                next.next(lines, null);
            }
            myReader.close();
            next.next(new ArrayList<>(), true);
        } catch (Exception e) {
            e.printStackTrace();
            next.next(new ArrayList<>(), false);
        }
    }
}
