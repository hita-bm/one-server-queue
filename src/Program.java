import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import static java.lang.Math.round;

public class Program {
    private static double clock;
    private static double[] timeList_AD = new double[2];
    private static boolean isEnd;
    private static Queue<Double> queue;
    private static boolean serverIsBusy;
    private static int customersServiced;
    private static double q;
    private static double totalDelay;
    private static double b;

    public static void main(String args[]) {
        init();
        while (!isEnd) {
            double prevClock = clock;
            if (timeList_AD[0] < timeList_AD[1]) {
                clock = timeList_AD[0];
                System.out.println("clock is " + clock);
                if (checkEnd())
                    break;
                if (serverIsBusy)
                    b += clock - prevClock;
                arrivalTime();
                oneEntered(prevClock);
            } else if (timeList_AD[0] > timeList_AD[1]) {
                clock = timeList_AD[1];
                System.out.println("clock is " + clock);
                if (checkEnd())
                    break;
                if (serverIsBusy)
                    b += clock - prevClock;
                serviceTime();
                oneLeft(prevClock);
            } else {
                clock = timeList_AD[0];
                System.out.println("clock is " + clock);
                if (checkEnd())
                    break;
                if (serverIsBusy)
                    b += clock - prevClock;
                arrivalTime();
                serviceTime();
                oneLeft(prevClock);
                oneEntered(prevClock);
            }
            System.out.println("A: " + timeList_AD[0] + " | D: " + timeList_AD[1]);
            printQueue();
            isEnd &= checkEnd();
        }
        System.out.println("__________________________________________");
        System.out.println("Customers Serviced: " + customersServiced);
        System.out.println("Q(t) is: " + q);
        System.out.println("B(t) is: " + b);
        System.out.println("Total delay in this system was: " + totalDelay);
    }

    private static void init() {
        timeList_AD[0] = 0;
        timeList_AD[1] = 0;
        arrivalTime();
        serviceTime();
        timeList_AD[1] += timeList_AD[0];
        clock = 0;
        q = 0;
        b = 0;
        totalDelay = 0;
        queue = new LinkedList<>();
        isEnd = false;
        serverIsBusy = false;
        customersServiced = 0;
        System.out.println("system initialized, clock is 0");
        System.out.println("A: " + timeList_AD[0] + " | D: " + timeList_AD[1] + "\n");
    }

    private static double exponentialDistribution(double lambda) {
        Random random = new Random();
        return round((-100) * Math.log(random.nextDouble()) / lambda) / 100.0;
    }

    private static void arrivalTime() {
        timeList_AD[0] += exponentialDistribution(1.0);
    }

    private static void serviceTime() {
        timeList_AD[1] += exponentialDistribution(1.25);
    }

    private static boolean checkEnd() {
        return customersServiced == 5;
    }

    private static void oneEntered(double prevClock) {
        if (queue.size() > 0) {
            q += queue.size() * (clock - prevClock);
        }
        queue.add(clock);
        if (!serverIsBusy) {
            queue.remove();
            serverIsBusy = true;
        }
        System.out.println("one entered");
    }

    private static void oneLeft(double prevClock) {
        customersServiced++;
        if (queue.size() > 0) {
            q += queue.size() * (clock - prevClock);
            double time = queue.remove();
            totalDelay += clock - time;
            isEnd = true;
        } else {
            serverIsBusy = false;
            while (timeList_AD[0] >= timeList_AD[1]) {
                serviceTime();
            }
        }
        System.out.println("one left");
    }

    private static void printQueue() {
        System.out.println("<<<<< Queue <<<<<");
        if (queue.size() > 0) {
            System.out.print(queue);
        } else {
            System.out.println("queue is empty!");
        }
        System.out.println("\n");
    }
}
