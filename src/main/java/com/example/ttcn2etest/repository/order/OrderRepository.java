package com.example.ttcn2etest.repository.order;

import com.example.ttcn2etest.model.etity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByUserId(Long userId);
    List<Order> findByStatus(String status);

    @Query(value = "SELECT * FROM `order` WHERE status = 'PAID' AND user_id = :userId", nativeQuery = true)
    List<Order> findPaidOrdersByUserId(@Param("userId") Long userId);

    @Query(value = "select * from `order` where order_id = :orderId", nativeQuery = true)
    Optional<Order> findOrderByOrderId(@Param("orderId") String orderId);

    @Query ("SELECT MONTH(o.paymentDate) AS month, YEAR(o.paymentDate) AS year, SUM(o.amount) AS totalCost " +
            "FROM Order o WHERE YEAR(o.paymentDate) = :year  GROUP BY YEAR(o.paymentDate), MONTH(o.paymentDate)")
    List<Object[]> getTotalCostByMonthInYear(int year);
}
