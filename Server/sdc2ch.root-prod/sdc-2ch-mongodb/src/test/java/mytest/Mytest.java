package mytest;

import java.util.List;

import com.sdc2ch.cvo.io.TraceIO;
import com.sdc2ch.mongodb.service.cvo.CvoTraceServiceImpl;

public class Mytest {
	
    public static double harmonic(int n) {
        if (n == 1) return 1.0;
        return harmonic(n-1) + 1.0/n;
    } 

    public static void main(String[] args) {
        int n = Integer.parseInt(100 +"");
        System.out.println(harmonic(n));
        
        CvoTraceServiceImpl impl = new CvoTraceServiceImpl();
        impl.getVehicleTrace("20181012", "0000000272").forEach(System.out::println);
        
    }

}
