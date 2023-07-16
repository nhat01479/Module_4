package com.cg.repository;

import com.cg.model.Customer;
import com.cg.model.dto.customer.CustomerResDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    List<Customer> findAllByFullNameLike(String fullName);
    List<Customer> findAllByFullNameLikeOrEmailLikeOrPhoneLike(String fullName, String email, String phone);

    List<Customer> findAllByIdNot(Long id);
    @Query("select new com.cg.model.dto.customer.CustomerResDTO (c.id, c.fullName, c.email, c.phone, c.balance, c.locationRegion) from Customer as c where c.deleted = false")
    List<CustomerResDTO> findAllCustomerResDTO();
    @Query("SELECT NEW com.cg.model.dto.customer.CustomerResDTO (c.id, c.fullName, c.email, c.phone, c.balance, c.locationRegion) FROM Customer AS c WHERE c.deleted = false AND c.id != :senderId ")
    List<CustomerResDTO> findAllRecipientsWithoutSenderId(@Param("senderId") Long senderId);

    List<Customer> findAllByDeletedIs(Boolean boo);
    Boolean existsByEmail(String email);
    Boolean existsByEmailAndIdNot(String email, Long id);
    Boolean existsByEmailNot(String email);
    @Modifying
    @Query("UPDATE Customer as c set c.deleted = 1  where c.id = :id")
    void softDeleteById(@Param("id") Long id);

    @Modifying
    @Query("UPDATE Customer as c set c.balance = c.balance + :amount where c.id = :id")
    void incrementBalance(@Param("id") Long id, @Param("amount") BigDecimal amount);
    @Modifying
    @Query("UPDATE Customer as c set c.balance = c.balance - :amount where c.id = :id")
    void decrementBalance(@Param("id") Long id, @Param("amount") BigDecimal amount);
    List<Customer> findAllByDeletedFalse();
}
