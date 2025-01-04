package com.example.ttcn2etest.repository.order;

import com.example.ttcn2etest.model.etity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query ("SELECT MONTH(o.paymentDate) AS month, YEAR(o.paymentDate) AS year, SUM(o.amount) AS totalCost " +
            "FROM Order o WHERE YEAR(o.paymentDate) = :year  GROUP BY YEAR(o.paymentDate), MONTH(o.paymentDate)")
    List<Object[]> getTotalCostByMonthInYear(int year);
}