package com.folksdev.bookstore.service;


import com.folksdev.bookstore.model.Book;
import com.folksdev.bookstore.model.Order;
import com.folksdev.bookstore.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {


    private final Logger logger = LoggerFactory.getLogger(OrderService.class);
    private final BookService bookService;
    private final OrderRepository orderRepository;
    public OrderService(BookService bookService, OrderRepository orderRepository) {
        this.bookService = bookService;
        this.orderRepository = orderRepository;
    }
    public Order putAndOrder(List<Integer> bookIdList, String userName){
        List<Optional<Book>> bookList = bookIdList.stream()
                .map(bookService::findBookById).collect(Collectors.toList());

        Double totalPirce = bookList.stream()
                .map(optionalBook -> optionalBook.map(Book::getPrice).orElse(0.0))
                .reduce(0.0,Double::sum);

        Order order = Order.builder()
                .bookList(bookIdList)
                .totalPrice(totalPirce)
                .userName(userName)
                .build();
        return orderRepository.save(order);
    }
}
