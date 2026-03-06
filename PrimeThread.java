
import java.time.Duration;
import java.time.Instant;
import java.util.Scanner;

public class PrimeThread{
    
    private static int total=0;
    //this provided thread calls functions you will need to run in order to calculate the number of threads between the provided min and max values.
    public static class CountPrimesThread extends Thread {
        int count = 0;
        int min, max;
        public CountPrimesThread(int min, int max) {
            this.min = min;
            this.max = max;
        }
        public void run() {
            count = countPrimes(min,max);
            // total += count;
            System.out.println("There are " + count + 
                    " primes between " + min + " and " + max);
            sendBack(count);
        }
    }
    
    /**
     *Notice how this is a synchronized method! This allows multiple threads to access it safely.
     *We use this to keep track of the total number of primes found.
     */
    synchronized private static void sendBack(int count) {
        total += count;
    }
    /**
     * Count the primes between min and max, inclusive.
     * you need to implement this!
     */
    private static int countPrimes(int min, int max) {
        int count = 0;
        for (int num = min; num <= max; num++)
        {
            //System.out.print("Testing " + num + " ");
            if(isPrime(num))
            {
                count++;
            }
        }
        return count;
    }

    /**
     * Test whether x is a prime number.
     * x is assumed to be greater than 1.
     * You need to implement this!
     */
    private static boolean isPrime(int x) {
        for (int i = 2; i < x; i++)
        {
            if(x%i == 0)
            {
                return false;
            }
        }
        return true;
    }

    public void test(){
        int num = 0;
        for(int i=0; i<=50; i++){  
           num =num+i;
           System.out.print(num+", ");
        }  
     }
    
    public static void main(String args[]) {  
        int processors = Runtime.getRuntime().availableProcessors();
        if (processors == 1)
            System.out.println("Your computer has only 1 available processor.\n");
        else
            System.out.println("Your computer has " + processors + " available processors.\n");
        //uncomment the following once you're ready to take user input for the number of threads and max value to test
        Scanner reader = new Scanner(System.in);  // Reading from System.in
        System.out.println("Enter the number for which you wish to know how many primes are smaller than it: ");
        int max = reader.nextInt();
        System.out.println("Enter the number of threads you wish to use in your program");
        int threads = reader.nextInt();
        
        //Starting time
        Instant start = Instant.now();

        CountPrimesThread[] threadArray = new CountPrimesThread[threads];
        int min = 0;
        int threadsLeft = threads - 1;
        // with an extended amount of time spent on purely testing the best ratios
        // 5/12ths sized partitions result in the best time, as when we go above or below that
        // time begins to increase again
        int a = 5;
        int b = 12;
        int adjMax = (max-min)*a/b;
        for(int i = 0; i < threads; i++)
        {
            if(threadsLeft == 0) // when we are the last thread
            {
                // we want all of the rest
                threadArray[i] = new CountPrimesThread(min, max);
                threadArray[i].start();
            } else { // errors chunking here
                // If we arent the last thread to make
                // We want to go from min to 3/4 of the space
                adjMax = min+((max-min)*a/b);
                threadArray[i] = new CountPrimesThread(min, adjMax);
                System.out.println(adjMax);
                threadArray[i].start();

                //Move are selection
                min = adjMax;
                threadsLeft--;
            }
        }

        for(int i = 0; i < threads; i++)
        {
            try {
                threadArray[i].join();
            } catch (InterruptedException e){
                e.printStackTrace();
            } 
        }

        System.out.println("!!    There are " + total + " total primes between 0 and " + max + "    !!");


       
        //comment the following line out once you've seen how this works.
        //new PrimeThread().test();
       
       
        //Write the code for spawning the desired number of CountPrimeThreads
        //be sure to divide the work among the specified number of threads efficiently. Use .join() to check to see if a thread is finished.
        //System.out.println("Prime count: " + countPrimes(0, max));
       
        //End time
        Instant end = Instant.now();
        long time = Duration.between(start, end).toMillis();
        System.out.println();
        System.out.println(time+" Milli seconds");
    }
}