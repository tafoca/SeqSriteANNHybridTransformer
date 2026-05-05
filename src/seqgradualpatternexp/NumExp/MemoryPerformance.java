package seqgradualpatternexp.NumExp;

/**
 *
 * @author fotso
 */
public interface MemoryPerformance {

    public static final int MEGABYTE = 1024 *1024 ;//

    public static double bytesToMegabytes(long bytes) {
        return bytes / MEGABYTE;
    }
     /**
     * // Get the Java runtime memory in byte
     * @return 
     */
    public static long getMemoryCurrent() {
        // Get the Java runtime
        Runtime runtime = Runtime.getRuntime();
        // Run the garbage collector
        runtime.gc();
        // Calculate the used memory
        long memory = runtime.totalMemory()- runtime.freeMemory();//runtime.totalMemory() - runtime.freeMemory();
        return memory;
    }
    /**
     * // Get the Java runtime memory in MB
     * @return 
     */
     public static double getMemoryCurrentMB() {
        return getMemoryCurrent();
        //return bytesToMegabytes(getMemoryCurrent());
    }
    

}
