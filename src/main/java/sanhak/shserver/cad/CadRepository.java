package sanhak.shserver.cad;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CadRepository extends MongoRepository<Cad, String> {
    List<Cad> findAllByCadLabel(CadLabel cadLabel);
    Cad findCadByTitle(String title);
}
