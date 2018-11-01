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
    private static double Qt;
    private static double totalDelay;
    private static double Bt;

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
                    Bt += clock - prevClock;
                arrivalTime();
                oneEntered(prevClock);
            } else if (timeList_AD[0] > timeList_AD[1]) {
                clock = timeList_AD[1];
                System.out.println("clock is " + clock);
                if (checkEnd())
                    break;
                if (serverIsBusy)
                    Bt += clock - prevClock;
                serviceTime();
                oneLeft(prevClock);
            } else {
                clock = timeList_AD[0];
                System.out.println("clock is " + clock);
                if (checkEnd())
                    break;
                if (serverIsBusy)
                    Bt += clock - prevClock;
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
        System.out.println("Q(t) is: " + Qt);
        System.out.println("B(t) is: " + Bt);
        System.out.println("Total delay in this system was: " + totalDelay);
        double Wq = totalDelay / customersServiced;
        double Lq = Qt / clock;
        double p = Bt / clock;
        double L = Lq + p;
        System.out.println("Wq is: " + Wq + " min/customers");
        System.out.println("Lq is: " + Lq + " customers");
        System.out.println("p is: " + p);
        System.out.println("L is: " + L + " customers");
    }

    private static void init() {
        timeList_AD[0] = 0;
        timeList_AD[1] = 0;
        arrivalTime();
        serviceTime();
        timeList_AD[1] += timeList_AD[0];
        clock = 0;
        Qt = 0;
        Bt = 0;
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
            Qt += queue.size() * (clock - prevClock);
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
            Qt += queue.size() * (clock - prevClock);
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
