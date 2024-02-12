package porsche.coffeeKitchen.consumer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConsumerRepository extends JpaRepository<Consumer, Long> {

    @Query("SELECT c FROM Consumer c WHERE c.name = ?1")
    Optional<Consumer> findConsumerByName(String name);
}
