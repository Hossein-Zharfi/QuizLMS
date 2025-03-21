package ir.maktabsharif.QuizLMS.repository;
import ir.maktabsharif.QuizLMS.model.User;
import ir.maktabsharif.QuizLMS.model.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
   Optional<User> findByEmail(String email);
}

