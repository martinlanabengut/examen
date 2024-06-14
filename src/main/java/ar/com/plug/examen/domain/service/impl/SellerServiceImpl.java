package ar.com.plug.examen.domain.service.impl;

import ar.com.plug.examen.domain.model.Seller;
import ar.com.plug.examen.domain.repository.SellerRepository;
import ar.com.plug.examen.domain.service.SellerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SellerServiceImpl implements SellerService {
    private static final Logger logger = LoggerFactory.getLogger(SellerServiceImpl.class);

    @Autowired
    private SellerRepository sellerRepository;

    @Override
    public Seller createSeller(Seller seller) {
        logger.debug("Creating seller with details: {}", seller);
        try {
            Seller savedSeller = sellerRepository.save(seller);
            logger.debug("Seller created successfully: {}", savedSeller);
            return savedSeller;
        } catch (Exception e) {
            logger.error("Error creating seller: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public List<Seller> getAllSellers() {
        logger.debug("Retrieving all sellers");
        try {
            List<Seller> sellers = sellerRepository.findAll();
            logger.debug("Retrieved {} sellers", sellers.size());
            return sellers;
        } catch (Exception e) {
            logger.error("Error retrieving sellers: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public Optional<Seller> getSellerById(Long id) {
        logger.debug("Retrieving seller by id: {}", id);
        try {
            Optional<Seller> seller = sellerRepository.findById(id);
            seller.ifPresent(s -> logger.debug("Seller retrieved successfully: {}", s));
            return seller;
        } catch (Exception e) {
            logger.error("Error retrieving seller by id: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public Seller updateSeller(Long id, Seller sellerDetails) {
        logger.debug("Updating seller with id: {}", id);
        try {
            Seller seller = sellerRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Seller not found"));
            seller.setName(sellerDetails.getName());
            seller.setEmail(sellerDetails.getEmail());
            Seller updatedSeller = sellerRepository.save(seller);
            logger.debug("Seller updated successfully: {}", updatedSeller);
            return updatedSeller;
        } catch (Exception e) {
            logger.error("Error updating seller: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public void deleteSeller(Long id) {
        logger.debug("Deleting seller with id: {}", id);
        try {
            sellerRepository.deleteById(id);
            logger.debug("Seller deleted successfully");
        } catch (Exception e) {
            logger.error("Error deleting seller: {}", e.getMessage());
            throw e;
        }
    }
}
