package com.sdc2ch.web.spring.test.util;




public class MetricsPrinter extends MultiColumnPrinter  {
    public MetricsPrinter(int numCols, int gap, String border, int align)  {
	super(numCols, gap, border, align);
    }

    public MetricsPrinter(int numCols, int gap, String border)  {
	super(numCols, gap, border);
    }

    public void doPrint(String str)  {
        System.out.print(str);
    }

    public void doPrintln(String str)  {
        System.out.println(str);
    }
}
