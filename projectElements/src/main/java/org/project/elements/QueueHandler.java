/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.project.elements;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.project.elements.model.Element;

/**
 *
 * @author Suvorkov Vladimir
 */
public class QueueHandler {
    
    public static void start(int cntThread)
    {
        ExecutorService service = Executors.newCachedThreadPool();
        for(int j = 1; j <= cntThread; j++) 
        {
            final int iThread = j;
            service.submit(new Runnable() {
                
                /**
                 * количество элементов группы, обрабатываемых в потоке
                 */
                private final int cntElemHandler = 10;
                
                /**
                 * Считает элементы в группе
                 * если достигнет cntElemHandler, поток должен переключиться 
                 * на другую группу
                 */
                private int counterElem = 0;
                
                @Override
                public void run() {

                    /**
                     * Выполняем до тех пор, пока очередь не пуста
                     * или поток добавления элементов в очередь еще работает
                     */
                    while(GeneratorQueue.getQueue().size() > 0 || 
                            GeneratorQueue.getIsAliveThreadAddElements())
                    {
                        LinkedList<Element> l = (LinkedList<Element>) GeneratorQueue.getQueue().clone();

                        int cntElem = 1;
                        for ( Element elem : l) 
                        {
                            cntElem++;
                            if(elem.getGroupId() == TestAndSet.getGroupId(iThread, elem.getGroupId()) )
                            {
                                counterElem++;
                                printResult(elem);
                            }
                            
                            if(counterElem == cntElemHandler || cntElem == l.size() )
                            {
                                LinkedList<Element> l1 = (LinkedList<Element>) GeneratorQueue.getQueue().clone();
                                for ( Element elem1 : l1) 
                                {
                                    if(TestAndSet.setGroupId(iThread, elem1.getGroupId()))
                                    {
                                    counterElem = 0;
                                    break;
                                    }
                                }
                                break;
                            }
                            
                            try {
                            /* переводим в невктивный режим на
                            некоторое время, чтобы вывод результата потоков чередовался */
                                Thread.sleep(100);
                            } catch (InterruptedException e) {}
                        }    
                    }
                    
                }
            });
        }
    }
    
    /**
     * Результат обработки элемента
     * @param elem элемент
     */
    private static void printResult(Element elem)
    {
        String s = "RESULT: Поток ".
                concat( Thread.currentThread().getName()).
                concat( " обработал элемент: ( itemId = ").
                concat( elem.getId().toString() ).
                concat( ", ").
                concat( "groupId = " ).
                concat( elem.getGroupId().toString() ).
                concat( " )" );
        LogWriter.write(s);
        System.out.println( s );
        /**
         * Удаляем из очереди обработанный элемент
         */
        GeneratorQueue.removeElement(elem);
    }
    
    private static class TestAndSet
    {
        /**
         * Хранит значение поток - группа
         */
        private static final HashMap<Integer,Integer> map = new HashMap<Integer, Integer>();
        
        /**
         * Возвращает id группы, обрабатываемой потоком iThread
         * @param iThread номер потока
         * @param groupId id группы
         * @return id группы
         */
        public synchronized static int getGroupId(int iThread, int groupId)
        {
            Integer idgroup = map.get(iThread);
            if( idgroup != null ) 
            {
                return idgroup;
            }
            else {
                if(setGroupId(iThread, groupId))
                    return groupId;
            }
            
            return -1;
        }
     
        /**
         * Меняет группу для потока
         * @param iThread
         * @param groupId 
         */
        public synchronized static boolean setGroupId(int iThread, int groupId)
        {
            if(!map.containsValue(groupId))
            {
                map.put(iThread,groupId);
                return true;
            }
            else
            {
                return false;
            }
        }
    }
}
