/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.project.elements;

import java.util.LinkedList;
import java.util.Random;
import org.project.elements.model.Element;

/**
 *
 * @author Suvorkov Vladimir
 */

public class GeneratorQueue {
    
    /**
     * Уникальный идентификатор элемента
     */
    private static int uniqueNumberElement = 0;
    
    private static Thread threadAddElements ;
    /**
     * true - запущен поток добавления элементов в очередь
     * false - не запущен
     */
    private static final boolean isAliveThreadAddElements = true;
    
    /**
     * Очередь элементов
     */
    private static LinkedList linkedListQueue = new LinkedList();
    
    /**
     * @return следующий по порядку уникальный идентификатор
     */
    public synchronized static int getNextUniqueNumber()
    {
        return ++GeneratorQueue.uniqueNumberElement;
    }
    
    /**
     * @return очередь элементов
     */
    public static LinkedList getQueue()
    {
        return linkedListQueue;
    }
    
    /**
     * 
     * @return запущен поток добавления элементов в очередь или нет
     */
    public static boolean getIsAliveThreadAddElements()
    {
        return threadAddElements.isAlive();
        //return isAliveThreadAddElements;
    }
    
    /**
     * Добавляет элемент к очереди
     * id элемента берется из uniqueNumberElement
     * 
     * @param idGroup id группы
     */
    public synchronized static void addElement(int idGroup)
    {
        Element elem = new Element();
        elem.setId(getNextUniqueNumber());
        elem.setGroupId(idGroup);
        linkedListQueue.add(elem);
        
        final String s = "Добавляем в очередь элемент ( itemId = " + 
                elem.getId() + ", groupId = " + elem.getGroupId() + " )";
        System.out.println( s );
        LogWriter.write(s);
    }
    
    /**
     * Удаляет элемент из очереди
     * @param elem элемент очереди
     */
    public synchronized static void removeElement(Element elem)
    {
        final String s = "Удаляем из очереди элемент ( itemId = " + 
                elem.getId() + ", groupId = " + elem.getGroupId() + " )";
        //System.out.println( s );
        LogWriter.write(s);
        linkedListQueue.remove(elem);
    }
    
    /**
     * Генерирует очередь на основе входных параметров 
     * 
     * @param cntElement количество элементов
     * @param cntGroup количество групп
     */
    public static void Init(int cntElement, int cntGroup)
    {
        linkedListQueue = new LinkedList();
       
        /**
         * Добавляем элементы в очередь в потоке 
         */
        AddElementThread addElementThread = new AddElementThread(cntElement, cntGroup);
        threadAddElements = new Thread(addElementThread);
        threadAddElements.setName("threadAddElementsInQueue");
        threadAddElements.start();
        
        /* 
        //Если надо обрабатывать элементы после добавления всех элементов в очередь, то
        //необходимо расскомментировать блок
        try {
            threadAddElements.join(); // задержка, ждем когда выполнится поток добавления элементов
        } catch (InterruptedException ex) {
        }
        */        
    }
    
    private static class AddElementThread implements Runnable{

        private final int cntElement, cntGroup;
        private final Random rand;
        
        public AddElementThread(int cntElement, int cntGroup)
        {
            this.rand = new Random();
            this.cntElement = cntElement; 
            this.cntGroup = cntGroup;
        }
        
        @Override
        public void run() 
        {
            // Генерируем рандомом группу и добавляем элемент в очередь
            while(uniqueNumberElement < cntElement) {
                addElement(rand.nextInt(cntGroup) + 1); // + 1 для того чтобы нумерация пошла с 1 до cntGroup включительно
                try {
                    // Добавление в очередь с задержкой
                    Thread.sleep(50);
                } catch (InterruptedException e) {}
            }
        }
    }
}
