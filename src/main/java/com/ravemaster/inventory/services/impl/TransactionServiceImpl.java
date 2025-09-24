package com.ravemaster.inventory.services.impl;

import com.ravemaster.inventory.domain.dto.TransactionDto;
import com.ravemaster.inventory.domain.dto.TransactionDtoSecond;
import com.ravemaster.inventory.domain.dto.TransactionLineDto;
import com.ravemaster.inventory.domain.entity.Product;
import com.ravemaster.inventory.domain.entity.Transaction;
import com.ravemaster.inventory.domain.entity.TransactionLine;
import com.ravemaster.inventory.domain.request.TransactionLineRequest;
import com.ravemaster.inventory.domain.request.TransactionRequest;
import com.ravemaster.inventory.mapper.TransactionLineMapper;
import com.ravemaster.inventory.mapper.TransactionMapper;
import com.ravemaster.inventory.repository.ProductRepository;
import com.ravemaster.inventory.repository.TransactionRepository;
import com.ravemaster.inventory.services.TransactionService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository repository;
    private final ProductRepository productRepository;
    private final TransactionMapper mapper;
    private final TransactionLineMapper lineMapper;

    @Override
    @Transactional
    public TransactionDto createTransaction(TransactionRequest request) {

        Double total = 0.0;

        List<TransactionLine> transactionLines = new ArrayList<>();

        List<Product> products = new ArrayList<>();

        Transaction transaction = Transaction.builder()
                .transactionType(request.getTransactionType())
                .saleType(request.getSaleType())
                .build();

        for(TransactionLineRequest lineRequest: request.getTransactionLineRequests()){
            Integer quantity = lineRequest.getQuantity();
            Double unitPrice = lineRequest.getUnitPrice();
            Double lineTotal = quantity * unitPrice;
            total += lineTotal;
            Product product = productRepository.findById(lineRequest.getProductId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Product does not exist"
                    ));

            if (transaction.getTransactionType().equalsIgnoreCase("Sale")){
                if (product.getStockQuantity()<=0){
                    throw new IllegalArgumentException("Inventory quantity for product: "+product.getName()+" is zero");
                } else if (product.getStockQuantity()<quantity){
                    throw new IllegalArgumentException("Inventory quantity for product: "+product.getName()+" is less than transaction quantity");
                } else {
                    product.setStockQuantity(product.getStockQuantity()-quantity);
                }
            } else {
                product.setStockQuantity(product.getStockQuantity()+quantity);
            }
            products.add(product);
            TransactionLine transactionLine = TransactionLine.builder()
                    .quantity(quantity)
                    .product(product)
                    .unitPrice(unitPrice)
                    .lineTotal(lineTotal)
                    .transaction(transaction)
                    .build();
            transactionLines.add(transactionLine);
        }

        transaction.setTransactionLines(transactionLines);
        transaction.setTotalAmount(total);

        productRepository.saveAll(products);
        Transaction savedTransaction = repository.save(transaction);

        return mapper.toDto(savedTransaction);
    }

    @Override
    public Page<TransactionDto> getTransactions(Pageable pageable) {
        Page<Long> idpage = repository.findAllTransactionIds(pageable);
        List<Transaction> transactions = repository.findByIds(idpage.getContent());
        List<TransactionDto> collected = transactions.stream().map(mapper::toDto).toList();
        return new PageImpl<>(collected, pageable, idpage.getTotalElements());
    }

    @Override
    public TransactionDtoSecond getTransactionWithLines(UUID id) {
        Transaction byId = repository.findByIdWithLines(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Transaction could not be found with id: "+ id
                ));
        List<TransactionLine> transactionLines = byId.getTransactionLines();
        List<TransactionLineDto> list = transactionLines.stream().map(lineMapper::toDto).toList();
        TransactionDtoSecond dtoSecond = mapper.toDtoSecond(byId);
        dtoSecond.setTransactionLines(list);
        return dtoSecond;
    }

    @Override
    public void deleteTransaction(UUID id) {
        repository.deleteById(id);
    }
}
