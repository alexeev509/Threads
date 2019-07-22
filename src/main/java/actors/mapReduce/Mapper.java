package actors.mapReduce;

//analog Function)))0)
public interface Mapper<T, R> {
    R map(T arg);
}
