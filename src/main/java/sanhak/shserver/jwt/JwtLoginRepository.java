package sanhak.shserver.jwt;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface JwtLoginRepository extends MongoRepository<UserInform, Integer> {
    UserInform findByEmployNumber(Integer employNumber);
}