package doubleCheckProblem;

public class Singlton_1 {
    //Bad if we have lazy initilization
    private static Singlton_1 singlton_1;

    private Singlton_1() {
    }

    public static Singlton_1 getSinglton_1() {
        if (singlton_1 == null) {
            //1:
            synchronized (Singlton_1.class) {
                //We must check again, because another thread is waiting in "1:"
                //And if we dont have this check - he will create  new singlton again
                if (singlton_1 == null) {
                    singlton_1 = new Singlton_1();
                }
            }
        }
        return singlton_1;
    }
}
//http://qaru.site/questions/65537/why-is-volatile-used-in-this-example-of-double-checked-locking
