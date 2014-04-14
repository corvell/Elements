/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.project.elements;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

/**
 *
 * @author Suvorkov Vladimir
 */
public class LogWriter {
    
    /**
     * Запись строки в файл
     * @param str 
     */
    public static void write(String str)
    {
        FileWriter writeFile = null;
        try {
            writeFile = new FileWriter("project_queue.log",true);
            writeFile.append(new Date().toLocaleString().concat(" ").concat(str).concat("\n"));
        } catch (IOException e) {
        } finally {
            if(writeFile != null) {
                try {
                    writeFile.close();
                } catch (IOException e) {
                }
            }
        }
    }
}
