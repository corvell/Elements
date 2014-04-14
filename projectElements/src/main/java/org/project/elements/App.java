package org.project.elements;

import java.util.Scanner;

public class App 
{
    public static void main( String[] args )
    {
        try
        {
            int cntElement;
            int cntGroup;
            int cntThread;
            
            Scanner scan = new Scanner(System.in);
            System.out.println("Количество элементов (int cntElement): ");
            cntElement = scan.nextInt();
            System.out.println("Количество групп (int cntGroup): ");
            cntGroup = scan.nextInt();
            System.out.println("Количество потоков (int cntThread):"); 
            System.out.println("(Примечание: если вводится cntThread = 0, то значение берется по умолчанию cntThread = cntGroup - 1): ");
            cntThread = scan.nextInt();
            if(cntThread == 0)
                cntThread = cntGroup - 1;
            
            GeneratorQueue.Init(cntElement, cntGroup);
            QueueHandler.start(cntThread);
        }
        catch(Exception ex)
        {
            System.out.println("входные параметры введены ошибочно");
        }
    }
}
