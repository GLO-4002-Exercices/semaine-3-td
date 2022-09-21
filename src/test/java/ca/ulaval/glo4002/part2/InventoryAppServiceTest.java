package ca.ulaval.glo4002.part2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.*;

class InventoryAppServiceTest {
    private static final String A_PRODUCT_NAME = "laptop";
    private static final int A_QUANTITY = 2;

    private ProductFactory factory;
    private ProductRepository repository;
    private InventoryAppService service;

    @BeforeEach
    void before() {
        factory = mock(ProductFactory.class);
        repository = mock(ProductRepository.class);
        service = new InventoryAppService(factory, repository);
    }

    @Test
    void whenSearchingProducts_thenSearchesInTheStorageByName() {
        willReturn(List.of()).given(repository).findByName(anyString());

        service.searchProducts(A_PRODUCT_NAME);

        Mockito.verify(repository).findByName(A_PRODUCT_NAME);
    }

    @Test
    void givenManyProductsWithTheName_whenSearchingForProducts_thenOnlyReturnsTheOnesInStock() {
        Product productWithoutQuantity = givenProductWithQuantity(0);
        Product productWithQuantity = givenProductWithQuantity(A_QUANTITY);
        willReturn(List.of(productWithoutQuantity, productWithQuantity)).given(repository).findByName(anyString());

        List<Product> products = service.searchProducts(A_PRODUCT_NAME);

        Assertions.assertEquals(1, products.size());
        Assertions.assertEquals(productWithQuantity, products.get(0));
    }

    @Test
    void whenAddingProduct_thenCreatesItWithNameAndQuantity() {
        Product product = mock(Product.class);
        willReturn(product).given(factory).create(anyString(), anyInt());

        service.addProduct(A_PRODUCT_NAME, A_QUANTITY);

        Mockito.verify(factory).create(A_PRODUCT_NAME, A_QUANTITY);
    }

    @Test
    void givenItemCanBeSold_whenAddingProduct_thenSavesIt() {
        Product productThatCanBeSold = givenProductThatCanBeSold();
        willReturn(productThatCanBeSold).given(factory).create(anyString(), anyInt());

        service.addProduct(A_PRODUCT_NAME, A_QUANTITY);

        verify(repository).insert(productThatCanBeSold);
    }

    @Test
    void givenAnItemThatCannotBeSold_whenAddingProduct_thenDoesNotSaveIt() {
        Product productThatCannotBeSold = givenProductThatCannotBeSold();
        willReturn(productThatCannotBeSold).given(factory).create(anyString(), anyInt());

        service.addProduct(A_PRODUCT_NAME, A_QUANTITY);

        verify(repository, never()).insert(any());
    }

    @Test
    void whenUpdatingAQuantity_thenSendsTheUpdateRequestToTheStorage() {
        int newQuantity = 3;

        service.updateQuantity(A_PRODUCT_NAME, newQuantity);

        ArgumentCaptor<UpdateQuantityRequest> captor = ArgumentCaptor.forClass(UpdateQuantityRequest.class);
        verify(repository).updateQuantity(captor.capture());
        Assertions.assertEquals(A_PRODUCT_NAME, captor.getValue().name());
        Assertions.assertEquals(newQuantity, captor.getValue().quantity());
    }

    private Product givenProductThatCannotBeSold() {
        Product productThatCannotBeSold = mock(Product.class);
        willReturn(false).given(productThatCannotBeSold).canBeSold();
        return productThatCannotBeSold;
    }

    private Product givenProductThatCanBeSold() {
        Product productThatCanBeSold = mock(Product.class);
        willReturn(true).given(productThatCanBeSold).canBeSold();
        return productThatCanBeSold;
    }

    private Product givenProductWithQuantity(int quantity) {
        Product productWithQuantity = mock(Product.class);
        willReturn(quantity).given(productWithQuantity).quantity();
        return productWithQuantity;
    }
}

