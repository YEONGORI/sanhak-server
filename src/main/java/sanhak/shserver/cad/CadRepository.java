package sanhak.shserver.cad;

import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface CadRepository extends MongoRepository<Cad, String> {
    Cad findCadByTitle(String title);
    Cad findCadById(String id);
    Set<Cad> findAllByCadLabel(String cadLabel);
    Set<Cad> findAllByTitle(String title);
    Set<Cad> findAllByMainCategory(String mainCategory);
    Set<Cad> findAllBySubCategory(String subCategory);
    Set<Cad> findAllByAuthor(String author);
    Set<Cad> findAllByIndex(String index);
}
