package aandrosov.cbs.database.repository;

public interface CRUDRepository<Model> {

    long create(Model model);
    
    Model read(long id);
    
    boolean update(Model model);
    
    boolean delete(long id);
}