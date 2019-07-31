package doubleCheckProblem;

public class Singlton_0 {
    //Bad if we have lazy initilization
    private static Singlton_0 singlton_0 = new Singlton_0();

    private Singlton_0() {
    }

    public static Singlton_0 getSinglton_0() {
        return singlton_0;
    }
}
