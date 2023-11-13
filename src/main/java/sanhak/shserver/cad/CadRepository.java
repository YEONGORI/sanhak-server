package sanhak.shserver.cad;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CadRepository extends MongoRepository<Cad, String> {
    List<Cad> findAllByCadLabel(String cadLabel);
    Cad findCadByTitle(String title);
}
