package spg.finalchallenge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spg.finalchallenge.entity.Wishlist;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    Wishlist findById(long id);

    Wishlist findByClientCpf(String cpf);


}
