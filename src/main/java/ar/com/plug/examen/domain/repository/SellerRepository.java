package ar.com.plug.examen.domain.repository;

import ar.com.plug.examen.domain.model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerRepository extends JpaRepository<Seller, Long> {
}
