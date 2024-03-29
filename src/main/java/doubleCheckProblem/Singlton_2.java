package doubleCheckProblem;

public class Singlton_2 {
    //Bad if we have lazy initilization
    private static volatile Singlton_2 singlton_2;

    private Singlton_2() {
    }

    public static Singlton_2 getSinglton_2() {
        if (singlton_2 == null) {
            //1:
            synchronized (Singlton_1.class) {
                //We must check again, because another thread is waiting in "1:"
                //And if we dont have this check - he will create  new singlton again
                if (singlton_2 == null) {
                    singlton_2 = new Singlton_2();
                }
            }
        }
        return singlton_2;
    }
}

//Зачем volatile?
//Поток А замечает, что переменная не инициализирована, затем получает блокировку и начинает инициализацию.
//Семантика некоторых языков программирования[каких?] такова, что потоку А разрешено присвоить разделяемой переменной ссылку на объект, который находится в процессе инициализации (что в общем-то вполне однозначно нарушает причинно-следственную связь, ведь программист вполне явно просил присваивать переменной ссылку на объект [то есть — опубликовать ссылку в общий доступ] — в момент после инициализации, а не в момент до инициализации).
//Поток Б замечает, что переменная инициализирована (по крайней мере, ему так кажется), и возвращает значение переменной без получения блокировки. Если поток Б теперь будет использовать переменную до того момента, когда поток А закончит инициализацию, поведение программы будет некорректным.
