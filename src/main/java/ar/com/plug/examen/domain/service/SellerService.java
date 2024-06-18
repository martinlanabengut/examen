package ar.com.plug.examen.domain.service;

import ar.com.plug.examen.domain.model.Seller;

import java.util.List;
import java.util.Optional;

public interface SellerService {
    Seller createSeller(Seller seller);
    List<Seller> getAllSellers();
    Optional<Seller> getSellerById(Long id);
    Seller updateSeller(Long id, Seller sellerDetails);
    void deleteSeller(Long id);
}
