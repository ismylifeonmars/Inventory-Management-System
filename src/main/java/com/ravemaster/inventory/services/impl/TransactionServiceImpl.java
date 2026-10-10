package com.ravemaster.inventory.services.impl;

import com.ravemaster.inventory.domain.dto.TransactionDto;
import com.ravemaster.inventory.domain.dto.TransactionDtoSecond;
import com.ravemaster.inventory.domain.dto.TransactionLineDto;
import com.ravemaster.inventory.domain.entity.*;
import com.ravemaster.inventory.domain.enums.MovementType;
import com.ravemaster.inventory.domain.enums.ReferenceType;
import com.ravemaster.inventory.domain.request.TransactionLineRequest;
import com.ravemaster.inventory.domain.request.TransactionRequest;
import com.ravemaster.inventory.mapper.TransactionLineMapper;
import com.ravemaster.inventory.mapper.TransactionMapper;
import com.ravemaster.inventory.repository.MovementRepository;
import com.ravemaster.inventory.repository.ProductRepository;
import com.ravemaster.inventory.repository.TransactionRepository;
import com.ravemaster.inventory.repository.UserRepository;
import com.ravemaster.inventory.services.TransactionService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository repository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final TransactionMapper mapper;
    private final TransactionLineMapper lineMapper;
    private final MovementRepository movementRepository;

    @Override
    @Transactional
    public TransactionDto createTransaction(TransactionRequest request) {

        Double total = 0.0;

        List<TransactionLine> transactionLines = new ArrayList<>();

        List<MovementLine> movementLines = new ArrayList<>();

        List<Product> products = new ArrayList<>();

        Transaction transaction = Transaction.builder()
                .transactionType(request.getTransactionType())
                .saleType(request.getSaleType())
                .build();

        Movement movement = Movement.builder()
                .referenceType(ReferenceType.TRANSACTION)
                .build();

        if (transaction.getTransactionType().equalsIgnoreCase("sale")){
            movement.setMovementType(MovementType.SALE);
            movement.setReason("Stock movement as a result of a sale");
        } else if(transaction.getTransactionType().equalsIgnoreCase("purchase")){
            movement.setMovementType(MovementType.PURCHASE);
            movement.setReason("Stock movement as a result of a purchase");
        }

        User byName = userRepository.findByEmail(request.getEmail()).orElseThrow(
                () -> new EntityNotFoundException(
                        "User does not exist with name:" +request.getEmail()
                )
        );

        transaction.setUser(byName);
        movement.setPerformedBy(byName);

        for(TransactionLineRequest lineRequest: request.getTransactionLineRequests()){
            Integer quantity = lineRequest.getQuantity();
            Double unitPrice = lineRequest.getUnitPrice();
            Double lineTotal = quantity * unitPrice;
            total += lineTotal;
            Product product = productRepository.findById(lineRequest.getProductId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Product does not exist"
                    ));
            TransactionLine transactionLine = TransactionLine.builder()
                    .quantity(quantity)
                    .product(product)
                    .unitPrice(BigDecimal.valueOf(unitPrice))
                    .lineTotal(BigDecimal.valueOf(lineTotal))
                    .transaction(transaction)
                    .build();
            MovementLine movementLine = MovementLine.builder()
                    .quantity(quantity)
                    .previousQuantity(product.getStockQuantity())
                    .resultingQuantity(product.getStockQuantity()+quantity)
                    .product(product)
                    .movement(movement)
                    .build();
            product.setStockQuantity(movementLine.getResultingQuantity());
            products.add(product);
            transactionLines.add(transactionLine);
            movementLines.add(movementLine);
        }

        transaction.setTransactionLines(transactionLines);
        transaction.setTotalAmount(BigDecimal.valueOf(total));

        movement.setMovementLines(movementLines);

        productRepository.saveAll(products);
        Transaction savedTransaction = repository.save(transaction);

        movement.setReferenceId(savedTransaction.getId());
        movementRepository.save(movement);

        return mapper.toDto(savedTransaction);
    }

    @Override
    public Page<TransactionDto> getTransactions(Pageable pageable) {
        Page<UUID> idpage = repository.findAllTransactionIds(pageable);
        return new PageImpl<>(getList(idpage,pageable), pageable, idpage.getTotalElements());
    }

    @Override
    public Page<TransactionDto> getTransactionsByType(Pageable pageable, String transactionType) {
        Page<UUID> idpage = repository.findAllTransactionIdsByType(transactionType,pageable);
        return new PageImpl<>(getList(idpage,pageable), pageable, idpage.getTotalElements());
    }

    @Override
    public Page<TransactionDto> getTransactionsBySale(Pageable pageable, String saleType) {
        Page<UUID> idpage = repository.findAllTransactionIdsBySale(saleType,pageable);
        return new PageImpl<>(getList(idpage,pageable), pageable, idpage.getTotalElements());
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

    private List<TransactionDto> getList(Page<UUID> idList, Pageable pageable){
        List<Transaction> transactions = repository.findByIds(idList.getContent(),pageable.getSort());
        return transactions.stream().map(mapper::toDto).toList();
    }
}
