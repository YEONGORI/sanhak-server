package sanhak.shserver.cad;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CadRepository extends MongoRepository<Cad, String> {
    List<Cad> findAllByTitleContains(String searchText);
    List<Cad> findAllByIndexContains(String searchText);

    List<Cad> findAllByMainCategoryContains(String searchText);
    List<Cad> findAllBySubCategoryContains(String searchText);

}
