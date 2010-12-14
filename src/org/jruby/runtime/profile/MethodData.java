
package org.jruby.runtime.profile;

import java.util.ArrayList;

public class MethodData extends InvocationSet { 
    public int serialNumber;
    
    public MethodData(int serial) {
        this.serialNumber = serial;
        this.invocations = new ArrayList<Invocation>();
    }
    
    public Integer[] parents() {
        ArrayList<Integer> p = new ArrayList<Integer>();
        for (Invocation inv : invocations) {
            if (inv.parent != null) {
                int serial = inv.parent.methodSerialNumber;
                if (p.indexOf(serial) == -1)
                    p.add(serial);
            }
        }
        return MethodData.convertIntegers2(p);
    }
    
    public Integer[] children() {
        ArrayList<Integer> p = new ArrayList<Integer>();
        for (Invocation inv : invocations) {
            for (Integer childSerial : inv.children.keySet()) {
                if (p.indexOf(childSerial) == -1) {
                    p.add(childSerial);
                }
            }
        }
        return MethodData.convertIntegers2(p);
    }
    
    public InvocationSet invocationsForParent(int parentSerial) {
        ArrayList<Invocation> p = new ArrayList<Invocation>();
        for (Invocation inv : invocations) {
            int serial = inv.parent.methodSerialNumber;
            if (serial == parentSerial)
                p.add(inv.parent);
        }
        return new InvocationSet(p);
    }
    
    public InvocationSet rootInvocationsFromParent(int parentSerial) {
        ArrayList<Invocation> p = new ArrayList<Invocation>();
        for (Invocation inv : invocations) {
            int serial = inv.parent.methodSerialNumber;
            if (serial == parentSerial && inv.recursiveDepth == 1)
                p.add(inv);
        }
        return new InvocationSet(p);
    }

    public InvocationSet invocationsFromParent(int parentSerial) {
        ArrayList<Invocation> p = new ArrayList<Invocation>();
        for (Invocation inv : invocations) {
            int serial = inv.parent.methodSerialNumber;
            if (serial == parentSerial)
                p.add(inv);
        }
        return new InvocationSet(p);
    }

    public InvocationSet rootInvocationsOfChild(int childSerial) {
        ArrayList<Invocation> p = new ArrayList<Invocation>();
        for (Invocation inv : invocations) {
            Invocation childInv = inv.children.get(childSerial);
            if (childInv != null && childInv.recursiveDepth == 1) {
                p.add(childInv);
            }
        }
        return new InvocationSet(p);
    }
    
    public InvocationSet invocationsOfChild(int childSerial) {
        ArrayList<Invocation> p = new ArrayList<Invocation>();
        for (Invocation inv : invocations) {
            Invocation childInv = inv.children.get(childSerial);
            if (childInv != null) {
                p.add(childInv);
            }
        }
        return new InvocationSet(p);
    }
    
    public long totalTime() {
        long t = 0;
        for (Invocation inv : invocations) {
            if (inv.recursiveDepth == 1) {
                t += inv.duration;
            }
        }
        return t;
    }
    
    public long childTime() {
        long t = 0;
        for (Invocation inv : invocations) {
            if (inv.recursiveDepth == 1) {
                t += inv.childTime();
            }
        }
        return t;
    }
    
    public static int[] convertIntegers(ArrayList<Integer> integers)
    {
        int[] ret = new int[integers.size()];
        for (int i=0; i < ret.length; i++)
        {
            ret[i] = integers.get(i).intValue();
        }
        return ret;
    }
    
    public static Integer[] convertIntegers2(ArrayList<Integer> integers)
    {
        Integer[] ret = new Integer[integers.size()];
        for (int i=0; i < ret.length; i++)
        {
            ret[i] = integers.get(i).intValue();
        }
        return ret;
    }
}