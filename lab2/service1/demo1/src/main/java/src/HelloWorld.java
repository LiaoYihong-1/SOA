package src;


import jakarta.ejb.Remote;

@Remote
public interface HelloWorld {
//    WorkerFullInfo createWorker(CreateWorkerRequest t);
//
//    Page<?> getList(List<String> sort, List<String> filters, Boolean isUpper, Integer pageSize, Integer pageNum);
//
//    String delete(Integer id);
//
//    WorkerFullInfo updateWorker(WorkerInfo t, Integer id);
//
//    NumberOfWorkers getAmountByEndDate(String endDate, String condition) throws InvalidParameterException;
//
//    WorkerFullInfo getByMaxSalary();
//
//    WorkerFullInfo getById(Integer id);
    String getHelloWorld();
}