package ar.com.plug.examen.domain.service;

import ar.com.plug.examen.domain.model.Seller;
import ar.com.plug.examen.domain.repository.SellerRepository;
import ar.com.plug.examen.domain.service.impl.SellerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class SellerServiceImplTest {

    @InjectMocks
    private SellerServiceImpl sellerServiceImpl;

    private SellerService sellerService;

    @Mock
    private SellerRepository sellerRepository;

    @Mock
    private Logger logger;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        sellerService = sellerServiceImpl;
    }

    @Test
    @DisplayName("Should create a new seller successfully")
    void testCreateSeller() {
        // Given
        Seller seller = new Seller();
        seller.setName("Test Seller");

        // When
        when(sellerRepository.save(seller)).thenReturn(seller);
        Seller createdSeller = sellerService.createSeller(seller);

        // Then
        assertThat(createdSeller).isNotNull();
        assertThat(createdSeller.getName()).isEqualTo("Test Seller");
        verify(sellerRepository, times(1)).save(seller);
    }

    @Test
    @DisplayName("Should throw exception when creating seller fails")
    void testCreateSellerThrowsException() {
        // Given
        Seller seller = new Seller();
        seller.setName("Test Seller");

        // When
        when(sellerRepository.save(seller)).thenThrow(new RuntimeException("Error creating seller"));
        Exception exception = assertThrows(RuntimeException.class, () -> sellerService.createSeller(seller));

        // Then
        assertThat(exception.getMessage()).isEqualTo("Error creating seller");
        verify(sellerRepository, times(1)).save(seller);
    }

    @Test
    @DisplayName("Should return all sellers")
    void testGetAllSellers() {
        // Given
        Seller seller1 = new Seller();
        Seller seller2 = new Seller();
        List<Seller> sellers = Arrays.asList(seller1, seller2);

        // When
        when(sellerRepository.findAll()).thenReturn(sellers);
        List<Seller> result = sellerService.getAllSellers();

        // Then
        assertThat(result).hasSize(2);
        verify(sellerRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should throw exception when retrieving all sellers fails")
    void testGetAllSellersThrowsException() {
        // When
        when(sellerRepository.findAll()).thenThrow(new RuntimeException("Error retrieving sellers"));
        Exception exception = assertThrows(RuntimeException.class, () -> sellerService.getAllSellers());

        // Then
        assertThat(exception.getMessage()).isEqualTo("Error retrieving sellers");
        verify(sellerRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return seller by ID")
    void testGetSellerById() {
        // Given
        Seller seller = new Seller();
        seller.setId(1L);

        // When
        when(sellerRepository.findById(1L)).thenReturn(Optional.of(seller));
        Optional<Seller> result = sellerService.getSellerById(1L);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1L);
        verify(sellerRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when retrieving seller by ID fails")
    void testGetSellerByIdThrowsException() {
        // When
        when(sellerRepository.findById(1L)).thenThrow(new RuntimeException("Error retrieving seller by id"));
        Exception exception = assertThrows(RuntimeException.class, () -> sellerService.getSellerById(1L));

        // Then
        assertThat(exception.getMessage()).isEqualTo("Error retrieving seller by id");
        verify(sellerRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should update seller successfully")
    void testUpdateSeller() {
        // Given
        Seller seller = new Seller();
        seller.setId(1L);
        seller.setName("Updated Seller");

        Seller updatedDetails = new Seller();
        updatedDetails.setName("Updated Seller");

        // When
        when(sellerRepository.findById(1L)).thenReturn(Optional.of(seller));
        when(sellerRepository.save(seller)).thenReturn(seller);
        Seller updatedSeller = sellerService.updateSeller(1L, updatedDetails);

        // Then
        assertThat(updatedSeller).isNotNull();
        assertThat(updatedSeller.getName()).isEqualTo("Updated Seller");
        verify(sellerRepository, times(1)).findById(1L);
        verify(sellerRepository, times(1)).save(seller);
    }

    @Test
    @DisplayName("Should throw exception when updating seller fails")
    void testUpdateSellerThrowsException() {
        // Given
        Seller updatedDetails = new Seller();
        updatedDetails.setName("Updated Seller");

        // When
        when(sellerRepository.findById(1L)).thenThrow(new RuntimeException("Error updating seller"));
        Exception exception = assertThrows(RuntimeException.class, () -> sellerService.updateSeller(1L, updatedDetails));

        // Then
        assertThat(exception.getMessage()).isEqualTo("Error updating seller");
        verify(sellerRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should delete seller successfully")
    void testDeleteSeller() {
        // Given
        long sellerId = 1L;

        // When
        sellerService.deleteSeller(sellerId);

        // Then
        verify(sellerRepository, times(1)).deleteById(sellerId);
    }

    @Test
    @DisplayName("Should throw exception when deleting seller fails")
    void testDeleteSellerThrowsException() {
        // Given
        long sellerId = 1L;

        // When
        doThrow(new RuntimeException("Error deleting seller")).when(sellerRepository).deleteById(sellerId);
        Exception exception = assertThrows(RuntimeException.class, () -> sellerService.deleteSeller(sellerId));

        // Then
        assertThat(exception.getMessage()).isEqualTo("Error deleting seller");
        verify(sellerRepository, times(1)).deleteById(sellerId);
    }
}
